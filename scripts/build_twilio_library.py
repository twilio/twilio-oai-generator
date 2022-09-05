import argparse
import os
import re
import json
from pathlib import Path
from typing import Tuple
from clean_java_imports import remove_unused_imports

'''
Subdirectories map for maintaining directory
structures specific to language style.
'''
subdirectories = {
    'terraform': 'twilio/resources',
    'csharp':'Rest',
}


def build(openapi_spec_path: str, output_path: str, language: str) -> None:
    if os.path.isfile(openapi_spec_path):
        folder, domain = os.path.split(openapi_spec_path)
        generate(openapi_spec_path, output_path, language, domain, True)
    else:
        for domain in sorted(os.listdir(openapi_spec_path)):
            generate(openapi_spec_path, output_path, language, domain, False)


def generate(openapi_spec_path: str, output_path: str, language: str, domain: str, is_file: bool = False) -> None:
    full_path, domain_name, api_version = get_domain_info(openapi_spec_path, domain, is_file, language)
    parent_dir = Path(__file__).parent.parent

    to_generate = 'terraform-provider-twilio' if language == 'terraform' else f'twilio-{language}'
    is_domain_irrelevant = language in {'go', 'terraform'} and domain_name == 'preview'
    sub_dir = subdirectories.get(language, 'rest')
    output_path = os.path.join(output_path, sub_dir, domain_name)
    if language in {'go', 'terraform'}:
        output_path = os.path.join(output_path, api_version)
    if is_domain_irrelevant == False:
        run_openapi_generator(parent_dir, to_generate, output_path, full_path)
    if language == 'java':
        remove_unused_imports(output_path, 'java')


def run_openapi_generator(parent_dir: str, to_generate: str, output_path: str, full_path: str) -> None:
    command = f'cd {parent_dir} && java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar ' \
                      f'org.openapitools.codegen.OpenAPIGenerator generate -g {to_generate} ' \
                      f'--inline-schema-name-defaults arrayItemSuffix="" ' \
                      f'-i {full_path} ' \
                      f'-o {output_path} ' \
                      f'> /dev/null'  # Suppress stdout
    print(f'Generating {output_path} from {full_path}')
    if os.system(command) != 0:
        raise RuntimeError()
    print(f'Code generation completed at {output_path}')


def get_domain_info(oai_spec_location: str, domain: str, is_file: bool = False, language: str = "") -> Tuple[str, str, str]:
    full_path = oai_spec_location if is_file else os.path.join(oai_spec_location, domain)
    parts = re.split(r'twilio_(.+?)_?(v\d+)?\.', domain, flags=re.IGNORECASE)
    domain_name = parts[1]
    api_version = parts[2] or ''
    # added logic to fetch the domain name from servers url in spec file, instead for relying on file name
    if language == 'java' and full_path.endswith('.json'):
        domain_name = parse_domain_name(full_path)
    if language in {'csharp'}:
        domain_name = domain_name.capitalize()
    return full_path, domain_name, api_version


def parse_domain_name(oai_spec_location_path: str):
    server_regex = '^(?:https?://)?(?:[^@/\n]+@)?([^:/?\n.]+)'
    with open(oai_spec_location_path, 'r') as f:
        file_content = json.load(f)
    domain_from_server_url = re.search(
        server_regex, file_content["servers"][0]["url"]).group(1)
    domain_name = domain_from_server_url.replace('-', '').lower()
    return domain_name


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
