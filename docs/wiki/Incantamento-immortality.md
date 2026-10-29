<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Immortality

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Lo scudo impugnato salva dalla morte consumando soltanto questo incantesimo.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:immortality` |
| Livello massimo | 1 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/shield` |
| Oggetti — datapack | `#eldrich-surge:enchantable/shield` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Un danno mortale normale viene fermato lasciando **1 cuore**; il fuoco viene spento e Immortality rimosso dallo scudo. Lo scudo e gli altri incantesimi restano. Funziona in entrambe le mani e non richiede parata. Danni che bypassano l’invulnerabilità, come `/kill`, mantengono l’eccezione vanilla.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/ImmortalityEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/immortality.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
