<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Blade of Apocalypse

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge danno per ciascuna categoria bersaglio corrispondente.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:blade_of_apocalypse` |
| Livello massimo | 9 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:enchantable/weapon` |
| Oggetti — datapack | `#minecraft:enchantable/weapon` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

Il bonus è **2 × livello × categorie corrispondenti**, fra `animals`, `undead`, `void`, `hell` e `water`. Un’entità con più categorie può ricevere più contributi. Quando Blade è presente, il ramo runtime dei bonus individuali di categoria viene sostituito dal suo calcolo. EnchLib può cambiare le categorie assegnate ai mob.

## Ottenimento

Fusione all’incudine dei cinque componenti con una stella del Nether; escluso dai tavoli.

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Comet Slam](Incantamento-comet-slam), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), [Star Fate](Incantamento-star-fate), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst`.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {},
  "min_cost": {
    "base": 30,
    "per_level_above_first": 12
  },
  "max_cost": {
    "base": 80,
    "per_level_above_first": 16
  },
  "anvil_cost": 8
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/BladeOfApocalypseEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/blade_of_apocalypse.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [EldritchEnchantments.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/EldritchEnchantments.java)
- [AdvancedEnchantmentMenu.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java)
- [EnchantmentMenuMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)
- [SpecialAnvilRecipes.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java)

</details>
