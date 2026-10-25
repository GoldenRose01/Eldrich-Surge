<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Soft Falling

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Premere Salto in aria attiva Caduta Lenta; gli stivali prevengono il danno da caduta.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:soft_falling` |
| Livello massimo | 1 |
| Classe dichiarata | Unique |
| Oggetti — regola configuratore | `#minecraft:foot_armor` |
| Oggetti — datapack | `#minecraft:foot_armor` |
| Slot dichiarati | `feet` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:falling` |

## Effetto e utilizzo

Con gli stivali incantati, una nuova pressione di Salto in aria applica Caduta Lenta per **100 tick** (5 secondi a 20 TPS), con almeno 10 tick fra attivazioni. Non si attiva a terra, a cavallo o durante il volo creativo. Il controllo del danno annulla separatamente il danno di tipo `FALL` quando questi stivali sono equipaggiati.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Soil Falling](Incantamento-soil-falling).

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SoftFallingEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/soft_falling.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ModEnchantmentDefinition.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ModEnchantmentDefinition.java)
- [SoftFallingMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SoftFallingMechanics.java)

</details>
