<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_chestplate.png" width="48" alt="Armatura e mobilità">

# God's Protection

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Applica una riduzione del 60% nel calcolatore dei danni della mod.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:god_protection` |
| Livello massimo | 1 |
| Classe dichiarata | Unique |
| Oggetti — regola configuratore | `#minecraft:enchantable/armor`, `#minecraft:enchantable/trident` |
| Oggetti — datapack | `#eldrich-surge:enchantable/god_protection` |
| Slot dichiarati | `head`, `chest`, `legs`, `feet`, `hand`, `offhand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:protection` |

## Effetto e utilizzo

Se presente su un equipaggiamento controllato, moltiplica per **0,40** il moltiplicatore del danno. Opera nel percorso intercettato dal calcolatore della mod, insieme alle altre riduzioni; non va interpretato come un controllo universale di ogni possibile fonte o sistema di danno di terze parti.

## Ottenimento

Nessun tavolo abilitato dalla definizione; verificare loot, comandi e impostazioni del mondo.

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: `minecraft:blast_protection`, `minecraft:fire_protection`, `minecraft:projectile_protection`, `minecraft:protection`.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {},
  "min_cost": {
    "base": 20,
    "per_level_above_first": 0
  },
  "max_cost": {
    "base": 50,
    "per_level_above_first": 0
  },
  "anvil_cost": 4
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/GodProtectionEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/god_protection.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)

</details>
