import fnmatch
import os
import re

LANGUAGE_REGEX_MAP = {
    "java": {
        "pattern": "*.java",
        "regex": re.compile(r'^\s*import\s+[\w\.]+\.(\w+)\s*;\s*(?://.*)?$')
    },
    "php": {
        "pattern": "*.php",
        "regex": re.compile(r'^\s*use\s+[\w\\]+\\(\w+)\s*\w*\s*(\w*);\s*(?://.*)?$')
    }
}


def locate(pattern, root=os.curdir):
    for path, dirs, files in os.walk(os.path.abspath(root)):
        for file_name in fnmatch.filter(files, pattern):
            yield os.path.join(path, file_name)


def remove_unused_imports(root_dir, language):
    for filename in locate(LANGUAGE_REGEX_MAP[language]["pattern"], root_dir):
        import_lines = {}
        other_lines = []
        with open(filename) as f:
            all_lines = f.readlines()
        for n, line in enumerate(all_lines):
            IMPORT_RE=LANGUAGE_REGEX_MAP[language]["regex"]
            m = IMPORT_RE.match(line)
            if m:
                if " as " in line:
                    import_lines[n] = m.group(2)
                else:
                    import_lines[n] = m.group(1)
            else:
                other_lines.append(line)
        other_code = ''.join(other_lines)
        with open(filename, 'w') as f:
            for n, line in enumerate(all_lines):
                if (n in import_lines and
                        not re.search(r'(?<!\w)%s(?!\w)' % import_lines[n],
                                      other_code)):
                    continue
                f.write(line)
