<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Blessing of the Sea

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Cura il giocatore e ripara l’armatura incantata quando sei bagnato.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:blessing_of_the_sea` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor` |
| Oggetti — datapack | `#minecraft:enchantable/armor` |
| Slot dichiarati | `armor` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

In acqua o sotto pioggia diretta, ogni **3 secondi** cura **0,25 punti salute (⅛ di cuore)** una sola volta e ripara **1 punto durabilità per ogni pezzo incantato**. Nessun effetto all’asciutto; la cura non cresce indossando più pezzi.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/BlessingOfTheSeaEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/blessing_of_the_sea.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
