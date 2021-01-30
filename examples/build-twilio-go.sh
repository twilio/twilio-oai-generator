# You can find the openapi-generator-cli.jar here: https://mvnrepository.com/artifact/org.openapitools/openapi-generator-cli
for domain in <path-to>/twilio-oai/spec/yaml/twilio_*.yaml; do
  fullname="$(basename "$domain")"
  filename="${fullname%.*}"
  domain_name="$(cut -d'_' -f2 <<< "$filename")"
  version="$(cut -d'_' -f3 <<< "$filename")"
  java -cp <path-to>/openapi-generator-cli.jar:target/twilio-go-openapi-generator-1.0.0.jar org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i "$domain" -o ./twilio-go/"$domain_name"/"$version" --ignore-file-override=./.openapi-generator
done
