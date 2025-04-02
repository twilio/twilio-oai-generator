#!/usr/bin/env python3

import argparse
import glob
import os
import scripts.build_twilio_library as build_library


def parse_args():
    parser = argparse.ArgumentParser(
        description="Generate files for Twilio library in different languages."
    )
    parser.add_argument(
        "--language",
        choices=["csharp", "java", "node", "php", "go", "python", "ruby", "terraform"],
        required=True,
        help="Specify the target language for code generation."
    )
    parser.add_argument(
        "--base",
        default=".",
        help="Base directory for code generation. Defaults to current directory."
    )
    parser.add_argument(
        "--input",
        default=None,
        help="Path to the OpenAPI spec JSON files. If not specified, defaults to 'spec/json' inside the base directory."
    )
    return parser.parse_args()


def get_output_dir(base, language):
    """
    Constructs the output directory for each language based on the given base path.
    Update these paths as needed for your environment or repository layout.
    """
    if language == "csharp":
        return os.path.join(base, "twilio-csharp/src/Twilio")
    elif language == "java":
        return os.path.join(base, "twilio-java/src/main/java/com/twilio")
    elif language == "node":
        return os.path.join(base, "twilio-node/src")
    elif language == "php":
        return os.path.join(base, "twilio-php/src/Twilio")
    elif language == "go":
        return os.path.join(base, "twilio-go")
    elif language == "python":
        return os.path.join(base, "twilio-python/twilio")
    elif language == "ruby":
        return os.path.join(base, "twilio-ruby/lib/twilio-ruby")
    elif language == "terraform":
        return os.path.join(base, "terraform-provider-twilio")
    else:
        return None


def generate_files(input_dir, output_dir, language):
    # If needed, you can iterate over the input spec files here using glob:
    # for filepath in glob.glob(os.path.join(input_dir, "*.json")):
    #     print(filepath)
    build_library.build(input_dir, output_dir, language)


def main():
    args = parse_args()

    # Determine input directory
    input_dir = args.input if args.input else os.path.join(args.base, "spec/json")

    # Determine output directory for the chosen language
    output_dir = get_output_dir(args.base, args.language)

    if not output_dir:
        print("Unsupported language or path couldn't be resolved.")
        return

    # Ensure the output directory exists
    os.makedirs(output_dir, exist_ok=True)

    # Run a make install (if needed for your environment)
    os.system("make install")

    # Generate files
    generate_files(input_dir, output_dir, args.language)

    # Optional language-specific post-processing
    if args.language == "go":
        os.chdir(output_dir)
        os.system("go fmt ./...")
        os.system("goimports -w .")


if __name__ == "__main__":
    main()
