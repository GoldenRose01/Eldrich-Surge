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

Inserisci l'oggetto nello slot superiore a sinistra e il reagente in quello inferiore. Cerca un incantesimo nell'elenco, selezionalo, scegli il livello con **− / +**, poi premi **Incanta**. Le frecce e la rotella scorrono l'elenco. Passando il cursore su un incantesimo si apre un pannello con i dettagli del solo incantesimo evidenziato. Puoi spostare il cursore nel riquadro per mantenerlo aperto. Il tooltip del pulsante Incanta mostra il costo completo.

**Ametista** seleziona gli incantamenti ordinari avanzati. **Frammenti dell'eco** selezionano i rituali su un libro normale. Lapislazzuli non è un reagente del tavolo avanzato.

Il massimo mostrato è sempre il limite reale configurato, anche senza formazione: non viene più indicato I per tutti gli incantesimi. Senza formazione puoi comunque scegliere soltanto il livello I. Con la formazione 5×5 completa puoi scegliere fino al limite configurato. Gli incantesimi incompatibili, disabilitati, riservati al bottino o già al limite non compaiono. Gli incantesimi sono scelti direttamente: non si effettuano estrazioni casuali.

La verifica della struttura rituale è ottimizzata per verificarsi all'apertura dell'interfaccia e ad ogni cambio degli slot, eliminando controlli inutili su ogni singolo tick di gioco.

Il costo in livelli XP scala in base alla classe di rarità dell'incantesimo selezionato:
- **Common:** fino a 15 livelli XP e 1 frammento
- **Uncommon:** fino a 20 livelli XP e 2 frammenti
- **Rare:** fino a 25 livelli XP e 2 frammenti
- **Epic:** fino a 30 livelli XP e 3 frammenti
- **Ultra:** fino a 35 livelli XP e 3 frammenti

I livelli intermedi aggiunti costano in proporzione al livello massimo configurato, arrotondando per eccesso, con un minimo di 1 livello XP e 1 frammento. La modalità creativa non consuma XP né reagenti.

Chiudendo l'interfaccia, l'oggetto inserito torna all'inventario del giocatore. Il reagente inserito resta memorizzato in modo persistente nei dati NBT del blocco (conservandosi anche attraverso riavvii del server o ricaricamenti dei chunk) e viene automaticamente droppato a terra se il blocco del tavolo viene distrutto.

## EMI, REI e ricettario vanilla

La **ricetta di costruzione** della tavola usa `minecraft:crafting_shaped`, quindi i visualizzatori EMI e REI la possono leggere come normale ricetta di crafting, senza dipendenze obbligatorie. Il ricettario vanilla la sblocca quando ottieni un frammento di ametista **oppure** un tavolo degli incantamenti.

Questo supporto riguarda la costruzione della tavola: non aggiunge categorie EMI/REI per ogni operazione di incantamento e non inserisce un ricettario di crafting nell'interfaccia di incantamento. Usa sempre una versione del visualizzatore compatibile con Minecraft 26.3; l'EMI ufficiale e le sue versioni portate dalla community hanno disponibilità diversa.

Fonti: [formazione](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/AdvancedEnchantingFormation.java), [menu](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/menu/AdvancedEnchantmentMenu.java), [ricetta](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/eldrich-surge/recipe/advanced_enchanting_table.json).
