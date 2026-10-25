<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Comet Slam

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Se lo Smash Attack viene eseguito da piu di 12 blocchi d'altezza, genera un'esplosione magica che incendia l'area e applica Knockback X.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:comet_slam` |
| Livello massimo | 1 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:enchantable/mace` |
| Oggetti — datapack | `#minecraft:enchantable/mace` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Se lo Smash Attack viene eseguito da piu di 12 blocchi d'altezza, genera un'esplosione magica che incendia l'area e applica Knockback X.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Blade of Apocalypse](Incantamento-blade-of-apocalypse), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), [Star Fate](Incantamento-star-fate), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst`.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CometSlamEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/comet_slam.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
