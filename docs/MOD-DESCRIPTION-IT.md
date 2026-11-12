# Eldritch Surge

**Espandi la progressione dei tuoi incantesimi con 119 incantamenti personalizzati, incantamento avanzato, Libri degli Incantesimi e regole di mondo configurabili.**

Eldritch Surge è una mod Fabric per Minecraft 26.3 e 26.4 Snapshot 8 che rivoluziona il modo in cui potenzi il tuo equipaggiamento. Scopri nuove opzioni per armi, armature, strumenti, archi, balestre, scudi e molto altro, con effetti dedicati a combattimento, esplorazione, estrazione, mobilità, sopravvivenza e lancio di incantesimi.

## 119 incantamenti personalizzati

Esplora una vasta collezione di incantesimi che include abilità offensive, potenziamenti difensivi, benedizioni ambientali, maledizioni, effetti di utilità e rituali. Ciascun incantesimo ha limiti di livello definiti, categorie di oggetti compatibili e regole di ottenimento predefinite.

Costruisci build incentrate su incantesimi per armi come Katana, Freeze Aspect e Leeching Aspect; sfrutta opzioni per armatura e movimento come Health Upgrade, Rabbit Foot e Snowshoeing; oppure forgia strumenti specializzati con Digger, Excavator, Lumberjack e Smelting.

La collezione comprende anche incantesimi per canne da pesca, tridenti, mazze, lance e shulker box.

## Incantamento avanzato e persistenza reagenti

Il Tavolo degli Incantamenti Avanzato introduce un nuovo livello nella progressione. Utilizza Frammenti di Ametista, memorizzazione persistente su NBT (con rilascio sicuro degli oggetti alla rottura del blocco) e una formazione 5×5 di blocchi d'oro e quarzo cesellato per sbloccare i livelli superiori.

I costi in XP scalano dinamicamente in base alla classe di rarità dell'incantesimo (Common, Uncommon, Rare, Epic, Ultra). La disponibilità nel tavolo normale e in quello avanzato è configurabile separatamente.

## Comandi e gamerule

Gli operatori possono regolare le meccaniche di gioco tramite i comandi `/eldrich <setting> <on/off>` (o `/eldritch`):
- `keepLoyaltyTridents`: Impedisce la perdita dei tridenti con Lealtà in caso di morte o caduta nel vuoto.
- `sweepingRequiresEnchantment`: Rende obbligatorio l'incantesimo Sweeping Edge per eseguire l'attacco a spazzata.
- `lethalPoison`: Permette al danno da Veleno di essere letale su `player`, `mob`, o `all`.

## Libri magici e rituali

I Libri degli Incantesimi (Spell Books) sono una categoria distinta di oggetti per incantesimi ad attivazione, che estendono il sistema oltre i normali potenziamenti. La collezione include Cocktail Spell, Ragnarok, Red Moon, Storm Spell e Trench Spell, legati al tavolo avanzato tramite i Frammenti dell'Eco.

## Fusioni speciali all'incudine

Alcuni incantesimi dispongono di combinazioni uniche all'incudine, tra cui le fusioni per forgiare Blade of Apocalypse e Star Fate, nonché la trasformazione da Katana a Gream Reaper tramite una Stella del Nether.

## Configurazione di ogni mondo

Ogni mondo memorizza le proprie impostazioni: presenza nei tavoli normale/avanzato, nel bottino e limiti massimi per incudine e tavolo.
Nei server multigiocatore, le impostazioni attive vengono sincronizzate ai client e possono essere modificate dagli operatori con livello di permesso 2 o superiore.

## Installazione

1. Installa Fabric Loader per Minecraft 26.3 o 26.4 Snapshot 8 e usa Java 25.
2. Aggiungi Fabric API, Fabric Language Kotlin e il file JAR di Eldritch Surge nella cartella `mods`.
3. (Opzionale) Aggiungi Mod Menu e Cloth Config / YACL per la schermata di configurazione in gioco.
4. (Opzionale) Installa EnchLib per le integrazioni estese.

**Versione:** 1.8.0 · **Minecraft:** 26.3 / 26.4 Snapshot 8 · **Loader:** Fabric · **Licenza:** MIT
