<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Durabilità">

# Curse of Incompatibility

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Impedisce la combinazione con qualsiasi altro incantesimo.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_incompatibility` |
| Livello massimo | 1 |
| Classe dichiarata | Common |
| Oggetti — regola configuratore | `#minecraft:enchantable/durability` |
| Oggetti — datapack | `#minecraft:enchantable/durability` |
| Slot dichiarati | `any` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

La regola è simmetrica nel controllo standard di compatibilità: blocca sia l’aggiunta di altri incantesimi sia l’applicazione della maledizione a un oggetto già incantato. Si applica anche al tavolo avanzato. Non cancella incantesimi forzati direttamente con comandi.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

Incompatibile con **qualsiasi altro incantesimo**, tramite regola runtime simmetrica.

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
  "anvil_cost": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfIncompatibilityEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_incompatibility.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [CurseCompatibilityMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/CurseCompatibilityMixin.java)

</details>
