<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/bow.png" width="48" alt="Tiro e proiettili">

# Sniper

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge danno in funzione della distanza dal tiratore.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:sniper` |
| Livello massimo | 1 |
| Classe dichiarata | Uncommon |
| Oggetti — regola configuratore | `#minecraft:enchantable/bow`, `#minecraft:enchantable/crossbow` |
| Oggetti — datapack | `#eldrich-surge:enchantable/sniper` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 10 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Se Sniper è presente, il bonus è `min(distanza attuale fra proprietario e freccia × 0,4, 25)`. Il livello viene usato come verifica di presenza, senza un ulteriore moltiplicatore nel metodo. È distanza dal tiratore, non la lunghezza totale della traiettoria.

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
  "anvil_cost": 1
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/SniperEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/sniper.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [ProjectileEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/ProjectileEnchantmentMechanics.java)

</details>
