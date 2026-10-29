<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Curse of Corrosion

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

L’armatura maledetta infligge danno quando sei bagnato.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_corrosion` |
| Livello massimo | 1 |
| Classe dichiarata | Common |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor` |
| Oggetti — datapack | `#minecraft:enchantable/armor` |
| Slot dichiarati | `armor` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

In acqua o sotto pioggia diretta infligge **0,25 punti salute (⅛ di cuore) ogni 3 secondi**. Più pezzi non moltiplicano il danno. Sono danni magici soggetti alle normali difese; un tetto ferma l’effetto della pioggia, ma non quello del contatto con acqua.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfCorrosionEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_corrosion.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
