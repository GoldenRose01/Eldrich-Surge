<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Dual Sweeping

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Raddoppia il danno del fendente quando nell’altra mano c’è una spada.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:dual_sweeping` |
| Livello massimo | 2 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:swords` |
| Oggetti — datapack | `#minecraft:swords` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Richiede Dual Sweeping sull’arma principale e un oggetto del tag `SWORDS` nella mano secondaria. Il danno passato al fendente viene moltiplicato per **2**. Il metodo verifica la presenza dell’incantamento, senza aumentare ulteriormente il fattore al livello II.

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/DualSweepingEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/dual_sweeping.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SweepAttackMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/SweepAttackMixin.java)

</details>
