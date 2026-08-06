import argparse
import os

import requests
from packaging.version import InvalidVersion, Version

MODRINTH_API = "https://api.modrinth.com/v2"


def api_request(url, token=None, method="GET", body=None, params=None):
    headers = {"User-Agent": "lightoverlay release workflow @ https://github.com/lugosieben/lightoverlay"}
    if token:
        headers["Authorization"] = f"Bearer {token}"
    try:
        response = requests.request(method, url, headers=headers, json=body, params=params, timeout=30)
        response.raise_for_status()
        return response.json() if response.content else None
    except requests.RequestException as error:
        raise SystemExit(f"Modrinth API {method} {url} failed: {error}") from error


def mc_line(minecraft_version):
    try:
        return tuple(int(part) for part in minecraft_version.split(".")[:2])
    except ValueError:
        return None


def find_overlaylib_version(overlaylib_version):
    target = Version(overlaylib_version)
    if not target.local:
        raise SystemExit(f"Unrecognized OverlayLib version format: '{overlaylib_version}' (expected X.Y.Z+MC)")
    overlay_line = target.release[:2]
    minecraft_line = mc_line(target.local)

    candidates = []
    offset = 0
    while True:
        versions = api_request(
            f"{MODRINTH_API}/project/M1B8mLoH/version",
            params={"loaders": '["fabric"]', "limit": 100, "offset": offset},
        )
        if not versions:
            break
        for version in versions:
            try:
                candidate = Version(version["version_number"])
            except InvalidVersion:
                continue
            if candidate.release[:2] != overlay_line:
                continue
            if not any(mc_line(game) == minecraft_line for game in version.get("game_versions", [])):
                continue
            candidates.append((candidate, version))
        if len(versions) < 100:
            break
        offset += 100

    return max(candidates, key=lambda x: x[0])[1] if candidates else None


def main():
    parser = argparse.ArgumentParser(description="Pin the OverlayLib dependency version on a Modrinth version.")
    parser.add_argument(
        "--version-id",
        required=True,
    )
    parser.add_argument(
        "--overlaylib-version",
        required=True,
    )
    args = parser.parse_args()

    token = os.environ.get("MODRINTH_TOKEN")
    if not token:
        raise SystemExit("error: MODRINTH_TOKEN environment variable is required")

    print(f"Resolving the latest OverlayLib compatible with '{args.overlaylib_version}' on Modrinth ...")
    match = find_overlaylib_version(args.overlaylib_version)
    if match is None:
        raise SystemExit(
            f"error: no compatible OverlayLib version for '{args.overlaylib_version}' was found on Modrinth. "
        )
    print(f"Found OverlayLib version {match['version_number']} (id: {match['id']}, MC: {', '.join(match.get('game_versions', []))})")

    dependencies = [
        {"project_id": "P7dR8mSH", "dependency_type": "required"},  # fabric-api
        {"project_id": "M1B8mLoH", "version_id": match["id"], "dependency_type": "required"}, # overlaylib
        {"project_id": "1eAoo2KR", "dependency_type": "required"},  # yacl
        {"project_id": "mOgUt4GM", "dependency_type": "optional"},  # modmenu
    ]
    api_request(f"{MODRINTH_API}/version/{args.version_id}", token=token, method="PATCH", body={"dependencies": dependencies})
    print(f"Light Overlay version {args.version_id} now pins OverlayLib {match['version_number']}")


if __name__ == "__main__":
    main()
