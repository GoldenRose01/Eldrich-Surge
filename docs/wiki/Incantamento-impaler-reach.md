<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Impaler Reach

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge un blocco di portata per livello a interazioni ed entità.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:impaler_reach` |
| Livello massimo | 3 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/spear` |
| Oggetti — datapack | `#eldrich-surge:enchantable/spear` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:weapon_utility` |

## Effetto e utilizzo

Il datapack applica **+1 × livello** sia a `block_interaction_range` sia a `entity_interaction_range` negli slot dichiarati. Non usa il vecchio nome di attributo `generic.player.reach_distance`. Le incompatibilità sono quelle del gruppo `weapon_utility` e delle regole del mondo.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Gravity Well](Incantamento-gravity-well), [Seismic Wave](Incantamento-seismic-wave), [Vanguard Charge](Incantamento-vanguard-charge), `minecraft:sweeping_edge`.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {
    "minecraft:attributes": [
      {
        "amount": {
          "type": "minecraft:linear",
          "base": 1.0,
          "per_level_above_first": 1.0
        },
        "attribute": "minecraft:block_interaction_range",
        "id": "eldrich-surge:impaler_block_reach",
        "operation": "add_value"
      },
      {
        "amount": {
          "type": "minecraft:linear",
          "base": 1.0,
          "per_level_above_first": 1.0
        },
        "attribute": "minecraft:entity_interaction_range",
        "id": "eldrich-surge:impaler_entity_reach",
        "operation": "add_value"
      }
    ]
  },
  "min_cost": {
    "base": 20,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 10
  },
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/ImpalerReachEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/impaler_reach.json)
