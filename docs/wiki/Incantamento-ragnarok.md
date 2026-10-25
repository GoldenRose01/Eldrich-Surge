<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Rituali">

# Ragnarok

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Evoca sul posto 4 Wither Skeleton dotati di scheletro equino come cavalcatura e armatura completa in Netherite.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:ragnarok` |
| Livello massimo | 1 |
| Classe dichiarata | Ritual |
| Oggetti — regola configuratore | `minecraft:book` |
| Oggetti — datapack | `minecraft:book` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:rituals` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Evoca sul posto 4 Wither Skeleton dotati di scheletro equino come cavalcatura e armatura completa in Netherite.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Libro normale + frammento di eco nel tavolo avanzato, con formazione valida per gli slot superiori.

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Cocktail Spell](Incantamento-cocktail-spell), [Red Moon](Incantamento-red-moon), [Storm Spell](Incantamento-storm-spell), [Trench Spell](Incantamento-trench-spell).

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/RagnarokEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/ragnarok.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SpellCastMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpellCastMechanics.java)

</details>
