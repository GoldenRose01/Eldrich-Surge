"""Generate the EnchLib preset reference from the sibling checkout."""
from pathlib import Path
import json

ROOT = Path(__file__).resolve().parents[2].parent / 'enchlib'
OUT = ROOT / 'docs/wiki'
PRESETS = ROOT / 'src/main/resources/config'
BASE = 'https://github.com/GoldenRose01/enchlib'

# Brief descriptions of the underlying vanilla mechanics, not claims of API enforcement.
DESCRIPTIONS = {
    'aqua_affinity': 'Riduce la penalità di scavo sott’acqua.',
    'bane_of_arthropods': 'Danno aggiuntivo e rallentamento contro gli artropodi.',
    'blast_protection': 'Protezione contro le esplosioni.',
    'breach': 'Riduce l’efficacia dell’armatura bersaglio.',
    'channeling': 'Richiama fulmini con il tridente durante i temporali.',
    'curse_of_binding': 'Vincola l’equipaggiamento indossato.',
    'curse_of_vanishing': 'Fa scomparire l’oggetto alla morte.',
    'density': 'Aumenta il danno di schianto della mazza in caduta.',
    'depth_strider': 'Aumenta la mobilità sott’acqua.',
    'efficiency': 'Accelera l’estrazione dei blocchi compatibili.',
    'feather_falling': 'Riduce il danno da caduta.',
    'fire_aspect': 'Incendia il bersaglio colpito.',
    'fire_protection': 'Protegge dal fuoco.',
    'flame': 'Incendia i bersagli con le frecce.',
    'fortune': 'Aumenta determinati drop dei blocchi.',
    'frost_walker': 'Forma ghiaccio temporaneo sull’acqua.',
    'impaling': 'Aumenta il danno del tridente contro i bersagli previsti dall’edizione.',
    'infinity': 'Consente di tirare frecce normali senza consumarle.',
    'knockback': 'Aumenta la spinta dei colpi.',
    'looting': 'Aumenta determinati drop dei mob.',
    'loyalty': 'Fa tornare il tridente lanciato.',
    'luck_of_the_sea': 'Favorisce i tesori nella pesca.',
    'lure': 'Riduce l’attesa prima dell’abbocco.',
    'mending': 'Usa esperienza raccolta per riparare l’equipaggiamento.',
    'multishot': 'La balestra scaglia più proiettili.',
    'piercing': 'I dardi attraversano più bersagli.',
    'power': 'Aumenta il danno delle frecce dell’arco.',
    'projectile_protection': 'Protegge dai proiettili.',
    'protection': 'Riduce diversi tipi di danno.',
    'punch': 'Aumenta la spinta delle frecce.',
    'quick_charge': 'Accorcia la ricarica della balestra.',
    'respiration': 'Prolunga la respirazione sott’acqua.',
    'riptide': 'Proietta il giocatore insieme al tridente nelle condizioni previste.',
    'sharpness': 'Aumenta il danno degli attacchi.',
    'silk_touch': 'Raccoglie determinati blocchi nella forma originale.',
    'smite': 'Aumenta il danno contro i non-morti.',
    'soul_speed': 'Accelera il movimento sui blocchi delle anime.',
    'sweeping_edge': 'Aumenta l’efficacia dei fendenti.',
    'swift_sneak': 'Accelera il movimento da accovacciati.',
    'thorns': 'Può infliggere danno agli attaccanti.',
    'unbreaking': 'Può evitare consumo di durabilità.',
    'wind_burst': 'Fornisce una spinta dopo gli schianti della mazza.'
}


def properties(name):
    result = {}
    for line in (PRESETS / name).read_text(encoding='utf-8-sig').splitlines():
        line = line.strip()
        if line and not line.startswith('#') and '=' in line:
            key, value = line.split('=', 1)
            result[key.strip()] = value.strip()
    return result


def write(name, text):
    (OUT / (name + '.md')).write_text(text.strip() + '\n', encoding='utf-8', newline='\n')


