<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Leeching Aspect

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Cura l’attaccante dopo un colpo critico.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:leeching_aspect` |
| Livello massimo | 2 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:swords`, `#minecraft:axes` |
| Oggetti — datapack | `#eldrich-surge:enchantable/leeching_aspect` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Un colpo considerato critico dal controllo della mod cura `max(1, danno × 0,15 × livello)` punti salute. Il codice usa una cura diretta: non assegna cuori gialli di Assorbimento.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/LeechingAspectEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/leeching_aspect.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
