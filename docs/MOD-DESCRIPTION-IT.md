# Eldritch Surge

**Espandi la progressione dei tuoi incantesimi con 119 incantamenti personalizzati, incantamento avanzato, Libri degli Incantesimi e regole di mondo configurabili.**

Eldritch Surge è una mod Fabric per Minecraft 26.3 e 26.4 Snapshot 8 che rivoluziona il modo in cui potenzi il tuo equipaggiamento. Scopri nuove opzioni per armi, armature, strumenti, archi, balestre, canne da pesca, tridenti, mazze, lance, scudi e molto altro, con effetti dedicati a combattimento, esplorazione, estrazione, mobilità, sopravvivenza e lancio di incantesimi.

> **Prima release pubblica — 2.0.0**  
> Eldritch Surge è stato sviluppato e testato internamente fino alla versione 1.9.1.1. Questa è la prima distribuzione ufficiale per la comunità.

## 119 incantamenti personalizzati

Esplora una vasta collezione di incantesimi che include abilità offensive, potenziamenti difensivi, benedizioni ambientali, maledizioni, effetti di utilità e rituali. Ciascun incantesimo ha limiti di livello definiti, categorie di oggetti compatibili e regole di ottenimento predefinite.

Costruisci build incentrate su incantesimi per armi come Katana, Freeze Aspect e Leeching Aspect; sfrutta opzioni per armatura e movimento come Health Upgrade, Rabbit Foot e Snowshoeing; oppure forgia strumenti specializzati con Digger, Excavator, Lumberjack e Smelting.

La collezione comprende anche incantesimi per archi, balestre, canne da pesca, tridenti, mazze, lance e shulker box.

## Tavolo degli Incantamenti Avanzato

Il Tavolo degli Incantamenti Avanzato introduce un nuovo livello nella progressione. Utilizza Frammenti di Ametista, memorizzazione persistente su NBT (con rilascio sicuro degli oggetti alla rottura del blocco) e una formazione 5×5 di blocchi d'oro e quarzo cesellato per sbloccare i livelli superiori.

I costi in XP scalano dinamicamente in base alla classe di rarità dell'incantesimo (Common, Uncommon, Rare, Epic, Ultra). La disponibilità nel tavolo normale e in quello avanzato è configurabile separatamente.

## Libri Magici e Rituali

I Libri Magici (Spell Books) sono una categoria distinta di oggetti per incantesimi ad attivazione, che estendono il sistema oltre i normali potenziamenti. La collezione include Cocktail Spell, Ragnarok, Red Moon, Storm Spell e Trench Spell, legati al tavolo avanzato tramite i Frammenti dell'Eco.

Ogni rituale modifica il mondo: evocazioni, fulmini e scavo sono effetti reali, non semplici animazioni.

## Fusioni speciali all'incudine

Alcuni incantesimi dispongono di combinazioni uniche all'incudine, tra cui le fusioni per forgiare Blade of Apocalypse e Star Fate, nonché la trasformazione da Katana a Gream Reaper tramite una Stella del Nether.

## Comandi e gamerule

Gli operatori possono regolare le meccaniche di gioco tramite i comandi `/eldrich <setting> <on/off>` (o `/eldritch`):
- `keepLoyaltyTridents`: Impedisce la perdita dei tridenti con Lealtà in caso di morte o caduta nel vuoto.
- `sweepingRequiresEnchantment`: Rende obbligatorio l'incantesimo Sweeping Edge per eseguire l'attacco a spazzata.
- `lethalPoison`: Permette al danno da Veleno di essere letale su `player`, `mob`, o `all`.

## Configurazione di ogni mondo

Ogni mondo memorizza le proprie impostazioni: presenza nei tavoli normale/avanzato, nel bottino e limiti massimi per incudine e tavolo.
Nei server multigiocatore, le impostazioni attive vengono sincronizzate ai client e possono essere modificate dagli operatori con livello di permesso 2 o superiore.

## Integrazione EnchLib (Opzionale)

EnchLib aggiunge un editor visuale e un'API per categorie di mob, metadata degli incantesimi, rarità e regole di compatibilità. Eldritch Surge e EnchLib sono mod separate; EnchLib è opzionale e non è incluso nel download.

## Installazione

1. Installa Fabric Loader per Minecraft 26.3 o 26.4 Snapshot 8 e usa Java 25.
2. Aggiungi Fabric API, Fabric Language Kotlin e il file JAR di Eldritch Surge nella cartella `mods`.
3. (Opzionale) Aggiungi Mod Menu e Cloth Config / YACL per la schermata di configurazione in gioco.
4. (Opzionale) Installa EnchLib per le integrazioni estese.

**Versione:** 2.0.0 · **Minecraft:** 26.3 / 26.4 Snapshot 8 · **Loader:** Fabric · **Licenza:** MIT