<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bow.png" width="48" alt="Tiro e proiettili">

# Curse of Target

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Le frecce evidenziano il bersaglio colpito.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_target` |
| Livello massimo | 5 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `#minecraft:enchantable/bow`, `#minecraft:enchantable/crossbow` |
| Oggetti — datapack | `#eldrich-surge:enchantable/curse_of_target` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 10 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:positive_curses` |

## Effetto e utilizzo

L’effetto Glowing dura **40 × livello tick**, cioè 2 secondi per livello a 20 TPS. Questo valore runtime è diverso dal vecchio testo che indicava un secondo per livello.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Curse of Perish](Incantamento-curse-of-perish), [Curse of Spider](Incantamento-curse-of-spider).

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {
    "minecraft:post_attack": [
      {
        "affected": "victim",
        "effect": {
          "type": "minecraft:apply_mob_effect",
          "to_apply": "minecraft:glowing",
          "min_duration": {
            "type": "minecraft:linear",
            "base": 1.0,
            "per_level_above_first": 1.0
          },
          "max_duration": {
            "type": "minecraft:linear",
            "base": 1.0,
            "per_level_above_first": 1.0
          },
          "min_amplifier": 0,
          "max_amplifier": 0
        },
        "enchanted": "attacker",
        "requirements": {
          "type": "minecraft:entity_properties",
          "entity": "direct_attacker",
          "predicate": {
            "minecraft:entity_type": "minecraft:arrow"
          }
        }
      }
    ]
  },
  "min_cost": {
    "base": 5,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 35,
    "per_level_above_first": 10
  },
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfTargetEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_target.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ProjectileEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ProjectileEnchantmentMechanics.java)

</details>
