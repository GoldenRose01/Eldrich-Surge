# Wiki authoring tools

The editable wiki sources live in each project's `docs/wiki/`. Wiki repositories are separate Git checkouts under the ignored `state/wiki/` folder.

From the Eldrich Surge repository root, run:

```powershell
python external_tools/wiki/build.py
python external_tools/wiki/enchlib.py
python external_tools/wiki/artwork.py
python external_tools/wiki/validate.py
```

`build.py` extracts every registered enchantment and datapack definition. It refreshes the 104 detail pages and three indexes, merging reviewed explanations from `effects.json`. Keep those explanations in sync with mechanic changes; generation alone does not review gameplay. The EnchLib generator reads the sibling `../enchlib` checkout and documents all bundled level presets. `artwork.py` creates original SVG headers and the formation diagram without extra packages.

Do not edit generated catalog pages by hand. Edit descriptions and guides at their source, regenerate, validate, then copy the wiki files into the corresponding wiki checkout and review the Git diff before committing/pushing. Do not copy internal documentation paths or the wiki checkout itself into the published wiki.

The workflow does not build JARs or change mod versions.
