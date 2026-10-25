<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Mida's Touch

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Le uccisioni possono generare pepite o un lingotto d’oro.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:midas_touch` |
| Livello massimo | 5 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:swords`, `#minecraft:hoes`, `minecraft:netherite_hoe` |
| Oggetti — datapack | `#eldrich-surge:enchantable/midas_touch` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La probabilità del drop è `min(100%, 20% × livello)`. Superato il tiro, il premio è un lingotto con probabilità 15%; altrimenti sono pepite in quantità pari al livello. La meccanica rispetta l’abilitazione dei drop dei mob.

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
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/MidasTouchEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/midas_touch.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [MidasTouchMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/MidasTouchMechanics.java)

</details>
