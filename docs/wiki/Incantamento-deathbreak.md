<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Durabilità">

# DeathBreak

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Blocca il consumo di durabilità e sostituisce Unbreaking.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:deathbreak` |
| Livello massimo | 1 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#minecraft:enchantable/durability` |
| Oggetti — datapack | `#minecraft:enchantable/durability` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Il controllo della durabilità restituisce zero danni all’oggetto. Applicato tramite l’incudine, imposta anche il componente `UNBREAKABLE` e rimuove Unbreaking. Il libro si ottiene combinando **due libri contenenti soltanto Unbreaking III**. Non viene offerto dai tavoli.

## Ottenimento

Due libri con il solo Unbreaking III combinati all’incudine producono DeathBreak I; escluso dai tavoli.

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/DeathbreakEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/deathbreak.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdvancedEnchantmentMenu.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java)
- [AnvilEnchantmentsMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/AnvilEnchantmentsMixin.java)
- [EnchantmentMenuMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)
- [ForgedUnbreakingMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ForgedUnbreakingMixin.java)

</details>
