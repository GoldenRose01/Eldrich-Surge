<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Dodge

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Può evitare completamente un evento di danno con i gambali equipaggiati.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:dodge` |
| Livello massimo | 3 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/leg_armor` |
| Oggetti — datapack | `#minecraft:enchantable/leg_armor` |
| Slot dichiarati | `legs` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Probabilità: **12,5% / 25% / 33%** ai livelli I/II/III. Il tiro avviene sui colpi che superano i controlli vanilla di immunità e intervallo fra danni. Una schivata annulla il danno, conserva l’intervallo normale e mostra **Schivato!** sopra la barra rapida. I contatti ripetuti con una fonte di danno non consumano subito la schivata con un nuovo tentativo al tick successivo.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/DodgeEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/dodge.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
