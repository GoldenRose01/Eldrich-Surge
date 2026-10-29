"""Check generated coverage, local page links, assets and SVG syntax."""
from pathlib import Path
from urllib.parse import unquote
import re
import xml.etree.ElementTree as ET
from build import ROOT, collect, slug


def validate(directory, repository):
    errors = []
    pages = {p.stem for p in directory.glob('*.md')}
    for path in directory.glob('*.md'):
        text = path.read_text(encoding='utf-8')
        if '\ufffd' in text: errors.append(f'{path.name}: invalid replacement character')
        if text.count('```') % 2: errors.append(f'{path.name}: unclosed code fence')
        for target in re.findall(r'\]\(([^)]+)\)',text):
            if target.startswith(('http:', 'https:', '#', 'mailto:')):
                if '/assets/' in target and repository in target:
                    asset = directory / 'assets' / target.split('/assets/',1)[1]
                    if not asset.exists(): errors.append(f'{path.name}: missing {asset.name}')
                continue
            name = unquote(target.split('#')[0]).removesuffix('.md')
            if name not in pages: errors.append(f'{path.name}: unknown page {target}')
        for target in re.findall(r'<img src="([^"]+)"',text):
            if '/assets/' in target and repository in target:
                asset = directory / 'assets' / target.split('/assets/',1)[1]
                if not asset.exists(): errors.append(f'{path.name}: missing {asset.name}')
    for svg in (directory / 'assets').glob('*.svg'):
        ET.parse(svg)
    if errors: raise AssertionError('\n'.join(errors))
    return len(pages)


if __name__ == '__main__':
    surge = ROOT / 'docs/wiki'
    enchlib = ROOT.parent / 'enchlib/docs/wiki'
    entries = collect()
    expected = {slug(e['id']) for e in entries}
    actual = {p.stem for p in surge.glob('Incantamento-*.md')}
    assert expected == actual, f'Enchantment coverage mismatch: {expected ^ actual}'
    assert len(list(enchlib.glob('Preset-*.md'))) == 42
    for directory,repository in [(surge,'Eldrich-Surge'),(enchlib,'enchlib')]:
        print(f'{repository}: {validate(directory,repository)} Markdown pages; links, assets and SVG valid')
    print(f'All {len(entries)} registered enchantments and 42 preset entries have individual pages.')
