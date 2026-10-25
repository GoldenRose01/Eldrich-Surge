"""Build the source-backed Eldrich Surge wiki; leave hand-written guides intact."""
from pathlib import Path
import json
import re
import shutil
from collections import defaultdict

ROOT = Path(__file__).resolve().parents[2]
OUT = ROOT / 'docs/wiki'
DATA = ROOT / 'src/main/resources/data'
DEFINITIONS = ROOT / 'src/main/java/eldritch/surge/enchantment/ench'
REPO = 'https://github.com/GoldenRose01/Eldrich-Surge'
RAW = 'https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge'


def split_arguments(text):
    result, start, depth, quoted, escaped = [], 0, 0, False, False
    for i, char in enumerate(text):
        if quoted:
            if escaped:
                escaped = False
            elif char == '\\':
                escaped = True
            elif char == '"':
                quoted = False
        elif char == '"':
            quoted = True
        elif char in '([{':
            depth += 1
        elif char in ')]}':
            depth -= 1
        elif char == ',' and depth == 0:
            result.append(text[start:i].strip())
            start = i + 1
    result.append(text[start:].strip())
    return result


def strings(text):
    return [json.loads(x) for x in re.findall(r'"(?:[^"\\]|\\.)*"', text)]


def write(name, text):
    (OUT / (name + '.md')).write_text(text.strip() + '\n', encoding='utf-8', newline='\n')


def slug(name):
    return 'Incantamento-' + name.replace('_', '-')


def link(entry):
    return f'[{entry["name"]}]({slug(entry["id"])})'


def code_list(values):
    return ', '.join(f'`{value}`' for value in values) or '—'


def table(headers, rows):
    clean = lambda value: str(value).replace('|', '\\|').replace('\n', '<br>')
    return '\n'.join(['| ' + ' | '.join(map(clean, headers)) + ' |',
                      '| ' + ' | '.join('---' for _ in headers) + ' |'] +
                     ['| ' + ' | '.join(map(clean, row)) + ' |' for row in rows])


def tag_values(tag, kind, visited=None):
    visited = set() if visited is None else visited
    if tag in visited:
        return set()
    visited.add(tag)
    namespace, path = tag.removeprefix('#').split(':', 1)
    file = DATA / namespace / 'tags' / kind / (path + '.json')
    if not file.exists():
        return {tag}
    result = set()
    for value in json.loads(file.read_text(encoding='utf-8-sig'))['values']:
        value = value if isinstance(value, str) else value['id']
        result |= tag_values(value, kind, visited) if value.startswith('#') else {value}
    return result


def collect():
    registry = (DEFINITIONS.parent / 'mechanics/ModEnchantmentDefinitions.java').read_text(encoding='utf-8-sig')
    entries = []
    for class_name in re.findall(r'(\w+)\.DEFINITION', registry):
        path = DEFINITIONS / (class_name + '.java')
        text = path.read_text(encoding='utf-8-sig')
        body = text.split('new ModEnchantmentDefinition(', 1)[1].split(');', 1)[0]
        args = split_arguments(body)
        if len(args) != 10:
            raise ValueError(f'{class_name}: expected 10 fields, found {len(args)}')
        identifier, name, category = [strings(arg)[0] for arg in args[:3]]
        resource_path = DATA / 'eldrich-surge/enchantment' / (identifier + '.json')
        resource = json.loads(resource_path.read_text(encoding='utf-8-sig'))
        entries.append(dict(id=identifier, name=name, category=category,
            declared_max=int(args[3]), rarity=strings(args[4])[0], acquisition=strings(args[5])[0],
            item_rules=strings(args[6]), slots=resource['slots'], exclusive=strings(args[8])[0],
            description=strings(args[9])[0], max=resource['max_level'], resource=resource,
            java=path.relative_to(ROOT).as_posix(), data=resource_path.relative_to(ROOT).as_posix()))
    ids = [entry['id'] for entry in entries]
    resources = {file.stem for file in (DATA / 'eldrich-surge/enchantment').glob('*.json')}
    assert len(ids) == len(set(ids)) and set(ids) == resources, 'Registry/resource mismatch'
    return sorted(entries, key=lambda entry: entry['name'].casefold())


def family(entry):
    rules = ' '.join(entry['item_rules'])
    if 'CAST SPELLS' in entry['category']: return 'Rituali'
    if 'shulker_box' in rules: return 'Inventario'
    if any(x in rules for x in ['armor', 'elytra']): return 'Armatura e mobilità'
    if any(x in rules for x in ['bow', 'crossbow']): return 'Tiro e proiettili'
    if any(x in rules for x in ['pickaxe', 'shovel', 'shears']): return 'Scavo e strumenti'
    if 'durability' in rules: return 'Durabilità'
    return 'Combattimento'


