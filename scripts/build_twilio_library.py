import argparse
import os
import re
from pathlib import Path
from typing import Tuple

from clean_java_imports import remove_unused_imports

'''
Subdirectories map for maintaining directory
structures specific to language style.
'''
subdirectories = {
    'terraform': 'twilio/resources',
    'csharp': 'Rest',
}


def build(openapi_spec_path: str, output_path: str, language: str) -> None:
    if os.path.isfile(openapi_spec_path):
        folder, domain = os.path.split(openapi_spec_path)
        generate(openapi_spec_path, output_path, language, domain, True)
    else:
        for domain in sorted(os.listdir(openapi_spec_path)):
            generate(openapi_spec_path, output_path, language, domain, False)


def generate(openapi_spec_path: str, output_path: str, language: str, domain: str, is_file: bool = False) -> None:
    full_path, domain_name, api_version = get_domain_info(openapi_spec_path, domain, is_file)
    parent_dir = Path(__file__).parent.parent

    sub_dir = subdirectories.get(language, 'rest')
    output_path = os.path.join(output_path, sub_dir)
    run_openapi_generator(parent_dir, language, output_path, full_path)
    if language == 'java':
        remove_unused_imports(output_path, 'java')


def run_openapi_generator(parent_dir: str, language: str, output_path: str, full_path: str) -> None:
    to_generate = 'terraform-provider-twilio' if language == 'terraform' else f'twilio-{language}'

    command = f'cd {parent_dir} && java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar ' \
              f'org.openapitools.codegen.OpenAPIGenerator generate -g {to_generate} ' \
              f'--inline-schema-name-defaults arrayItemSuffix="" ' \
              f'--global-property apiTests=false ' \
              f'-i {full_path} ' \
              f'-o {output_path}'

    if language in {'node'}:
        command += ' --global-property skipFormModel=false'

    print(f'Generating {output_path} from {full_path}')
    if os.system(command + '> /dev/null') != 0:  # Suppress stdout
        raise RuntimeError()
    print(f'Code generation completed at {output_path}')


def get_domain_info(oai_spec_location: str, domain: str, is_file: bool = False) -> Tuple[str, str, str]:
    full_path = oai_spec_location if is_file else os.path.join(oai_spec_location, domain)
    parts = re.split(r'twilio_(.+?)_?(v\d+)?\.', domain, flags=re.IGNORECASE)
    domain_name = parts[1]
    api_version = parts[2] or ''
    return full_path, domain_name, api_version


if __name__ == '__main__':
    example_text = '''example:

     python3 scripts/build_twilio_library.py /path/to/twilio-oai/spec/yaml /path/to/twilio-go -l go
     python3 scripts/build_twilio_library.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/twilio-go -l go
     python3 scripts/build_twilio_library.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/terraform-provider-twilio -l terraform
     python3 scripts/build_twilio_library.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/terraform-provider-twilio -l terraform
     python3 scripts/build_twilio_library.py /path/to/twilio-oai/spec/yaml /path/to/terraform-provider-twilio --lang terraform'''
    parser = argparse.ArgumentParser(description='Generate code from twilio-oai-generator', epilog=example_text,
                                     formatter_class=argparse.RawTextHelpFormatter)
    parser.add_argument('spec_path', type=str, help='path to open api specs')
    parser.add_argument('output_path', type=str, help='path to output the generated code')
    parser.add_argument('-l', '--lang', type=str, help='generate Twilio library from twilio-oai',
                        choices=['go', 'terraform', 'java', 'node', 'csharp'], required=True)
    args = parser.parse_args()
    build(args.spec_path, args.output_path, args.lang)
