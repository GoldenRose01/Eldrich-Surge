<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Hell Flakes

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Quando il giocatore va a fuoco o subisce calore, rigenera livello punti di durabilità al secondo.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:hell_flakes` |
| Livello massimo | 3 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor` |
| Oggetti — datapack | `#minecraft:enchantable/armor` |
| Slot dichiarati | `armor` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:flakes` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Quando il giocatore va a fuoco o subisce calore, rigenera livello punti di durabilità al secondo.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Dwarf Flakes](Incantamento-dwarf-flakes), [End Flakes](Incantamento-end-flakes), [Sea Flakes](Incantamento-sea-flakes).

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
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HellFlakesEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/hell_flakes.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ArmorRepairMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ArmorRepairMechanics.java)

</details>
