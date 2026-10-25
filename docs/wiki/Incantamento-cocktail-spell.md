<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Rituali">

# Cocktail Spell

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Condivide gli effetti di stato del lanciatore con i giocatori vicini.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:cocktail_spell` |
| Livello massimo | 1 |
| Classe dichiarata | Ritual |
| Oggetti — regola configuratore | `minecraft:book` |
| Oggetti — datapack | `minecraft:book` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:rituals` |

## Effetto e utilizzo

Quando il libro lanciato raggiunge terra, copia gli effetti attivi del suo proprietario sui giocatori entro `8 + 8 × livello` blocchi. Conserva gli effetti copiati: non imposta automaticamente intensità X. Il libro viene consumato dopo un lancio riuscito.

## Ottenimento

Libro normale + frammento di eco nel tavolo avanzato, con formazione valida per gli slot superiori.

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Ragnarok](Incantamento-ragnarok), [Red Moon](Incantamento-red-moon), [Storm Spell](Incantamento-storm-spell), [Trench Spell](Incantamento-trench-spell).

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CocktailSpellEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/cocktail_spell.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [SpellCastMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpellCastMechanics.java)

</details>
