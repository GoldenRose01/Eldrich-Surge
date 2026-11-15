<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Neptune's Will

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge 5 danni per livello al tridente in mischia e al lancio.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:neptunes_will` |
| Livello massimo | 5 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#minecraft:enchantable/trident` |
| Oggetti — datapack | `#minecraft:enchantable/trident` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 2 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:additional_damage` |

## Effetto e utilizzo

Ai livelli I/II/III aggiunge **5 / 10 / 15 punti danno**. Il tridente lanciato usa il livello salvato sul proiettile: cambiare arma dopo il lancio non lo altera e un secondo tridente impugnato non raddoppia il bonus del proiettile. In mischia resta valido il bonus alle altre armi quando un tridente incantato è nella mano secondaria.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: [Blade of Apocalypse](Incantamento-blade-of-apocalypse), [Comet Slam](Incantamento-comet-slam), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), [Star Fate](Incantamento-star-fate), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst`.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "effects": {
    "minecraft:attributes": [
      {
        "amount": {
          "type": "eldrich-surge:logarithmic_level",
          "base": 2.5,
          "per_level_above_first": 2.5,
          "max_level": 5
        },
        "attribute": "minecraft:attack_damage",
        "id": "eldrich-surge:neptunes_will",
        "operation": "add_value"
      }
    ]
  },
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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/NeptunesWillEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/neptunes_will.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [CreativeEnchantmentClassifier.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/creative/CreativeEnchantmentClassifier.java)
- [ThrownTridentMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ThrownTridentMixin.java)

</details>
