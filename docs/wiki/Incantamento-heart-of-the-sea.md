<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Heart of the Sea

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Fornisce gli effetti di stato Conduit Power e Dolphin's Grace permanenti al giocatore.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:heart_of_the_sea` |
| Livello massimo | 1 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:chest_armor` |
| Oggetti — datapack | `#minecraft:chest_armor` |
| Slot dichiarati | `chest` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:hearts` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Fornisce gli effetti di stato Conduit Power e Dolphin's Grace permanenti al giocatore.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Heart of Depth](Incantamento-heart-of-depth), [Heart of Nether](Incantamento-heart-of-nether), [Heart of the Sky](Incantamento-heart-of-the-sky).

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HeartOfTheSeaEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/heart_of_the_sea.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
