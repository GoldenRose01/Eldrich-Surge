# 🔮 Tavolo degli incantamenti avanzato

[Home](Home) · [Ottenimento](Ottenimento) · [Rituali](Rituali)

![Ricetta e formazione](https://raw.githubusercontent.com/wiki/GoldenRose01/Eldrich-Surge/assets/formation.svg)

## Ricetta

Disponi quattro **frammenti di ametista** a croce attorno a un **tavolo degli incantamenti**:

| | | |
|---|---|---|
| — | Ametista | — |
| Ametista | Tavolo normale | Ametista |
| — | Ametista | — |

Risultato: `eldrich-surge:advanced_enchanting_table`.

## Formazione 5×5

Tutti i blocchi sono **alla stessa altezza del tavolo**. Vista dall’alto:

```text
G · Q · G
· A A A ·
Q A T A Q
· A A A ·
G · Q · G
```

| Simbolo | Blocco | Quantità |
|---|---|---:|
| T | Tavolo avanzato | 1 |
| Q | Quarzo cesellato (`minecraft:chiseled_quartz_block`) | 4 |
| G | Blocco d’oro | 4 |
| A | Aria obbligatoria | 8 |
| · | Posizione non vincolata | 8 |

Le Q sono a due blocchi di distanza sui punti cardinali; le G occupano gli angoli esterni. Le otto posizioni immediatamente adiacenti al tavolo devono restare vuote.

## Slot e reagenti

| Slot | Formazione | Livello dell’offerta |
|---|---|---|
| Primo | Non richiesta dal controllo speciale | Calcolo vanilla senza contributo delle librerie |
| Secondo | Richiesta | 15 |
| Terzo | Richiesta | 30 |

Il numero dell’offerta è il requisito di livello; non equivale a un consumo fisso di 15 o 30 livelli per operazione.

**Ametista** seleziona gli incantamenti ordinari avanzati. **Frammenti di eco** selezionano i rituali su un libro normale; in questa modalità gli incantamenti non rituali vengono esclusi. Lapislazzuli non è il reagente del tavolo avanzato.

Fonti: [formazione](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/AdvancedEnchantingFormation.java), [menu](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java), [ricetta](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/recipe/advanced_enchanting_table.json).
