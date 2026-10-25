<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bundle.png" width="48" alt="Inventario">

# Siphon

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Instrada gli oggetti raccolti negli stack incompleti di una Shulker Box.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:siphon` |
| Livello massimo | 1 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `minecraft:shulker_box`, `minecraft:white_shulker_box`, `minecraft:orange_shulker_box`, `minecraft:magenta_shulker_box`, `minecraft:light_blue_shulker_box`, `minecraft:yellow_shulker_box`, `minecraft:lime_shulker_box`, `minecraft:pink_shulker_box`, `minecraft:gray_shulker_box`, `minecraft:light_gray_shulker_box`, `minecraft:cyan_shulker_box`, `minecraft:purple_shulker_box`, `minecraft:blue_shulker_box`, `minecraft:brown_shulker_box`, `minecraft:green_shulker_box`, `minecraft:red_shulker_box`, `minecraft:black_shulker_box` |
| Oggetti — datapack | `#eldrich-surge:enchantable/siphon` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Cerca stack con lo stesso oggetto e gli stessi componenti nelle Shulker incantate nell’inventario e nella mano secondaria. Riempie soltanto stack già presenti e incompleti; non usa nuovi slot vuoti. Il residuo resta al normale percorso di raccolta.

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
    "base": 5,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 35,
    "per_level_above_first": 10
  },
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SiphonEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/siphon.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
