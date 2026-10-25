<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Spirit Cast

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Ogni 15 meno livello uccisioni assegna casualmente Forza, Velocità o Resistenza per 10 secondi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:spirit_cast` |
| Livello massimo | 10 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `#minecraft:enchantable/weapon`, `minecraft:netherite_hoe` |
| Oggetti — datapack | `#eldrich-surge:enchantable/spirit_cast` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 10 |
| Gruppo esclusivo dichiarato | `power_up` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Ogni 15 meno livello uccisioni assegna casualmente Forza, Velocità o Resistenza per 10 secondi.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: `power_up`.

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
  "anvil_cost": 5
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SpiritCastEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/spirit_cast.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
