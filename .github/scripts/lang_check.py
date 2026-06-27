import json
import os
import sys
from pathlib import Path

LANG_DIR = Path("src/main/resources/assets/light-overlay/lang")
MAIN_FILE = "en_us.json"

def main():
    if not LANG_DIR.exists() or not (LANG_DIR / MAIN_FILE).exists():
        print("::error::Language directory or main file not found.")
        sys.exit(1)

    with open(LANG_DIR / MAIN_FILE, 'r', encoding='utf-8') as f:
        master_data = json.load(f)
        master_keys = set(master_data.keys())

    summary_file = os.environ.get('GITHUB_STEP_SUMMARY')
    summary_lines = ["# Missing Translation Strings\n"]
    fail_build = False

    for lang_file in sorted(LANG_DIR.glob("*.json")):
        if lang_file.name == MAIN_FILE:
            continue

        with open(lang_file, 'r', encoding='utf-8') as f:
            lang_keys = set(json.load(f).keys())

        dead_keys = lang_keys - master_keys
        missing_keys = master_keys - lang_keys

        if dead_keys:
            fail_build = True
            print(f"::error file={lang_file}::Found {len(dead_keys)} unnecessary keys.")

        if missing_keys:
            print(f"::warning file={lang_file}::Found {len(missing_keys)} missing keys.")
            summary_lines.append("<details>")
            summary_lines.append(f"  <summary>{lang_file.name}: {len(missing_keys)} missing</summary>\n")
            summary_lines.append("  | English Name | Internal Name |")
            summary_lines.append("  |---|---|")
            for k in sorted(missing_keys):
                cleaned = str(master_data[k]).replace('|', '\\|')
                summary_lines.append(f"  | {cleaned} | `{k}` |")
            summary_lines.append("</details>\n")
        else:
            summary_lines.append(f"{lang_file.name}: Complete!\n")

    if summary_file:
        with open(summary_file, 'a', encoding='utf-8') as sf:
            sf.write("\n".join(summary_lines))

    if fail_build:
        sys.exit(1)

if __name__ == "__main__":
    main()