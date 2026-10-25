<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Seismic Wave

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Gli attacchi in caduta (Smash) generano un'onda d'urto che infligge il 20% * livello del danno totale ai mob nel raggio di 4 blocchi.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:seismic_wave` |
| Livello massimo | 3 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#minecraft:enchantable/mace` |
| Oggetti — datapack | `#minecraft:enchantable/mace` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:weapon_utility` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Gli attacchi in caduta (Smash) generano un'onda d'urto che infligge il 20% * livello del danno totale ai mob nel raggio di 4 blocchi.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Gravity Well](Incantamento-gravity-well), [Impaler Reach](Incantamento-impaler-reach), [Vanguard Charge](Incantamento-vanguard-charge), `minecraft:sweeping_edge`.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SeismicWaveEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/seismic_wave.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
