<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Vanguard Charge

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Doppia pressione dello scatto esegue un affondo aereo che trascina i mob colpiti fino al primo blocco solido, infliggendo danno da impatto.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:vanguard_charge` |
| Livello massimo | 2 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/spear` |
| Oggetti — datapack | `#eldrich-surge:enchantable/spear` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:weapon_utility` |

## Effetto e utilizzo

La definizione del progetto descrive questo effetto: Doppia pressione dello scatto esegue un affondo aereo che trascina i mob colpiti fino al primo blocco solido, infliggendo danno da impatto.

Per i componenti gestiti dai dati, gli effetti dichiarati sono riportati nel riquadro tecnico sotto. La presenza nel catalogo non sostituisce una prova in gioco.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Gravity Well](Incantamento-gravity-well), [Impaler Reach](Incantamento-impaler-reach), [Seismic Wave](Incantamento-seismic-wave), `minecraft:sweeping_edge`.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/VanguardChargeEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/vanguard_charge.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [VanguardChargeMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/VanguardChargeMixin.java)

</details>
