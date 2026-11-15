<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_pickaxe.png" width="48" alt="Scavo e strumenti">

# Smelting

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Cuoce automaticamente in fornace qualsiasi drop compatibile e applica un bonus stile Fortuna fino al livello 5.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:smelting` |
| Livello massimo | 1 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:axes`, `#minecraft:pickaxes`, `#minecraft:shovels` |
| Oggetti — datapack | `#eldrich-surge:enchantable/smelting` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Consulta `RecipeType.SMELTING` per ogni drop del blocco rotto (inclusi minerali, sabbia, pietrisco, argilla e tronchi trasformati in carbonella con l’ascia). Applica inoltre un moltiplicatore stile Fortuna fino al livello 5 sugli oggetti fusi.

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SmeltingEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/smelting.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ToolEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ToolEnchantmentMechanics.java)

</details>
