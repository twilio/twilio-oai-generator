import glob
import scripts.build_twilio_library as build_library
import os
INPUT_DIR = "/Users/sbansla/Documents/code/twilio-oai/spec/json/*"
language = "java"

if language == "csharp":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-csharp/src/Twilio"
elif language == "java":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-java/src/main/java/com/twilio"
elif language == "node":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-node/src"
elif language == "php":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-php/src/Twilio"
elif language == "go":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-go"
elif language == "python":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-python/twilio"
elif language == "ruby":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-ruby/lib/twilio-ruby"
elif language == "terraform":
    OUTPUT_DIR = "/Users/sbansla/Documents/code/terraform-provider-twilio"
    
def generate_files():
    os.system("make install")
    for filepath in glob.glob(INPUT_DIR):
        print(filepath)
        build_library.build(filepath, OUTPUT_DIR, language)

custom_file_path = "/Users/sbansla/Documents/code/open-api/spec/messaging_bulk/v1/twilio_messaging_bulk_v1.yaml"

def custom_generate_files():
    os.system("make install")
    build_library.build(custom_file_path, OUTPUT_DIR, language)

generate_files()

#custom_generate_files()