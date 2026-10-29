<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Fuse Cutter

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Annulla le esplosioni entro tre blocchi mentre pari con lo scudo.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:fuse_cutter` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/shield` |
| Oggetti — datapack | `#eldrich-surge:enchantable/shield` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Richiede uno scudo incantato in parata effettiva. Se il centro dell’esplosione è entro **3 blocchi** dalla posizione del giocatore, viene annullata l’intera esplosione, proteggendo anche terreno e altre entità. Scudo abbassato, ritardo iniziale della parata o distanza superiore non attivano l’effetto.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/FuseCutterEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/fuse_cutter.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