def obtaining(entry):
    identifier = entry['id']
    special = {
        'blade_of_apocalypse': 'Fusione all’incudine dei cinque componenti con una stella del Nether; escluso dai tavoli.',
        'star_fate': 'Fusione all’incudine dei cinque componenti con una stella del Nether; escluso dai tavoli.',
        'gream_reaper': 'Trasformazioni all’incudine di Katana oppure Blade of Apocalypse + Star Fate; escluso dai tavoli.',
        'deathbreak': 'Due libri con il solo Unbreaking III combinati all’incudine producono DeathBreak I; escluso dai tavoli.'}
    if identifier in special: return special[identifier]
    if 'CAST SPELLS' in entry['category']:
        return 'Libro normale + frammento di eco nel tavolo avanzato, con formazione valida per gli slot superiori.'
    values = []
    if 'Normal' in entry['acquisition']: values.append('Tavolo normale')
    if 'Advanced' in entry['acquisition']: values.append('Tavolo avanzato con ametista')
    return ' / '.join(values) or 'Nessun tavolo abilitato dalla definizione; verificare loot, comandi e impostazioni del mondo.'


def build():
    OUT.mkdir(parents=True, exist_ok=True)
    entries = collect()
    editorial = json.loads((Path(__file__).parent / 'effects.json').read_text(encoding='utf-8'))
    runtime_sources = {p: p.read_text(encoding='utf-8-sig') for p in (ROOT / 'src/main/java').rglob('*.java')
                       if '/ench/' not in p.as_posix()}
    icons = {'Combattimento':'diamond_sword', 'Armatura e mobilità':'diamond_chestplate',
             'Tiro e proiettili':'bow', 'Scavo e strumenti':'diamond_pickaxe',
             'Inventario':'bundle', 'Durabilità':'enchanted_book', 'Rituali':'enchanted_book'}
    (OUT / 'assets').mkdir(exist_ok=True)
    for icon in icons.values():
        image = ROOT / 'external_tools/config-tool/mc_itemlist' / (icon + '.png')
        if image.exists(): shutil.copy2(image, OUT / 'assets' / image.name)
    by_id = {e['id']: e for e in entries}
    groups = defaultdict(list)
    for entry in entries:
        groups[family(entry)].append(entry)
    index = ['# 📖 Catalogo degli incantamenti',
        f'**{len(entries)} incantamenti · valori del sorgente corrente · Minecraft 26.3**',
        '[Home](Home) · [Per oggetto](Per-oggetto) · [Compatibilità](Compatibilita) · [Ottenimento](Ottenimento)',
        'Ogni nome apre una scheda con effetto, oggetti, livelli, ottenimento, gruppi e riferimenti al codice. I nomi restano quelli inglesi del gioco; le spiegazioni sono in italiano.',
        '> I livelli sono massimi di definizione, modificabili dal mondo. Le categorie riportano le regole degli oggetti effettive, non soltanto l’etichetta descrittiva della classe Java. I valori non equivalgono a una garanzia di funzionamento oltre il massimo previsto.',
        '## Indice delle famiglie',
        '\n'.join(f'- [{name}](#{name.lower().replace(" ", "-").replace("à", "a")}) — {len(items)}' for name, items in groups.items())]
    for name, items in groups.items():
        # Use explicit stable anchors to avoid accents and renderer differences.
        anchor = name.lower().replace(' ', '-').replace('à', 'a')
        index.extend([f'<a name="{anchor}"></a>\n\n## {name}', table(
            ['Incantamento', 'Effetto', 'Max', 'Classe', 'Ottenimento'],
            [[link(e), editorial.get(e['id'], {}).get('summary', e['description']), e['max'], e['rarity'],
              'Fusione' if e['id'] in {'blade_of_apocalypse','star_fate','gream_reaper'} else
              'Speciale' if e['id']=='deathbreak' else e['acquisition']] for e in items])])
    write('Enchantments', '\n\n'.join(index))
    for entry in entries:
        identifier = entry['id']
        edit = editorial.get(identifier, {})
        effect = edit.get('summary', entry['description'])
        runtime = edit.get('detail')
        exclusive = entry['resource'].get('exclusive_set', entry['exclusive'])
        group_rules = exclusive if isinstance(exclusive, list) else [exclusive] if exclusive else []
        blocked = set()
        for rule in group_rules:
            blocked |= tag_values(rule, 'enchantment') if rule.startswith('#') else {rule}
        blocked.discard('eldrich-surge:' + identifier)
        blocked_links = ', '.join(link(by_id[x.split(':')[1]]) if x.startswith('eldrich-surge:') and x.split(':')[1] in by_id else f'`{x}`' for x in sorted(blocked))
        effective = entry['resource'].get('supported_items', entry['item_rules'])
        primary = entry['resource'].get('primary_items')
        fields = [['Identificatore', f'`eldrich-surge:{identifier}`'], ['Livello massimo', str(entry['max'])],
            ['Classe dichiarata', entry['rarity']], ['Oggetti — regola configuratore', code_list(entry['item_rules'])],
            ['Oggetti — datapack', code_list(effective if isinstance(effective, list) else [effective])],
            ['Slot dichiarati', code_list(entry['slots'])], ['Peso nel datapack', entry['resource']['weight']],
            ['Gruppo esclusivo dichiarato', code_list(group_rules)]]
        if primary: fields.append(['Oggetti primari nel datapack', code_list(primary if isinstance(primary,list) else [primary])])
        icon = icons[family(entry)]
        lines = [f'<img src="{RAW}/assets/{icon}.png" width="48" alt="{family(entry)}">\n\n# {entry["name"]}', '[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)',
            effect, table(['Proprietà', 'Valore predefinito'], fields), '## Effetto e utilizzo',
            runtime or ('La definizione del progetto descrive questo effetto: ' + entry['description'] +
                '\n\nPer i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.'),
            '## Ottenimento', obtaining(entry),
            'Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).',
            '## Compatibilità',
            ('L’`exclusive_set` del datapack include: ' + blocked_links + '.') if blocked_links else
            'Nessun altro incantamento risolto da un gruppo esclusivo in questa definizione. Restano applicabili le regole degli oggetti e le incompatibilità impostate nel mondo.',
            'Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.',
            '## Dati tecnici', '<details>\n<summary>Mostra effetti e costi dichiarati nel datapack</summary>\n\n```json\n' +
            json.dumps({k:entry['resource'][k] for k in ['effects','min_cost','max_cost','anvil_cost'] if k in entry['resource']},ensure_ascii=False,indent=2) + '\n```\n\n</details>',
            '## Fonti del progetto', f'- [Definizione Java]({REPO}/blob/main/{entry["java"]})\n- [Definizione datapack]({REPO}/blob/main/{entry["data"]})']
        if edit.get('sources'):
            lines.append('\n'.join(f'- [Meccanica: {Path(p).name}]({REPO}/blob/main/{p})' for p in edit['sources']))
        cited = [p.relative_to(ROOT).as_posix() for p,content in runtime_sources.items() if f'"{identifier}"' in content]
        if cited:
            lines.append('<details>\n<summary>Meccaniche che consultano questo ID</summary>\n\n' +
                '\n'.join(f'- [{Path(p).name}]({REPO}/blob/main/{p})' for p in cited) + '\n\n</details>')
        write(slug(identifier), '\n\n'.join(lines))
    object_groups = defaultdict(list)
    for entry in entries:
        for rule in entry['item_rules']:
            object_groups[rule].append(entry)
    write('Per-oggetto', '\n\n'.join(['# 🧰 Incantamenti per oggetto', '[Home](Home) · [Catalogo](Enchantments) · [Compatibilità](Compatibilita)',
        'Le righe seguono le regole dichiarate dal configuratore. Un tag raccoglie tutti gli oggetti che ne fanno parte, anche aggiunti da datapack. Gli oggetti primari del tavolo possono essere un sottoinsieme: consulta la scheda del singolo incantamento.',
        table(['Oggetto o tag', 'Incantamenti'], [[f'`{rule}`', ', '.join(link(e) for e in items)] for rule,items in sorted(object_groups.items())])]))
    write('Compatibilita', '\n\n'.join(['# ⚖️ Compatibilità e gruppi', '[Home](Home) · [Catalogo](Enchantments) · [Incudine](Incudine)',
        '## Come leggere le incompatibilità',
        'Il datapack dichiara `exclusive_set`; le impostazioni del mondo aggiungono regole esplicite per coppie di incantamenti. Il risultato dipende dal sistema che consulta tali regole. Un’etichetta di gruppo non autorizza a ignorare le eccezioni delle fusioni.',
        '## Gruppi dichiarati nei tag del progetto',
        table(['Tag', 'Componenti'], [[f'`eldrich-surge:{p.stem}`', ', '.join(link(by_id[v.split(':')[1]]) if v.startswith('eldrich-surge:') and v.split(':')[1] in by_id else f'`{v}`' for v in json.loads(p.read_text(encoding='utf-8-sig'))['values'])]
            for p in sorted((DATA / 'eldrich-surge/tags/enchantment').glob('*.json'))]),
        '## Limite dei componenti', 'Il filtro delle offerte blocca un ulteriore incantamento di categoria quando sull’oggetto sono già presenti almeno tre componenti. Le cinque componenti per Blade of Apocalypse o Star Fate vanno preparate secondo le regole dei libri e delle fusioni all’incudine; il limite del tavolo non descrive da solo ogni percorso di combinazione.',
        '## Eccezioni e impostazioni', 'EnchLib può fornire categorie di oggetti e coppie incompatibili. Le coppie impostate con la sua API sono simmetriche. Forzare un incantamento tramite comandi non dimostra che l’oggetto sia ottenibile nei tavoli o che il suo effetto si attivi.',
        f'[Fonte: filtro dei tavoli]({REPO}/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)']))
    print(f'Generated {len(entries)} enchantment pages and 3 indexes in {OUT}')


if __name__ == '__main__':
    build()
