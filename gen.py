import argparse
import glob
import scripts.build_twilio_library as build_library
import os

INPUT_DIR = "/Users/sbansla/Documents/code/twilio-oai/spec/json/*"
# python your_script.py --language python
def parse_args():
    parser = argparse.ArgumentParser(description="Generate files for Twilio library in different languages.")
    parser.add_argument("--language", choices=["csharp", "java", "node", "php", "go", "python", "ruby", "terraform"],
                        help="Specify the target language for code generation.")
    return parser.parse_args()

def set_output_dir(language):
    if language == "csharp":
        return "/Users/sbansla/Documents/code/twilio-csharp/src/Twilio"
    elif language == "java":
        return "/Users/sbansla/Documents/code/twilio-java/src/main/java/com/twilio"
    elif language == "node":
        return "/Users/sbansla/Documents/code/twilio-node/src"
    elif language == "php":
        return "/Users/sbansla/Documents/code/twilio-php/src/Twilio"
    elif language == "go":
        return "/Users/sbansla/Documents/code/twilio-go"
    elif language == "python":
        return "/Users/sbansla/Documents/code/twilio-python/twilio"
    elif language == "ruby":
        return "/Users/sbansla/Documents/code/twilio-ruby/lib/twilio-ruby"
    elif language == "terraform":
        return "/Users/sbansla/Documents/code/terraform-provider-twilio"
    else:
        return None

def generate_files(output_dir, language):
    os.system("make install")
    for filepath in glob.glob(INPUT_DIR):
        print(filepath)
        build_library.build(filepath, output_dir, language)

def main():
    args = parse_args()
    language = args.language
    output_dir = set_output_dir(language)

    if output_dir is None:
        print("Invalid language specified.")
        return

    generate_files(output_dir, language)

if __name__ == "__main__":
    main()
