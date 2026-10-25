<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Butcher

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge danno contro gli animali e raddoppia specifici drop di carne.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:butcher` |
| Livello massimo | 7 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `#minecraft:enchantable/weapon` |
| Oggetti — datapack | `#minecraft:enchantable/weapon` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 10 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

Contro la categoria `animals` aggiunge **2,5 danni per livello**. La meccanica dei drop duplica manzo, maiale, pollo, montone e coniglio crudi quando il killer impugna Butcher in una delle mani. Il raddoppio non cresce ulteriormente con il livello e non si estende automaticamente a qualsiasi drop.

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
  "effects": {},
  "min_cost": {
    "base": 5,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 35,
    "per_level_above_first": 10
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/ButcherEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/butcher.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [MobCategoryDamageMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/MobCategoryDamageMechanics.java)
- [ProjectileEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ProjectileEnchantmentMechanics.java)
- [SpecialAnvilRecipes.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java)

</details>
