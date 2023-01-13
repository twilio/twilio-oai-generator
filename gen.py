import glob
import scripts.build_twilio_library as build_library
import os
INPUT_DIR = "/Users/sbansla/Documents/code/twilio-oai/spec/json/*"
#OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-oai-generator/codegen/twilio_csharp_cmd"
OUTPUT_DIR = "/Users/sbansla/Documents/code/twilio-csharp/src/Twilio"
def generate_files():
    os.system("make install")
    for filepath in glob.glob(INPUT_DIR):
        print(filepath)
        build_library.build(filepath, OUTPUT_DIR, "csharp")


generate_files()
