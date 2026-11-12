<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Neptune's Will

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Aggiunge +2.5 danni per livello al tridente in mischia (come attributo nativo d'attacco) e al lancio, sostituendo Impalamento (Impaling).

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

Sostituisce completamente l'incantesimo vanilla **Impalamento** (*Impaling*), estendendo il bonus di danno a qualsiasi bersaglio (non solo acquatico).
Ai livelli I/II/III/IV/V conferisce **+2.5 / +5.0 / +7.5 / +10.0 / +12.5 punti danno d'attacco**. Il bonus opera in mischia tramite l'attributo nativo dell'arma (come Katana) e viene trasferito al proiettile quando il tridente viene lanciato.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

Appartiene al gruppo esclusivo `#eldrich-surge:additional_damage` ed è incompatibile con Impaling, Katana, Gream Reaper e gli altri incantamenti di danno addizionale.

Consulta [Compatibilità](Compatibilita) per la distinzione fra gruppi dichiarati, componenti di fusione e regole personalizzate.

## Dati tecnici

<details>
<summary>Mostra effetti e costi dichiarati nel datapack</summary>

```json
{
  "anvil_cost": 5,
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
  "exclusive_set": "#eldrich-surge:additional_damage",
  "max_cost": {
    "base": 50,
    "per_level_above_first": 10
  },
  "max_level": 5,
  "min_cost": {
    "base": 20,
    "per_level_above_first": 7
  },
  "slots": [
    "hand"
  ],
  "supported_items": "#minecraft:enchantable/trident",
  "weight": 2
}
```

</details>

## Fonti del progetto

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/NeptunesWillEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/neptunes_will.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [AdditionalDamageCalculator.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/combat/AdditionalDamageCalculator.java)
- [CreativeEnchantmentClassifier.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/creative/CreativeEnchantmentClassifier.java)
- [ThrownTridentMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/ThrownTridentMixin.java)

</details>
