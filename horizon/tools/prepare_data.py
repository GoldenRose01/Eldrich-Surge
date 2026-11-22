"""Convert all Eldrich Surge data definitions, tags, and translations to Horizon data."""

import json
import math
from pathlib import Path

HORIZON = Path(__file__).resolve().parents[1]
SOURCE = HORIZON.parent / "src/main/resources"
NAMESPACE = "eldrich-surge"


def lookup_effect(amount):
    maximum = amount["max_level"]
    base = amount["base"]
    step = amount["per_level_above_first"]
    values = []
    for level in range(1, 256):
        effective = level if level <= maximum else maximum + math.log1p(level - maximum)
        values.append(base + step * (effective - 1))
    return {"type": "minecraft:lookup", "values": values, "fallback": values[-1]}


def convert_logarithmic(node):
    if isinstance(node, dict):
        if node.get("type") == "eldrich-surge:logarithmic_level":
            return lookup_effect(node)
        return {k: convert_logarithmic(v) for k, v in node.items()}
    if isinstance(node, list):
        return [convert_logarithmic(v) for v in node]
    return node


def write(path, data):
    out = HORIZON / path
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def main():
    ench_dir = SOURCE / f"data/{NAMESPACE}/enchantment"
    count = 0
    for src_file in sorted(ench_dir.glob("*.json")):
        definition = json.loads(src_file.read_text(encoding="utf-8"))
        definition = convert_logarithmic(definition)
        if src_file.stem == "impaler_reach":
            definition["supported_items"] = "#minecraft:spears"
        write(f"data/{NAMESPACE}/enchantment/{src_file.name}", definition)
        count += 1

    tags_dir = SOURCE / f"data/{NAMESPACE}/tags/enchantment"
    for src_file in sorted(tags_dir.glob("*.json")):
        data = json.loads(src_file.read_text(encoding="utf-8"))
        if src_file.stem == "additional_damage" and "minecraft:sharpness" not in data.get("values", []):
            data.setdefault("values", []).append("minecraft:sharpness")
        write(f"data/{NAMESPACE}/tags/enchantment/{src_file.name}", data)

    for locale in ("en_us", "it_it"):
        lang_file = SOURCE / f"assets/{NAMESPACE}/lang/{locale}.json"
        if lang_file.exists():
            language = json.loads(lang_file.read_text(encoding="utf-8"))
            write(f"assets/{NAMESPACE}/lang/{locale}.json", language)

    print(f"Exported {count} enchantments, tags, and translations.")


if __name__ == "__main__":
    main()
