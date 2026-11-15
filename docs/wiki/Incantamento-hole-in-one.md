<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Hole in One

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Probabilità di eliminare istantaneamente i mob semplici negli attacchi corpo a corpo con la lancia.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:hole_in_one` |
| Livello massimo | 3 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/spear` |
| Oggetti — datapack | `#eldrich-surge:enchantable/spear` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Concede **1% / 3% / 5%** di probabilità ai livelli I/II/III (poi progressione logaritmica verso il **15%**) di infliggere danno letale immediato a mob non boss negli attacchi melee con la lancia.

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
    "base": 25,
    "per_level_above_first": 10
  },
  "max_cost": {
    "base": 65,
    "per_level_above_first": 12
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HoleInOneEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/hole_in_one.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [LivingEntityDamageMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/LivingEntityDamageMixin.java)

</details>
