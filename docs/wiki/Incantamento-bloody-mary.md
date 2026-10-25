<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Bloody Mary

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Può far ottenere la testa della vittima di un’uccisione in mischia.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:bloody_mary` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:swords`, `#minecraft:axes`, `#minecraft:hoes` |
| Oggetti — datapack | `#eldrich-surge:enchantable/bloody_mary` |
| Slot dichiarati | `mainhand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Richiede un colpo diretto di un giocatore, un’arma valida con Bloody Mary e i drop dei mob abilitati. Probabilità: `min(100%, 10% + 10% × Looting)`. Le teste native vengono usate dove disponibili; le altre teste vanilla dipendono dalle tabelle dedicate. Per un giocatore viene conservato il suo profilo; i mob di altre mod non ricevono automaticamente una testa.

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
    "per_level_above_first": 0
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 0
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/BloodyMaryEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/bloody_mary.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [BloodyMaryMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/BloodyMaryMechanics.java)
- [EnchantmentBookTags.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/EnchantmentBookTags.java)

</details>
