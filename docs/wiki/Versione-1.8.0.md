# Eldrich Surge 1.8.0

[Home](Home) · [Versione precedente](Versione-1.7.1) · [Tavolo avanzato](Tavolo-avanzato) · [Comandi e gamerule](Comandi-e-gamerule)

**Minecraft 26.3 / 26.4 Snapshot 8 · Fabric · 119 incantesimi nel catalogo**

## Cosa cambia dalla 1.7.1

- **Nuovo incantamento Lumberjack:** introdotto l'incantamento `eldrich-surge:lumberjack` per asce (pietra, ferro, oro, diamante, netherite), che permette di abbattere l'intero albero in un colpo mentre si è abbassati (sneak), integrato con il sistema di felling avanzato e mod compatibili.
- **Tassonomia e Categorie Geografiche dei Mob:** Classificati tutti i 90 mob viventi di Minecraft (inclusi quelli dello snapshot 26.4: Creaking, Breeze, Bogged, Armadillo, Nautilus, Zombie Nautilus, Camel Husk, Parched, ecc.) e aggiunti i nuovi tag dedicati per livello geografico/habitat (`geographical/surface`, `geographical/underground`, `geographical/aquatic`, `geographical/aerial`, `geographical/nether`, `geographical/end`, `geographical/arid`, `geographical/cold`).
- **Bilanciamento Danno di Categoria (+2.5 per livello):** Tutti i 10 incantesimi mirati a categorie tassonomiche (`butcher`, `undead_slayer`, `creeping_threat`, `witch_hunter`, `exorcist`, `bane_of_end`, `wrath_of_the_abyss`, `smoother`, `herbicide`, `flogging`) infliggono ora uniformemente **+2.5 di danno per livello**.
- **Formula Incantesimi Compositi (Star Fate & Blade of Apocalypse):** Aggiornata la formula di calcolo per entrambi a **+2.5 × livello × numero di categorie soddisfatte dal bersaglio**.
- **Gream Reaper (+3.0 per livello):** Danno universale aumentato da +2.5 a **+3.0 per livello** contro qualsiasi creatura senza restrizioni di categoria.
- **Neptune's Will (Sostituzione di Impaling e Attributo Stile Katana):** Sostituisce ed elimina dal gioco l'incantesimo vanilla Impalamento (*Impaling*). Applica al tridente l'attributo nativo `minecraft:attack_damage` con scaling logaritmico da livello 1 a 5 (I–V), partendo da **+2.5 danni per livello** sia in mischia sia al lancio.
- **Localizzazione Completa Duale (EN/IT):** Tutte le descrizioni e i tooltip dei 119 incantesimi tradotti e verificati in italiano e inglese.
- **Soulbond e conservazione al respawn:** risolto il problema per cui con `keepInventory = false` gli oggetti con Soulbond venivano persi al respawn. Tramite `ServerPlayerRespawnMixin`, gli oggetti con Soulbond vengono mantenuti esattamente negli slot di origine dell'inventario al momento del respawn.
- **Risoluzione doppie applicazioni (JSON + Java):**
  - `bane_of_end` e `undead_slayer`: rimossi gli effetti datapack ridondanti, lasciando la sola applicazione Java in `MobCategoryDamageMechanics` per garantire il corretto bonus per livello contro le rispettive categorie senza duplicazioni.
  - `smithcrafts`: rimosso il modificatore di armatura duplicato via codice; l'attributo nativo nello scudo in seconda mano applica +1 armatura per livello.
  - `curse_of_target`: rimossa la riapplicazione manuale dell'effetto Glowing da mixin proiettile; il datapack gestisce nativamente la durata di 1 secondo per livello.
  - `replenish`: rimossa la doppia estrazione casuale in Java; il consumo munizioni pari a 0 sulle frecce speciali nel datapack gestisce la conservazione con probabilità 33% × livello.
- **Comandi `/eldrich` e gestione gamerule:**
  - Introdotta la sintassi `/eldrich <setting> <on/off>` (e alias `/eldritch`) per configurare a livello globale le opzioni del mod.
  - `keepLoyaltyTridents`: quando attiva, i tridenti con Lealtà non cadono a terra e non si perdono nel vuoto o alla morte del giocatore.
  - `sweepingRequiresEnchantment`: quando attiva, l'attacco a spazzata (sweeping) con la spada richiede necessariamente l'incantesimo Sweeping Edge.
  - `lethalPoison`: configurabile come `player`, `mob`, `all` o `off`; permette al veleno di azzerare la salute ed eliminare l'entità bersaglio anziché fermarsi a mezzo cuore.
  - Rimosso il comando deprecato e non autorizzato `/surge`.
- **Persistenza reagenti e sicurezza blocchi:**
  - I reagenti del tavolo d'incantamento (ametista, lapislazzuli, frammenti d'eco) non vengono più persi durante il riavvio del server o la chiusura del mondo: sono salvati persistentemente nei dati NBT del blocco.
  - Alla rottura del tavolo, tutti i reagenti inseriti vengono droppati a terra in modo sicuro nel mondo tramite `BlockEntityMixin`.
- **Spirit Cast e contatore uccisioni:**
  - Il contatore uccisioni per Spirit Cast viene preservato attraverso disconnessioni e riavvii e si azzera solo alla morte del giocatore.
- **Bilanciamento Armored su Elitre:**
  - Riportato il bonus armatura fornito da Armored su Elitre a **2 punti armatura per livello** (massimo 10 punti al livello V), allineando il comportamento effettivo alla descrizione ufficiale dell'incantesimo.
- **RNG deterministico nel tavolo vanilla:**
  - Sostituito il generatore casuale generico con il seed/RandomSource dell'incantesimo per garantire corrispondenza assoluta tra l'anteprima mostrata e l'incantesimo applicato.
- **Spawn sicuro per Ragnarok e Red Moon:**
  - Aggiunti controlli di sicurezza `hasChunkAt` per prevenire caricamenti forzati di chunk ai confini e impedire generazioni errate sul soffitto di bedrock del Nether.
- **Prestazioni del Tavolo Avanzato:**
  - Eliminato il controllo per-tick (20 volte al secondo) della formazione d'oro 5×5; la validazione avviene ora solo all'apertura dell'interfaccia e alla modifica degli slot.
  - Costi XP scalati dinamicamente in base alla rarità dell'incantesimo (Common: 15, Uncommon: 20, Rare: 25, Epic: 30, Ultra: 35 livelli massimi).
- **Loot e schede creative:**
  - `god_protection` e `gream_reaper` abilitati nei drop/loot (`loot: true`).
  - Assegnata correttamente la categoria creativa per `vanishing_curse`.
- **Aggiornamento Snapshot:**
  - Aggiunto il supporto e compilato il JAR per **Minecraft 26.4 Snapshot 8**.

## JAR disponibili

| Minecraft | File |
|---|---|
| 26.3 | `eldrich-surge-26.3+-1.8.0.jar` |
| 26.4 Snapshot 8 | `eldrich-surge-26.4-snapshot-8+-1.8.0.jar` |
