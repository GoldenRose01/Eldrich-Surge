<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Fear

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

I colpi di spada prolungano il timer di esplosione del creeper.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:fear` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:swords` |
| Oggetti — datapack | `#minecraft:swords` |
| Slot dichiarati | `mainhand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni colpo diretto riuscito del giocatore aggiunge **20 tick (1 secondo)** alla durata della miccia del creeper ancora vivo. I colpi si sommano; frecce e attacchi respinti non contribuiscono. Il timer è salvato dal creeper, con limite a 32.767 tick per rispettare il formato vanilla.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/FearEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/fear.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalLivingEntityMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/SurvivalLivingEntityMixin.java)

</details>
