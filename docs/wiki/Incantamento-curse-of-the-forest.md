<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Durabilità">

# Curse of the Forest

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Alla morte del giocatore, l'oggetto genera un'esplosione controllata (Lvl 1 = Esplosione Creeper, Lvl 2 = Creeper Caricato).

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_the_forest` |
| Livello massimo | 2 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#minecraft:enchantable/durability` |
| Oggetti — datapack | `#minecraft:enchantable/durability` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:negative_curses` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Alla morte del giocatore, l'oggetto genera un'esplosione controllata (Lvl 1 = Esplosione Creeper, Lvl 2 = Creeper Caricato).

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Curse of Fragility](Incantamento-curse-of-fragility).

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfTheForestEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_the_forest.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SoulbondPlayerMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/SoulbondPlayerMixin.java)

</details>
