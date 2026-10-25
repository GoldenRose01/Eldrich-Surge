<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bow.png" width="48" alt="Tiro e proiettili">

# Replenish

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Può rimborsare un proiettile dopo il tiro.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:replenish` |
| Livello massimo | 3 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/bow`, `#minecraft:enchantable/crossbow` |
| Oggetti — datapack | `#eldrich-surge:enchantable/replenish` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La probabilità è `min(100, 33 × livello)%`: 33%, 66%, 99% ai primi tre livelli. I proiettili marcati `INTANGIBLE_PROJECTILE` sono esclusi. Il metodo corrente non limita il rimborso soltanto alle frecce con effetti; il rimborso va nell’inventario o cade a terra se pieno.

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
  "effects": {
    "minecraft:ammo_use": [
      {
        "effect": {
          "type": "minecraft:set",
          "value": 0.0
        },
        "requirements": {
          "type": "minecraft:all_of",
          "terms": [
            {
              "chance": {
                "type": "minecraft:enchantment_level",
                "amount": {
                  "type": "minecraft:linear",
                  "base": 0.33,
                  "per_level_above_first": 0.33
                }
              },
              "type": "minecraft:random_chance"
            },
            {
              "type": "minecraft:match_tool",
              "predicate": {
                "items": "#eldrich-surge:special_arrows"
              }
            }
          ]
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
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/ReplenishEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/replenish.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ProjectileEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ProjectileEnchantmentMechanics.java)

</details>
