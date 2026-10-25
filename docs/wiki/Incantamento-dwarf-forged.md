<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Dwarf Forged

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Può evitare il consumo di durabilità nei biomi sotterranei riconosciuti.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:dwarf_forged` |
| Livello massimo | 5 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor` |
| Oggetti — datapack | `#minecraft:enchantable/armor` |
| Slot dichiarati | `armor` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:forged` |

## Effetto e utilizzo

Nei biomi con percorso `lush_caves`, `dripstone_caves` o `deep_dark`, se è disponibile il giocatore nel controllo, una probabilità fissa di **1 su 3** azzera il consumo gestito. Il controllo verifica la presenza dell’incantamento; non moltiplica la probabilità per il livello.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [End Forged](Incantamento-end-forged), [Nether Forged](Incantamento-nether-forged), [Villager Forged](Incantamento-villager-forged).

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
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/DwarfForgedEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/dwarf_forged.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ForgedUnbreakingMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ForgedUnbreakingMixin.java)

</details>
