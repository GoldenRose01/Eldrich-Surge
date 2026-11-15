<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Fisherman

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Garantisce che un pesce abbocchi sempre entro 5 secondi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:fisherman` |
| Livello massimo | 1 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:enchantable/fishing` |
| Oggetti — datapack | `#minecraft:enchantable/fishing` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Incantesimo leggendario (nome oro) per canna da pesca che limita i timer di attesa dell’amo in acqua (`timeUntilLured` e `timeUntilHooked`) garantendo l’abboccata entro 100 tick (5 secondi).

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
    "base": 35,
    "per_level_above_first": 10
  },
  "max_cost": {
    "base": 75,
    "per_level_above_first": 10
  },
  "anvil_cost": 6
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/FishermanEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/fisherman.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [FanaticFishingMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/FanaticFishingMixin.java)

</details>
