<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/diamond_sword.png" width="48" alt="Combattimento">

# Heartseeker

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Potenzia i colpi alle spalle e dimezza l’armatura nel calcolo intercettato.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:heartseeker` |
| Livello massimo | 1 |
| Classe dichiarata | Legendary |
| Oggetti — regola configuratore | `#eldrich-surge:enchantable/spear` |
| Oggetti — datapack | `#eldrich-surge:enchantable/spear` |
| Slot dichiarati | `hand` |
| Peso nel datapack | 1 |
| Gruppo esclusivo dichiarato | `#eldrich-surge:aspect` |

## Effetto e utilizzo

Il prodotto scalare fra la direzione dello sguardo della vittima e la direzione verso l’attaccante deve essere inferiore a −0,2. In quel caso il danno passato al calcolo armatura è moltiplicato per **1,5** e il valore di armatura per **0,5**. La logica usa la presenza dell’incantamento, non un moltiplicatore ulteriore per livello.

## Ottenimento

Tavolo avanzato con ametista

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

L’`exclusive_set` del datapack include: `minecraft:fire_aspect`.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/HeartseekerEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/heartseeker.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [HeartseekerMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/HeartseekerMixin.java)

</details>
