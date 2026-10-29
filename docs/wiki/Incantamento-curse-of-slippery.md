<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Curse of Slippery

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Fa cadere casualmente l’oggetto impugnato, in media ogni 33 secondi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_slippery` |
| Livello massimo | 1 |
| Classe dichiarata | Common |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/slippery` |
| Oggetti — datapack | `#eldrich-surge:enchantable/slippery` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni oggetto maledetto nelle mani ha probabilità **1/660 per tick** di cadere: 33 secondi in media a 20 TPS, non un timer fisso. L’intero stack conserva componenti, durabilità e incantesimi. Supporta spade, asce, picconi, pale, zappe, lance, tridenti, archi, balestre, scudi e canne da pesca.

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
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfSlipperyEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_slippery.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