def build():
    OUT.mkdir(parents=True, exist_ok=True)
    levels = properties('EnchLVLmax.config')
    rarities = properties('EnchRarity.config')
    items = properties('EnchCompatibility.config')
    conflicts = properties('EnchUNcompatibility.config')
    rows = []
    for full_id, cap in sorted(levels.items()):
        identifier = full_id.split(':')[1]
        assert identifier in DESCRIPTIONS, identifier
        title = identifier.replace('_', ' ').title()
        page = 'Preset-' + identifier.replace('_', '-')
        canonical = {'curse_of_binding':'binding_curse','curse_of_vanishing':'vanishing_curse'}.get(identifier,identifier)
        other_id = 'minecraft:' + canonical
        category = items.get(full_id, items.get(other_id, '—'))
        blocked = conflicts.get(full_id, conflicts.get(other_id, ''))
        rarity = rarities.get(full_id, rarities.get(other_id, '—'))
        rows.append(f'| [{title}]({page}) | {DESCRIPTIONS[identifier]} | {cap} | `{rarity}` | `{category}` |')
        body = [f'# {title}', '[← Catalogo preset](Incantamenti) · [Regole](Tavoli-e-rarita) · [Configurazione](Configurazione)',
            DESCRIPTIONS[identifier],
            '| Proprietà del preset | Valore |\n|---|---|\n' +
            f'| ID nel preset | `{full_id}` |\n| Massimo configurato iniziale | {cap} |\n| Rarità | `{rarity}` |\n| Categorie oggetti nel preset | `{category}` |\n| Incompatibilità elencate nel preset | {blocked or "Nessuna voce"} |',
            '## Come si applicano queste regole',
            'Il massimo riportato è il **preset EnchLib**, non il massimo vanilla. La prima registrazione può inizializzare il valore del mondo; le scelte successive vengono conservate. La potenza dell’effetto resta quella implementata da Minecraft o dalla mod che lo modifica. I cap superiori non garantiscono una crescita lineare.',
            'Le categorie e incompatibilità riportate provengono dai file di preset inclusi: controlla la voce attiva in `EnchantmentsDetails.json5` e che il sistema interessato legga tali regole. Una categoria memorizzata non modifica da sola qualsiasi oggetto o tavolo esterno.',
            '## Fonti', f'- [Preset livelli]({BASE}/blob/main/src/main/resources/config/EnchLVLmax.config)\n- [Preset oggetti]({BASE}/blob/main/src/main/resources/config/EnchCompatibility.config)\n- [Preset incompatibilità]({BASE}/blob/main/src/main/resources/config/EnchUNcompatibility.config)\n- [Meccanica vanilla generale](https://minecraft.wiki/w/Enchantment)']
        if canonical != identifier:
            body.insert(3, f'> Il preset usa il nome storico `{full_id}`. Il registry vanilla usa `{other_id}`: verifica con autocompletamento e `validate`. La configurazione non registra né converte automaticamente un ID.')
        write(page, '\n\n'.join(body))
    write('Incantamenti', '\n\n'.join(['# 📖 Incantamenti e preset', '[Home](Home) · [Configurazione](Configurazione) · [Tavoli e rarità](Tavoli-e-rarita)',
        'EnchLib gestisce gli ID trovati nel registry; non introduce una seconda collezione di effetti vanilla. Questa tabella descrive tutte le **42 voci del preset livelli incluso**, con schede individuali.',
        '> **Max preset ≠ massimo vanilla.** I valori inizializzano la configurazione; il loro effetto dipende dalle operazioni che li consultano. Le incompatibilità e gli oggetti dei preset possono differire dalle regole vanilla effettive.',
        '| Incantamento | Effetto vanilla generale | Max preset | Rarità | Oggetti nel preset |\n|---|---|---|---|---|\n'+'\n'.join(rows),
        '## Incantamenti aggiunti da altre mod',
        'Il registry può contenere altri ID non elencati nei preset inclusi. EnchLib può memorizzarne i metadati senza riscriverne automaticamente gli effetti. Per tutti i 104 incantamenti di Eldrich Surge consulta il [catalogo dedicato](https://github.com/GoldenRose01/Eldrich-Surge/wiki/Enchantments).',
        '## ID storici', 'I preset includono `curse_of_binding` e `curse_of_vanishing`; il registry vanilla usa `binding_curse` e `vanishing_curse`. Le rispettive schede evidenziano la differenza. Non copiare un ID dal preset senza verificarlo nel gioco corrente.']))
    print(f'Generated {len(levels)} preset pages and index in {OUT}')


if __name__ == '__main__':
    build()
