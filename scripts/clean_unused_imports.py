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
    },
    "node": {
        "pattern": "*.ts",
        "regex": re.compile(r'^\s*import\s+')
    },
    "python": {
        "pattern": "*.py",
        "regex": re.compile(r'^\s*from\s[\w\.]+\simport\s(\w+)')
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
                    key = m.group(1)
                    if language == "python" and key == "date":
                        import_lines[n] = ': date,'
                    import_lines[n] = key
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


def remove_duplicate_imports(root_dir, language):
    for filename in locate(LANGUAGE_REGEX_MAP[language]["pattern"], root_dir):
        with open(filename) as f:
            all_lines = f.readlines()

        # Find duplicate import statements
        lines_to_remove = []
        for n, line in enumerate(all_lines):
            IMPORT_RE=LANGUAGE_REGEX_MAP[language]["regex"]
            import_found = IMPORT_RE.match(line)
            if import_found and line in all_lines[:n]:
                lines_to_remove.append(line)

        for line in lines_to_remove:
            all_lines.remove(line)

        if lines_to_remove:
            with open(filename, 'w') as f:
                for n, line in enumerate(all_lines):
                    f.write(line)

