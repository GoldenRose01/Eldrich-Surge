<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_pickaxe.png" width="48" alt="Scavo e strumenti">

# Veinminer

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Da accovacciato, rompe tutta la vena di minerali connessa.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:veinminer` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:pickaxes` |
| Oggetti — datapack | `#minecraft:pickaxes` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 3 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Su picconi, tenendo premuto `Shift`, scava fino a 64 blocchi connessi dello stesso minerale (incluse varianti deepslate), fermandosi se il piccone sta per rompersi e restando compatibile con Fortuna, Silk Touch e Smelting.

## Ottenimento

Tavolo normale

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
    "per_level_above_first": 10
  },
  "max_cost": {
    "base": 55,
    "per_level_above_first": 10
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/VeinminerEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/veinminer.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ToolEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ToolEnchantmentMechanics.java)

</details>
