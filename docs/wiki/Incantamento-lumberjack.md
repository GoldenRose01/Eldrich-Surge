<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Lumberjack

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Abbatti l'intero albero in un solo colpo mentre sei abbassato con un'ascia idonea.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:lumberjack` |
| Livello massimo | 1 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/lumberjack` |
| Oggetti — datapack | `#eldrich-surge:enchantable/lumberjack` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 3 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Abbatti l'intero albero in un solo colpo mentre sei abbassato con un'ascia idonea.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo normale / Tavolo avanzato con ametista

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
    "base": 15,
    "per_level_above_first": 10
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 10
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/LumberjackEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/lumberjack.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [EldritchEnchantments.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/EldritchEnchantments.java)

</details>
