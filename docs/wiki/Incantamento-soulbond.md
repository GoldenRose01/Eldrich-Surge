<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Durabilità">

# Soulbond

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Mantiene l'oggetto nell'inventario del giocatore dopo la morte anche se la gamerule keepInventory è impostata su false.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:soulbond` |
| Livello massimo | 1 |
| Classe dichiarata | Ultra |
| Oggetti — regola configuratore | `#minecraft:enchantable/durability` |
| Oggetti — datapack | `#minecraft:enchantable/durability` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

L'incantesimo trattiene l'oggetto nell'inventario del giocatore anche alla morte con `keepInventory = false`. L'oggetto incantato con Soulbond non viene rilasciato a terra come drop e, al momento del respawn del giocatore, viene ripristinato esattamente nello stesso slot di origine dell'inventario.

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SoulbondEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/soulbond.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SoulbondPlayerMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/SoulbondPlayerMixin.java)

</details>
