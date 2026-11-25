"""Convert all Eldrich Surge data definitions, tags, assets, and bundled configs to Horizon."""

import json
import math
import shutil
from pathlib import Path

HORIZON = Path(__file__).resolve().parents[1]
SOURCE = HORIZON.parent / "src/main/resources"
REPO_ROOT = HORIZON.parent
ENCHLIB_SOURCE = Path(r"c:\Users\franc\OneDrive\Documenti\GitHub\enchlib\src\main\resources")
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


def write_json(rel_path, data):
    out = HORIZON / rel_path
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text(json.dumps(data, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def copy_tree(src_dir: Path, dst_rel: str):
    if not src_dir.exists():
        return 0
    count = 0
    for src_file in sorted(src_dir.rglob("*")):
        if src_file.is_file():
            rel = src_file.relative_to(src_dir)
            out = HORIZON / dst_rel / rel
            out.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(src_file, out)
            count += 1
    return count


def copy_file(src_file: Path, dst_rel: str):
    if not src_file.exists():
        return False
    out = HORIZON / dst_rel
    out.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src_file, out)
    return True


def main():
    # 1. Enchantment definitions (122)
    ench_dir = SOURCE / f"data/{NAMESPACE}/enchantment"
    ench_count = 0
    for src_file in sorted(ench_dir.glob("*.json")):
        definition = json.loads(src_file.read_text(encoding="utf-8-sig"))
        definition = convert_logarithmic(definition)
        if src_file.stem == "impaler_reach":
            definition["supported_items"] = "#minecraft:spears"
        write_json(f"data/{NAMESPACE}/enchantment/{src_file.name}", definition)
        ench_count += 1

    # 2. All tags recursively (enchantment, item, entity_type, block, minecraft/tags/enchantment)
    tags_root = SOURCE / f"data/{NAMESPACE}/tags"
    tag_count = 0
    if tags_root.exists():
        for src_file in sorted(tags_root.rglob("*.json")):
            rel = src_file.relative_to(tags_root)
            data = json.loads(src_file.read_text(encoding="utf-8-sig"))
            if rel.as_posix() == "enchantment/additional_damage.json" and "minecraft:sharpness" not in data.get("values", []):
                data.setdefault("values", []).append("minecraft:sharpness")
            write_json(f"data/{NAMESPACE}/tags/{rel.as_posix()}", data)
            tag_count += 1

    mc_tags = SOURCE / "data/minecraft/tags/enchantment"
    tag_count += copy_tree(mc_tags, "data/minecraft/tags/enchantment")

    # 3. Phase 4 recipes, advancements, loot tables
    copy_tree(SOURCE / f"data/{NAMESPACE}/recipe", f"data/{NAMESPACE}/recipe")
    copy_tree(SOURCE / f"data/{NAMESPACE}/advancement", f"data/{NAMESPACE}/advancement")
    copy_tree(SOURCE / f"data/{NAMESPACE}/loot_table", f"data/{NAMESPACE}/loot_table")

    # 4. Horizon native block/item content declarations
    write_json(
        f"data/{NAMESPACE}/horizon/block/advanced_enchanting_table.json",
        {
            "template": "minecraft:enchanting_table",
            "properties": {
                "hardness": 5.0,
                "explosion_resistance": 1200.0,
                "requires_tool": True,
                "light": 7,
            },
            "item": True,
        },
    )
    write_json(
        f"data/{NAMESPACE}/horizon/item/spell_book.json",
        {
            "template": "minecraft:book",
            "max_stack_size": 64,
            "rarity": "uncommon",
        },
    )

    # 5. Assets: lang, blockstates, items, models, textures
    for locale in ("en_us", "it_it"):
        lang_file = SOURCE / f"assets/{NAMESPACE}/lang/{locale}.json"
        if lang_file.exists():
            language = json.loads(lang_file.read_text(encoding="utf-8-sig"))
            write_json(f"assets/{NAMESPACE}/lang/{locale}.json", language)

    for subdir in ("blockstates", "items", "models", "textures"):
        copy_tree(SOURCE / f"assets/{NAMESPACE}/{subdir}", f"assets/{NAMESPACE}/{subdir}")

    # 6. Bundled config defaults for enchlib_bridge.rs (stored in src/defaults/ so data/ stays pure)
    caps_src = SOURCE / "config/eldrich-surge/enchantment_caps.json"
    if caps_src.exists():
        caps_text = caps_src.read_text(encoding="utf-8-sig")
        out = HORIZON / "src/defaults/enchantment_caps.json"
        out.parent.mkdir(parents=True, exist_ok=True)
        out.write_text(caps_text, encoding="utf-8")

    mob_cfg_src = ENCHLIB_SOURCE / "config/MobCategory.config"
    if mob_cfg_src.exists():
        copy_file(mob_cfg_src, "src/defaults/MobCategory.config")

    # 7. Full upstream MIT LICENSE
    license_src = REPO_ROOT / "LICENSE"
    if license_src.exists():
        copy_file(license_src, "LICENSE")

    print(f"Exported {ench_count} enchantments, {tag_count} tags, Phase 4 assets/data, configs, and LICENSE.")


if __name__ == "__main__":
    main()
