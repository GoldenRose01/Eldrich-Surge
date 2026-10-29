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

## Interfaccia e reagenti

Inserisci l'oggetto nello slot superiore a sinistra e il reagente in quello inferiore. Cerca un incantesimo nell'elenco, selezionalo, scegli il livello con **− / +**, poi premi **Incanta**. Le frecce e la rotella scorrono l'elenco. Il tooltip mostra il nome completo e il livello; quello del pulsante Incanta mostra il costo completo.

**Ametista** seleziona gli incantamenti ordinari avanzati. **Frammenti dell'eco** selezionano i rituali su un libro normale. Lapislazzuli non è un reagente del tavolo avanzato.

Senza formazione puoi scegliere soltanto il livello I. Con la formazione 5×5 completa puoi scegliere fino al limite configurato. Gli incantesimi incompatibili, disabilitati, riservati al bottino o già al limite non compaiono. Gli incantesimi sono scelti direttamente: non si effettuano estrazioni casuali.

Un incantesimo completo costa **30 livelli XP e 3 frammenti**. I livelli aggiunti costano in proporzione al limite completo, arrotondando per eccesso, con un minimo di 1 livello XP e 1 frammento. Esempio per un limite V: da zero a I costa 6 livelli XP e 1 frammento; da III a V costa 12 livelli XP e 2 frammenti. La modalità creativa non consuma XP né reagenti.

Chiudendo l'interfaccia, l'oggetto torna al giocatore e il reagente resta conservato nel tavolo secondo il sistema della mod.

## EMI, REI e ricettario vanilla

La **ricetta di costruzione** della tavola usa `minecraft:crafting_shaped`, quindi i visualizzatori EMI e REI la possono leggere come normale ricetta di crafting, senza dipendenze obbligatorie. Il ricettario vanilla la sblocca quando ottieni un frammento di ametista **oppure** un tavolo degli incantamenti.

Questo supporto riguarda la costruzione della tavola: non aggiunge categorie EMI/REI per ogni operazione di incantamento e non inserisce un ricettario di crafting nell'interfaccia di incantamento. Usa sempre una versione del visualizzatore compatibile con Minecraft 26.3; l'EMI ufficiale e le sue versioni portate dalla community hanno disponibilità diversa.

Fonti: [formazione](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/AdvancedEnchantingFormation.java), [menu](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java), [ricetta](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/recipe/advanced_enchanting_table.json).
