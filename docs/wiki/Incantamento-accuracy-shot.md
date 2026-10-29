<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bow.png" width="48" alt="Tiro e proiettili">

# Accuracy Shot

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Le frecce seguono la mira senza gravità e scompaiono dopo 30 secondi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:accuracy_shot` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/bow` |
| Oggetti — datapack | `#minecraft:enchantable/bow` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Rimuove la dispersione iniziale e la gravità. Non insegue automaticamente i bersagli: la direzione resta quella della mira. La freccia legge l’arco salvato al lancio, quindi cambiare arma non modifica l’effetto. La scadenza di **600 tick** è salvata anche per le frecce conficcate e non riparte ricaricando il mondo.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/AccuracyShotEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/accuracy_shot.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AccuracyShotArrowMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/AccuracyShotArrowMixin.java)

</details>
