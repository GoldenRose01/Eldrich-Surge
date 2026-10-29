<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Star Fate

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Combina i bersagli magici, artropodi, ribelli, fungini e cubici.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:star_fate` |
| Livello massimo | 9 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:enchantable/weapon` |
| Oggetti — datapack | `#minecraft:enchantable/weapon` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

Ogni categoria corrispondente aggiunge **2,5 × livello**, eccetto `arthropods`, che aggiunge **2,75 × livello**. Sono considerate `magik`, `arthropods`, `rebel`, `fungi` e `cubic`; i contributi si sommano per mob con più categorie. L’ottenimento richiede la fusione dei componenti all’incudine.

## Ottenimento

Fusione all’incudine dei cinque componenti con una stella del Nether; escluso dai tavoli.

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Blade of Apocalypse](Incantamento-blade-of-apocalypse), [Comet Slam](Incantamento-comet-slam), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst`.

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
  "anvil_cost": 5
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/StarFateEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/star_fate.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdvancedEnchantmentMenu.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java)
- [EnchantmentMenuMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)
- [MobCategoryDamageMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/MobCategoryDamageMechanics.java)
- [SpecialAnvilRecipes.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java)

</details>
