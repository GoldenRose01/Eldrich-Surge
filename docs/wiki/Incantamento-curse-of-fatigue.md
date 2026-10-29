<img src="https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/enchanted_book.png" width="48" alt="Durabilità">

# Curse of Fatigue

[← Catalogo](Enchantments) · [Oggetti](Per-oggetto) · [Regole](Compatibilita)

Rallenta azioni e movimento, con contributi cumulativi sull’armatura.

| Proprietà | Valore predefinito |
| --- | --- |
| Identificatore | `eldrich-surge:curse_of_fatigue` |
| Livello massimo | 3 |
| Classe dichiarata | Common |
| Oggetti — regola configuratore | `#minecraft:enchantable/durability` |
| Oggetti — datapack | `#minecraft:enchantable/durability` |
| Slot dichiarati | `any` |
| Peso nel datapack | 5 |
| Gruppo esclusivo dichiarato | — |

## Effetto e utilizzo

Su strumenti e armi la velocità di scavo, recupero degli attacchi, carica e animazione viene moltiplicata per **0,8 per livello**: riduzioni del 20%, 36% e 48,8%. Ogni livello sui quattro pezzi di armatura moltiplica il movimento per **0,95**; dodici livelli totali riducono la velocità di circa **46%**. Rimuovere i pezzi rimuove il contributo. Incompatibile con Efficiency.

## Ottenimento

Tavolo normale

Le impostazioni di disponibilità, rarità e compatibilità del mondo possono restringere le offerte. La voce «Loot» nei metadati non identifica da sola una struttura o una probabilità di generazione: vedi [Ottenimento](Ottenimento).

## Compatibilità

Incompatibile con **Efficiency**, tramite regola runtime simmetrica.

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

- [Definizione Java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/ench/CurseOfFatigueEnchantment.java)
- [Definizione datapack](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/enchantment/curse_of_fatigue.json)

<details>
<summary>Meccaniche che consultano questo ID</summary>

- [CurseCompatibilityMixin.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/CurseCompatibilityMixin.java)
- [SurvivalEnchantmentMechanics.java](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SurvivalEnchantmentMechanics.java)

</details>
