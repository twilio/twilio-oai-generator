import os
import re
import sys

regex = ".+?(?=_v[0-9]+)"

def build(openapi_spec_path, go_path):
    for domain in os.listdir(openapi_spec_path):
        full_path = os.path.join(openapi_spec_path, domain)
        fullname = os.path.splitext(domain)[0]

        regex_splits = re.split(r'(.+?(?=_v[0-9]+))', fullname, flags=re.IGNORECASE)
        splits = list(filter(None, regex_splits))

        domain_name = splits[0].replace('twilio_', "")
        version = (splits[1]).replace('_', '') if len(splits) > 1 else ''

        command = f"java -cp ./openapi-generator-cli.jar:target/twilio-go-openapi-generator-1.0.0.jar " \
                  f"org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i {full_path} -o " \
                  f"{go_path}/twilio/rest/{domain_name}/{version} --ignore-file-override=./.openapi-generator "
        os.system(command)

if __name__ == "__main__":
    if len(sys.argv) < 3:
        raise Exception(f'Usage: python {sys.argv[0]} path/to/apis/yaml path/to/twilio-go')
    build(sys.argv[1], sys.argv[2])
