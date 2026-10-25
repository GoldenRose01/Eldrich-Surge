<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_pickaxe.png" width="48" alt="Scavo e strumenti">

# Pruning

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Distrugge istantaneamente (Instamine) blocchi di foglie, erba e altri blocchi delicati gestiti dalle cesoie.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:pruning` |
| Livello massimo | 1 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `minecraft:shears` |
| Oggetti — datapack | `minecraft:shears` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 10 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Distrugge istantaneamente (Instamine) blocchi di foglie, erba e altri blocchi delicati gestiti dalle cesoie.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

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
  "effects": {},
  "min_cost": {
    "base": 5,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 35,
    "per_level_above_first": 10
  },
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/PruningEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/pruning.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PruningPlayerMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/PruningPlayerMixin.java)

</details>
