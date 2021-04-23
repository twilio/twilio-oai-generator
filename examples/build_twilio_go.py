from pathlib import Path
import os
import re
import argparse

def get_domain_info(domain, oai_spec_location, is_file=False):
    full_path = os.path.join(oai_spec_location, domain) if not is_file else oai_spec_location
    fullname = os.path.splitext(domain)[0]
    regex_splits = re.split(r'(.+?(?=_v[0-9]+))', fullname, flags=re.IGNORECASE)
    splits = list(filter(None, regex_splits))
    domain_name = splits[0].replace('twilio_', "")
    api_version = (splits[1]).replace('_', '') if len(splits) > 1 else ''
    return domain_name, full_path, api_version


def get_project_version():
    from xml.etree import ElementTree as etree
    tree = etree.ElementTree()
    parent_dir = Path(__file__).parent.parent
    pom = f"{parent_dir}/pom.xml"
    tree.parse(pom)
    namespace = "{http://maven.apache.org/POM/4.0.0}"
    version = tree.getroot().findtext(f"{namespace}version")
    return version


def build(openapi_spec_path, go_path, terraform=False):
    project_version = get_project_version()
    if os.path.isfile(openapi_spec_path):
        folder, domain = os.path.split(openapi_spec_path)
        generate(openapi_spec_path, go_path, project_version, domain, True, terraform)
    else:
        for domain in os.listdir(openapi_spec_path):
            generate(openapi_spec_path, go_path, project_version, domain, terraform)


def generate(openapi_spec_path, go_path, project_version, domain, is_file=False, terraform=False):
    domain_name, full_path, api_version = get_domain_info(domain, openapi_spec_path, is_file)
    parent_dir = Path(__file__).parent.parent
    to_generate = "terraform-provider-twilio" if terraform else "twilio-go"
    command = f"cd {parent_dir} && java -cp ./openapi-generator-cli.jar:target/twilio-go-openapi-generator-{project_version}.jar " \
              f"org.openapitools.codegen.OpenAPIGenerator generate -g {to_generate} -i {full_path} -o " \
              f"{go_path}/twilio/rest/{domain_name}/{api_version}"
    os.system(command)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Generate code from twilio-oai-generator')
    parser.add_argument("spec_path", type=str, help="path to open api specs")
    parser.add_argument("output_path", type=str, help="path to output the generated code")
    parser.add_argument("-t", "--terraform", help="generate twilio terraform provider", action="store_true")
    args = parser.parse_args()
    build(args.spec_path, args.output_path, args.terraform)
