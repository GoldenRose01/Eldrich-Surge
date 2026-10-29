<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Step Assist

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Sale automaticamente un blocco; gli ostacoli superiori richiedono un salto.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:step_assist` |
| Livello massimo | 3 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/foot_armor` |
| Oggetti — datapack | `#minecraft:enchantable/foot_armor` |
| Slot dichiarati | `feet` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

A tutti i livelli, l’altezza del passo automatico è **1 blocco**. Per superare dislivelli maggiori devi premere Salto. Il salto normale permette circa 1,25 blocchi; al livello III si aggiunge **0,05** alla forza del salto, permettendo di superare un blocco con una lastra sopra (**1,5 blocchi**). Rimuovendo gli stivali scompaiono entrambi i bonus; eventuali modificatori di altre mod possono sommarsi.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/StepAssistEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/step_assist.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
