<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Soil Falling

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Impedisce la distruzione dei blocchi di terreno zappato (farmland/crops) quando ci si salta sopra.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:soil_falling` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:foot_armor` |
| Oggetti — datapack | `#minecraft:foot_armor` |
| Slot dichiarati | `feet` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:falling` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Impedisce la distruzione dei blocchi di terreno zappato (farmland/crops) quando ci si salta sopra.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Soft Falling](Incantamento-soft-falling).

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SoilFallingEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/soil_falling.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SoilFallingMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/SoilFallingMixin.java)

</details>
