#!/usr/bin/env bash
for domain in ~/DI/apis/*.yaml; do
  fullname="$(basename "$domain")"
  filename="${fullname%.*}"
  domain_name="$(cut -d'_' -f2 <<< "$filename")"
  version="$(cut -d'_' -f3 <<< "$filename")"
#  echo "$domain_name"
  java -cp ~/packages/openapi-generator-cli.jar:target/twilio-go-openapi-generator-1.0.0.jar org.openapitools.codegen.OpenAPIGenerator generate -g twilio-go -i "$domain" -o ./twilio-go/"$domain_name"/"$version"
done

