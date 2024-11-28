import json
import re
import os

with open("twilio_am_temp.json") as f:
    specs = json.load(f)
    missing_paths = specs.get("paths")

def preprocess_orgs_spec(spec_folder:str, spec_file:str, parent_dir: str):
    with open(spec_folder + spec_file) as f:
        specs = json.load(f)
    paths = specs.get("paths")
    versioned_paths = {path: paths[path] for path in paths.keys() if re.match(r"/v\d+", path)}
    version = list(versioned_paths.keys())[0].split("/")[1]
    non_versioned_paths = missing_paths
    existing_paths = {path: paths[path] for path in paths.keys() if path not in versioned_paths.keys()}
    for path, path_value in existing_paths.items():
        x_twilio = path_value.get("x-twilio")
        if not x_twilio:
            x_twilio = {}
        if not x_twilio.get("parent"):
            x_twilio["parent"] = "/{OrganizationSid}"
        path_value["x-twilio"] = x_twilio
    non_versioned_paths.update(existing_paths)
    non_versioned_paths.update(missing_paths)
    versioned_specs = specs.copy()
    versioned_specs["paths"] = versioned_paths
    non_versioned_specs = specs.copy()
    non_versioned_specs["paths"] = non_versioned_paths
    temp_dir = os.path.join(parent_dir, "temp_specs")
    if not os.path.exists(temp_dir):
        os.makedirs(temp_dir)
    spec_file_name = spec_file.split(".")[0]
    spec_file_name_versioned = spec_file_name + "_" + version + ".json"
    spec_file_name_versionless = spec_file_name + "_versionless.json"
    with open(os.path.join(temp_dir, spec_file_name_versioned), 'w') as f:
        f.write(json.dumps(versioned_specs, indent=2))
    with open(os.path.join(temp_dir, spec_file_name_versionless), 'w') as f:
        f.write(json.dumps(non_versioned_specs, indent=2))

    with open(os.path.join(temp_dir, spec_file_name_versionless), 'r') as f:
        content = f.read()

    # fix param names
    content = content.replace("UserSid", "Id")
    content = content.replace("RoleAssignmentSid", "Sid")

    with open(os.path.join(temp_dir, spec_file_name_versionless), 'w') as file:
        file.write(content)
    return spec_file_name_versioned, spec_file_name_versionless, temp_dir
