<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Undead Slayer

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge 2,5 danni per livello contro la categoria undead.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:undead_slayer` |
| Livello massimo | 7 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `#minecraft:enchantable/weapon` |
| Oggetti — datapack | `#minecraft:enchantable/weapon` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 3 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

Il bonus runtime è **2,5 × livello** contro i mob assegnati a `undead`. Le categorie provengono da EnchLib, se disponibile, altrimenti dai tag di Eldrich Surge. La sostituzione delle offerte di Smite non significa che il danno sia raddoppiato rispetto a Smite.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Blade of Apocalypse](Incantamento-blade-of-apocalypse), [Comet Slam](Incantamento-comet-slam), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), [Star Fate](Incantamento-star-fate), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst`.

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
          "type": "eldrich-surge:gamerule_damage",
          "amount": {
            "type": "minecraft:linear",
            "base": 4.0,
            "per_level_above_first": 4.0
          },
          "entity_type_tag": "eldrich-surge:undead"
        },
        "enchanted": "attacker"
      }
    ]
  },
  "min_cost": {
    "base": 14,
    "per_level_above_first": 9
  },
  "max_cost": {
    "base": 55,
    "per_level_above_first": 12
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/UndeadSlayerEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/undead_slayer.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [EldritchEnchantments.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/EldritchEnchantments.java)
- [EnchantmentMenuMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)
- [MobCategoryDamageMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/MobCategoryDamageMechanics.java)
- [SpecialAnvilRecipes.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java)

</details>
