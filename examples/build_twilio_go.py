import os
import re

regex = ".+?(?=_v[0-9]+)"

openapi_spec_path = '/Users/shwetharadhakrishna/DI/apis/yaml1'

for domain in os.listdir(openapi_spec_path):
    full_path = os.path.join(openapi_spec_path, domain)
    fullname = os.path.splitext(domain)[0]

    regex_splits = re.split(r'(.+?(?=_v[0-9]+))', fullname, flags=re.IGNORECASE)
    splits = list(filter(None, regex_splits))

    domain_name = splits[0].replace('twilio_', "")
    version = (splits[1]).replace('_', '') if len(splits) > 1 else ''

    command = f"java -cp ./openapi-generator-cli.jar:target/twilio-go-openapi-generator-1.0.0.jar " \
              f"org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i {full_path} -o " \
              f"out/twilio-go/twilio/rest/{domain_name}/{version}"
    os.system(command)
