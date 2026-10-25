<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Healing Aura

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Cura i giocatori vicini mentre resti accovacciato.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:healing_aura` |
| Livello massimo | 3 |
| Classe dichiarata | Epic |
| Oggetti — regola configuratore | `#minecraft:leg_armor` |
| Oggetti — datapack | `#minecraft:leg_armor` |
| Slot dichiarati | `legs` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Ogni 40 tick, se il portatore è accovacciato, cura **1 + livello punti salute** ai giocatori feriti entro 6 blocchi, incluso il portatore. Il codice non filtra le squadre: «alleati» non significa che i nemici siano esclusi.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HealingAuraEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/healing_aura.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [PassiveEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/PassiveEnchantmentMechanics.java)

</details>
