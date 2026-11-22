"""Convert the supported Eldrich Surge definitions to Horizon data."""

import json
import math
from pathlib import Path

HORIZON = Path(__file__).resolve().parents[1]
SOURCE = HORIZON.parent / "src/main/resources"
NAMESPACE = "eldrich-surge"
SUPPORTED = {"katana", "smithcrafts", "impaler_reach"}


def lookup_effect(amount):
    maximum = amount["max_level"]
    base = amount["base"]
    step = amount["per_level_above_first"]
    values = []
    for level in range(1, 256):
        effective = level if level <= maximum else maximum + math.log1p(level - maximum)
        values.append(base + step * (effective - 1))
    return {"type": "minecraft:lookup", "values": values, "fallback": values[-1]}


def read(path):
    return json.loads((SOURCE / path).read_text(encoding="utf-8"))


def write(path, data):
    path = HORIZON / path
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def main():
    tags = set()
    for name in sorted(SUPPORTED):
        path = f"data/{NAMESPACE}/enchantment/{name}.json"
        definition = read(path)
        for modifier in definition["effects"]["minecraft:attributes"]:
            modifier["amount"] = lookup_effect(modifier["amount"])
        if name == "impaler_reach":
            definition["supported_items"] = "#minecraft:spears"
        if name == "katana":
            tags.add("additional_damage")
        if name == "impaler_reach":
            tags.add("weapon_utility")
        write(path, definition)

    for name in sorted(tags):
        path = f"data/{NAMESPACE}/tags/enchantment/{name}.json"
        data = read(path)
        data["values"] = [
            value for value in data["values"]
            if not value.startswith(NAMESPACE + ":") or value.split(":", 1)[1] in SUPPORTED
        ]
        if name == "additional_damage":
            data["values"].append("minecraft:sharpness")
        write(path, data)

    for locale in ("en_us", "it_it"):
        path = f"assets/{NAMESPACE}/lang/{locale}.json"
        language = read(path)
        prefixes = tuple(f"enchantment.{NAMESPACE}.{name}" for name in SUPPORTED)
        write(path, {key: value for key, value in language.items()
                     if key in prefixes or key.startswith(tuple(p + "." for p in prefixes))})


if __name__ == "__main__":
    main()
