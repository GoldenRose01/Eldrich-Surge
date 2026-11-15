<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Magnetic

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Attira gli oggetti a terra entro 8 blocchi verso il centro dell’Armor Stand.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:magnetic` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `minecraft:armor_stand` |
| Oggetti — datapack | `minecraft:armor_stand` |
| Slot dichiarati | `any` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Applicabile agli Armor Stand; attrae gli `ItemEntity` nel raggio di 8 blocchi verso il centro dell’entità e conserva l’incantesimo quando l’Armor Stand viene rotto e riposizionato.

## Ottenimento

Tavolo normale

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
    "base": 10,
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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/MagneticEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/magnetic.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ArmorStandItemMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ArmorStandItemMixin.java)
- [ArmorStandMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ArmorStandMixin.java)

</details>
