<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# Weapon Protection

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Riduce il danno da un attaccante che impugna un oggetto.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:weapon_protection` |
| Livello massimo | 5 |
| Classe dichiarata | Rare |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor` |
| Oggetti — datapack | `#minecraft:enchantable/armor` |
| Slot dichiarati | `armor` |
| Peso nel datapack | 4 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Il ramo corrente controlla che l’attaccante sia una creatura vivente con la mano principale non vuota, senza limitarsi a spade, asce o tridenti. Sottrae al moltiplicatore `min(0,5, 0,07 × somma dei livelli sull’armatura)`. Il risultato si combina con le altre riduzioni del calcolatore.

## Ottenimento

Tavolo normale

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
    "base": 5,
    "per_level_above_first": 7
  },
  "max_cost": {
    "base": 35,
    "per_level_above_first": 10
  },
  "anvil_cost": 3
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/WeaponProtectionEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/weapon_protection.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
