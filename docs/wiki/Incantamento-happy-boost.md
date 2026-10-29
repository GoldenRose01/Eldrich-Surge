<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Happy Boost

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aumenta la velocità di volo con le elitre del 50%, 80% o 110%.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:happy_boost` |
| Livello massimo | 3 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `minecraft:elytra` |
| Oggetti — datapack | `minecraft:elytra` |
| Slot dichiarati | `chest` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Lo spostamento in planata viene moltiplicato per **1,5 / 1,8 / 2,1**. Il fattore si applica sia sul client sia sul server, senza moltiplicare ripetutamente la velocità memorizzata: non genera accelerazione esponenziale. L’effetto riguarda il volo con elitre.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HappyBoostEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/happy_boost.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [FlightEnchantmentMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/FlightEnchantmentMixin.java)

</details>
