<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Quick Hit

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Accorcia il tempo di recupero degli attacchi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:quick_hit` |
| Livello massimo | 5 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#minecraft:swords`, `#minecraft:axes` |
| Oggetti — datapack | `#eldrich-surge:enchantable/quick_hit` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Il ritardo dell’attacco viene diviso per **1 + 0,1 × livello**. Il beneficio riguarda il tempo di ricarica della barra, non un azzeramento universale dell’invulnerabilità del bersaglio.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

Nessun altro incantamento risolto da un gruppo esclusivo in questa definizione. Restano applicabili le regole degli oggetti e le incompatibilità impostate nel mondo.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {},
  "min_cost": {
    "base": 20,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 10
  },
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/QuickHitEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/quick_hit.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [QuickHitMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/QuickHitMixin.java)

</details>
