<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_pickaxe.png" width="48" alt="Scavo e strumenti">

# Excavator

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aumenta la portata di interazione con blocchi ed entità.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:excavator` |
| Livello massimo | 3 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:axes`, `#minecraft:pickaxes`, `#minecraft:shovels` |
| Oggetti — datapack | `#eldrich-surge:enchantable/excavator` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni livello aggiunge **1 blocco** a `block_interaction_range` e `entity_interaction_range`. I livelli degli strumenti nelle due mani vengono sommati. Non modifica l’area di scavo: quella è la funzione di Digger.

## Ottenimento

Tavolo avanzato con ametista

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
    "base": 20,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 10
  },
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/ExcavatorEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/excavator.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
