# ✨ Ottenere gli incantamenti

[Home](Home) · [Catalogo](Enchantments) · [Tavolo avanzato](Tavolo-avanzato) · [Incudine](Incudine)

| Percorso | Reagente / requisito | Risultato |
|---|---|---|
| Tavolo normale | Lapislazzuli e livelli XP | Offerte del preset normale compatibili con l’oggetto |
| Tavolo avanzato | Ametista; formazione per gli slot superiori | Offerte del preset avanzato |
| Rituali | Libro normale + frammento di eco nel tavolo avanzato | Libro magico marcato con il rituale |
| Incudine | Oggetti o libri validi e XP | Applicazione, combinazione o trasformazione speciale |
| Loot | Tabelle e impostazioni attive | Risultati previsti dalle tabelle caricate |
| Comandi | Oggetto impugnato e comando appropriato | Applicazione diretta |

## Livelli e rarità

Il massimo delle schede è quello della definizione datapack. Il mondo può impostare cap distinti per incudine e tavolo; EnchLib può fornire ulteriori valori. Aumentare il cap non garantisce che ogni effetto continui a crescere linearmente: alcuni sono limitati o controllano solo la presenza dell’incantamento.

Il filtro delle offerte considera disponibilità, oggetto, incompatibilità, reagente e rarità. I pesi configurabili sono 100 per `common`, 60 per `uncommon`, 30 per `rare` e 5 per `very_rare`. La probabilità del filtro è il prodotto della probabilità del tavolo per il peso, diviso 100 con aritmetica intera. Non è la probabilità finale di ricevere quell’incantamento fra tutte le offerte.

## Eccezioni ai tavoli

DeathBreak, Blade of Apocalypse, Star Fate e Gream Reaper sono esclusi esplicitamente dai tavoli. Le etichette storiche «Normal» o «Advanced» non annullano questa esclusione. Vedi le [fusioni all’incudine](Incudine).

Una voce «Loot» nel configuratore non crea automaticamente una nuova tabella di bottino e non identifica un luogo garantito: contano le tabelle e i datapack caricati. Per le teste di Bloody Mary esiste una [meccanica dedicata](Incantamento-bloody-mary).

## Oggetti primari e supportati

`primary_items`, quando presente, restringe gli oggetti candidati al tavolo. `supported_items` descrive il supporto dati più ampio. Le regole configurabili possono aggiungere un altro filtro: nelle schede vengono riportati separatamente dati e regole del configuratore.

Fonte: [EnchantmentMenuMixin](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java).
