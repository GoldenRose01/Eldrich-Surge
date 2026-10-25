<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bundle.png" width="48" alt="Inventario">

# Vacuum

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Raccoglie gli oggetti vicini dentro una Shulker Box incantata.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:vacuum` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `minecraft:shulker_box`, `minecraft:white_shulker_box`, `minecraft:orange_shulker_box`, `minecraft:magenta_shulker_box`, `minecraft:light_blue_shulker_box`, `minecraft:yellow_shulker_box`, `minecraft:lime_shulker_box`, `minecraft:pink_shulker_box`, `minecraft:gray_shulker_box`, `minecraft:light_gray_shulker_box`, `minecraft:cyan_shulker_box`, `minecraft:purple_shulker_box`, `minecraft:blue_shulker_box`, `minecraft:brown_shulker_box`, `minecraft:green_shulker_box`, `minecraft:red_shulker_box`, `minecraft:black_shulker_box` |
| Oggetti — datapack | `#eldrich-surge:enchantable/vacuum` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni 10 tick esamina gli oggetti in un volume ottenuto espandendo di 4 blocchi la hitbox del giocatore. Inserisce prima negli stack compatibili incompleti, poi negli slot vuoti della Shulker. Gli oggetti non inseriti restano nel mondo.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/VacuumEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/vacuum.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
