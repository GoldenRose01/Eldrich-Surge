"""Build self-contained localized wiki editions from reviewed text and registry data.

No network service, credentials, or runtime dependencies are needed.
All page names are flat so GitHub Wiki resolves them unambiguously.
"""
import json
from pathlib import Path
from build import ROOT, OUT, REPO, collect, tag_values, table

HERE = Path(__file__).parent
LANGUAGES = {'en': 'English', 'es': 'Español', 'zh': '简体中文', 'hi': 'हिन्दी',
             'ar': 'العربية', 'bn': 'বাংলা', 'pt': 'Português', 'ru': 'Русский', 'fr': 'Français'}


def load_effects():
    rows = {}
    for line in (HERE / 'translations/effects.tsv').read_text(encoding='utf-8').splitlines():
        if not line.strip() or line.startswith('#'):
            continue
        fields = line.split('\t')
        if len(fields) != 10:
            raise ValueError(f'{fields[0]}: expected ID + nine translations, got {len(fields)}')
        if fields[0] in rows:
            raise ValueError(f'Duplicate effect: {fields[0]}')
        rows[fields[0]] = dict(zip(LANGUAGES, fields[1:]))
    return rows


def page(code, name):
    return f'{code}-{name}'


def selector(name='Home', italian='Home'):
    return ' · '.join([f'[Italiano]({italian})'] +
                      [f'[{label}]({page(code, name)})' for code, label in LANGUAGES.items()])


def build():
    entries = collect()
    effects = load_effects()
    ids = {e['id'] for e in entries}
    assert set(effects) == ids, f'Translation coverage mismatch: {ids ^ set(effects)}'
    by_id = {e['id']: e for e in entries}
    for code in LANGUAGES:
        t = json.loads((HERE / f'translations/{code}.json').read_text(encoding='utf-8'))
        def link(name, label):
            return f'[{label}]({page(code, name)})'
        def save(name, body, italian='Home'):
            navigation = selector(name, italian)
            text = navigation + '\n\n' + body.strip() + '\n'
            if code == 'ar':
                text = '<div dir="rtl" lang="ar">\n\n' + text + '\n</div>\n'
            (OUT / (page(code, name) + '.md')).write_text(text, encoding='utf-8', newline='\n')
        nav = ' · '.join(link(n, t[k]) for n, k in
                         [('Home','home'), ('Guide','guide'), ('Enchantments','catalog'), ('Compatibility','compatibility')])
        save('Home', f'# Eldrich Surge — {LANGUAGES[code]}\n\n{nav}\n\n{t["intro"]}\n\n'
             f'**{len(entries)}** {t["count"]}\n\n{t["start"]}\n\n{t["notes"]}\n\n'
             f'[{t["sources"]}]({REPO})')
        save('Guide', f'# {t["guide"]}\n\n{nav}\n\n' + '\n\n'.join(t['sections']) +
             f'\n\n## {t["sources"]}\n\n' + '\n'.join(f'- [{name}]({REPO}/blob/main/{path})' for name,path in [
                 ('AdvancedEnchantmentMenu','src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java'),
                 ('SpecialAnvilRecipes','src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java'),
                 ('SpellCastMechanics','src/main/java/eldritch/surge/enchantment/mechanics/SpellCastMechanics.java'),
                 ('EnchantmentCapsConfig','src/main/java/eldritch/surge/config/EnchantmentCapsConfig.java')]))
        save('Enchantments', f'# {t["catalog"]}\n\n{nav}\n\n{t["notes"]}\n\n' + table(
            [t['enchantment'],t['effect'],t['max']],
            [[link('Enchantment-'+e['id'].replace('_','-'), e['name']),effects[e['id']][code],e['max']] for e in entries]), 'Enchantments')
        compatibility = []
        for e in entries:
            ident = e['id']
            exclusive = e['resource'].get('exclusive_set', e['exclusive'])
            rules = exclusive if isinstance(exclusive,list) else [exclusive] if exclusive else []
            blocked = set()
            for rule in rules:
                blocked |= tag_values(rule,'enchantment') if rule.startswith('#') else {rule}
            blocked.discard('eldrich-surge:'+ident)
            blocked_text = ', '.join(link('Enchantment-'+v.split(':')[1].replace('_','-'),by_id[v.split(':')[1]]['name'])
                if v.startswith('eldrich-surge:') and v.split(':')[1] in by_id else f'`{v}`' for v in sorted(blocked)) or '—'
            compatibility.append([link('Enchantment-'+ident.replace('_','-'),e['name']),blocked_text])
            def codes(value):
                return ', '.join(f'`{x}`' for x in value) if isinstance(value,list) else f'`{value}`'
            fields = [[t['id'],f'`eldrich-surge:{ident}`'],[t['max'],e['max']],
                      [t['rarity'],e['rarity']],[t['items'],codes(e['resource'].get('supported_items',e['item_rules']))],
                      [t['rules'],codes(e['item_rules'])],[t['slots'],codes(e['slots'])],
                      [t['weight'],e['resource'].get('weight','—')]]
            if 'primary_items' in e['resource']:
                fields.append([t['primary'],codes(e['resource']['primary_items'])])
            acquisition = t['special'] if ident in {'deathbreak','blade_of_apocalypse','star_fate','gream_reaper'} else (
                t['ritual'] if 'CAST SPELLS' in e['category'] else ' / '.join(
                    t[key] for marker,key in [('Normal','normal'),('Advanced','advanced')] if marker in e['acquisition']) or t['other'])
            data = {k:v for k,v in e['resource'].items() if k in {'effects','min_cost','max_cost','anvil_cost','exclusive_set'}}
            save('Enchantment-'+ident.replace('_','-'), f'# {e["name"]}\n\n{nav}\n\n'
                f'## {t["effect"]}\n\n{effects[ident][code]}\n\n' + table([t['property'],t['value']],fields) +
                f'\n\n## {t["obtaining"]}\n\n{acquisition}\n\n{link("Guide",t["guide"])}\n\n'
                f'## {t["compatibility"]}\n\n{blocked_text}\n\n{t["notes"]}\n\n'
                f'## {t["technical"]}\n\n```json\n{json.dumps(data,ensure_ascii=False,indent=2)}\n```\n\n'
                f'## {t["sources"]}\n\n[Java]({REPO}/blob/main/{e["java"]}) · '
                f'[JSON]({REPO}/blob/main/{e["data"]}) · [{t["original"]}](Incantamento-{ident.replace("_","-")})',
                'Incantamento-'+ident.replace('_','-'))
        save('Compatibility', f'# {t["compatibility"]}\n\n{nav}\n\n{t["compat_notes"]}\n\n'+
             table([t['enchantment'],t['compatibility']],compatibility),'Compatibilita')
    (OUT/'Languages.md').write_text('# Languages / Lingue\n\n'+selector()+'\n',encoding='utf-8',newline='\n')
    for filename in ['Home.md','_Sidebar.md','_Footer.md']:
        path = OUT / filename
        text = path.read_text(encoding='utf-8')
        start, end = '<!-- languages:start -->', '<!-- languages:end -->'
        if start in text:
            before, rest = text.split(start,1)
            _, after = rest.split(end,1)
            text = before.rstrip() + after
        text = text.rstrip() + f'\n\n{start}\n\n{selector()}\n\n{end}\n'
        path.write_text(text,encoding='utf-8',newline='\n')
    print(f'Generated {len(LANGUAGES)} editions, {len(entries)} enchantments per language.')


if __name__ == '__main__':
    build()
