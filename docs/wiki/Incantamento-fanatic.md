<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Fanatic

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aumenta progressivamente la probabilità di pescare tesori sulla singola canna.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:fanatic` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/fishing` |
| Oggetti — datapack | `#minecraft:enchantable/fishing` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni cattura completata aggiunge **1 punto percentuale** alla probabilità di tesoro. Il contatore è salvato sulla canna, visibile nella descrizione e limitato a 100; non cresce con recuperi a vuoto o entità agganciate. Due canne conservano punteggi indipendenti. Il bonus tiene conto dei pesi vanilla e di Fortuna del mare; a 100 garantisce tesori anche senza acqua aperta. Datapack che cambiano i pesi possono modificare la progressione intermedia.

## Ottenimento

Tavolo normale / Tavolo avanzato con ametista

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/FanaticEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/fanatic.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [FanaticFishingMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/FanaticFishingMixin.java)

</details>
