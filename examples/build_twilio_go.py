import argparse
import os
import re
from pathlib import Path

'''
Subdirectories map for maintaining directory 
structures specific to language style. 
'''
subdirectories = {
    "go": "rest",
    "terraform": "twilio/resources",
    "java": "rest"
}


def get_domain_info(domain, oai_spec_location, is_file=False):
    full_path = os.path.join(oai_spec_location, domain) if not is_file else oai_spec_location
    fullname = os.path.splitext(domain)[0]
    regex_splits = re.split(r'(.+?(?=_v[0-9]+))', fullname, flags=re.IGNORECASE)
    splits = list(filter(None, regex_splits))
    domain_name = splits[0].replace('twilio_', "")
    api_version = (splits[1]).replace('_', '') if len(splits) > 1 else ''
    return domain_name, full_path, api_version


def build(openapi_spec_path, go_path, language='go'):
    if os.path.isfile(openapi_spec_path):
        folder, domain = os.path.split(openapi_spec_path)
        generate(openapi_spec_path, go_path, domain, True, language)
    else:
        for domain in os.listdir(openapi_spec_path):
            generate(openapi_spec_path, go_path, domain, False, language)


def generate(openapi_spec_path, go_path, domain, is_file=False, language='go'):
    domain_name, full_path, api_version = get_domain_info(domain, openapi_spec_path, is_file)
    parent_dir = Path(__file__).parent.parent

    to_generate = "terraform-provider-twilio" if language == "terraform" else f"twilio-{language}"
    sub_dir = subdirectories.get(language)
    output_path = f"{go_path}/{sub_dir}/{domain_name}"
    if language != "java":
        output_path += f"/{api_version}"
    command = f"cd {parent_dir} && java -cp ./openapi-generator-cli.jar:target/twilio-openapi-generator.jar " \
              f"org.openapitools.codegen.OpenAPIGenerator generate -g {to_generate} -i {full_path} -o " \
              f"{output_path} " \
              f"> /dev/null"  # Suppress stdout
    print(f"Generating {output_path} from {full_path}")
    os.system(command)


if __name__ == "__main__":
    example_text = '''example:
    
     python3 examples/build_twilio_go.py /path/to/twilio-oai/spec/yaml /path/to/twilio-go -l go
     python3 examples/build_twilio_go.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/twilio-go -l go
     python3 examples/build_twilio_go.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/terraform-provider-twilio -l terraform
     python3 examples/build_twilio_go.py /path/to/twilio-oai/spec/yaml/twilio_accounts_v1.yaml /path/to/terraform-provider-twilio -l terraform
     python3 examples/build_twilio_go.py /path/to/twilio-oai/spec/yaml /path/to/terraform-provider-twilio --lang terraform'''
    parser = argparse.ArgumentParser(description='Generate code from twilio-oai-generator', epilog=example_text,
                                     formatter_class=argparse.RawTextHelpFormatter)
    parser.add_argument("spec_path", type=str, help="path to open api specs")
    parser.add_argument("output_path", type=str, help="path to output the generated code")
    parser.add_argument("-l", "--lang", type=str, choices=['go', 'terraform', 'java'], required=True,
                        help="generate twilio-go/terraform-provider from twilio-oai")
    args = parser.parse_args()
    build(args.spec_path, args.output_path, args.lang)
