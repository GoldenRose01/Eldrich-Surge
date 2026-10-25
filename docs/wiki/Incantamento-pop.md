<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bow.png" width="48" alt="Tiro e proiettili">

# Pop

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Le frecce producono un’esplosione all’impatto.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:pop` |
| Livello massimo | 5 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#minecraft:enchantable/bow`, `#minecraft:enchantable/crossbow` |
| Oggetti — datapack | `#eldrich-surge:enchantable/pop` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La potenza passata all’esplosione è **1,5 + 0,5 × livello**. L’interazione con i blocchi è `NONE`: questa esplosione non è configurata per distruggere il terreno.

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
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/PopEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/pop.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ProjectileEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ProjectileEnchantmentMechanics.java)

</details>
