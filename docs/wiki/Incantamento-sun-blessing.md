<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Sun Blessing

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Genera esperienza di giorno nell’Overworld con cielo visibile.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:sun_blessing` |
| Livello massimo | 5 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:head_armor` |
| Oggetti — datapack | `#minecraft:head_armor` |
| Slot dichiarati | `head` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:blessings` |

## Effetto e utilizzo

Durante la fascia precedente al tick 12.000, nell’Overworld e con il cielo visibile, ogni 1.200 tick assegna **10 × livello punti XP**.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Blessing of the Night](Incantamento-blessing-of-the-night), [End Blessing](Incantamento-end-blessing), [Hell Blessing](Incantamento-hell-blessing), [Seeker Blessing](Incantamento-seeker-blessing), [Vision Blessing](Incantamento-vision-blessing).

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
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SunBlessingEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/sun_blessing.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
