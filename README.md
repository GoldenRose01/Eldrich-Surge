# Eldritch Surge

📚 **[Wiki completa](https://github.com/GoldenRose01/Eldrich-Surge/wiki)** — catalogo degli incantamenti, regole, tavolo avanzato, fusioni e configurazione.

> **Prima release pubblica — 2.0.0**  
> Eldritch Surge è stato sviluppato e testato internamente fino alla versione 1.9.1.1. Questa è la prima distribuzione ufficiale per la comunità.

**A Fabric enchantment overhaul for Minecraft 26.3 & 26.4 Snapshot 8**

Eldritch Surge espande la progressione degli incantamenti e il combattimento con **119 incantamenti personalizzati**, nuovi gruppi di incantamento, un Tavolo degli Incantamenti Avanzato, Libri Magici per rituali, fusioni speciali all'incudine, persistenza dei reagenti e regole di mondo configurabili.

## Caratteristiche

- **119 incantamenti personalizzati** con livelli definiti, categorie di oggetti, percorsi di ottenimento e classi di rarità.
- Effetti per combattimento, estrazione, armatura, movimento, bottino, maledizioni, benedizioni e lancio di incantesimi.
- **Tavolo degli Incantamenti Avanzato** con progressione separata, persistenza dei reagenti su NBT (rilascio sicuro alla rottura), costi XP scalati per rarità e formazione 5×5 richiesta per i livelli superiori.
- **Libri Magici** come tipo di oggetto distinto per incantamenti da lancio: Cocktail Spell, Ragnarok, Red Moon, Storm Spell e Trench Spell.
- **Fusioni speciali all'incudine**: Blade of Apocalypse (5 componenti), Star Fate (5 componenti), Gream Reaper (Katana + Stella del Nether) e DeathBreak (Unbreaking III × 2).
- Comandi e gamerule unificati tramite `/eldrich <setting> <on/off>` (alias `/eldritch`): `keepLoyaltyTridents`, `sweepingRequiresEnchantment`, `lethalPoison`.
- Conservazione sicura degli oggetti con **Soulbond** al respawn anche con `keepInventory = false`.
- Integrazione opzionale con **EnchLib** per categorie di mob, metadata degli incantamenti e regole di compatibilità. EnchLib è una mod separata e non è inclusa nel JAR.
- **Configurazione per mondo**: ogni salvataggio mantiene le proprie regole. Il server sincronizza le impostazioni attive ai client. Modifiche richiedono livello operatore 2+.

## Compatibilità

| Componente | Versione / stato |
|---|---|
| Minecraft | 26.3 · 26.4 Snapshot 8 |
| Mod | 2.0.0 |
| Mod loader | Fabric |
| Fabric API | Richiesta (`0.161.0+26.3` / `0.166.2+26.4`) |
| Fabric Language Kotlin | Richiesta (`1.13.12+kotlin.2.4.0`) |
| EnchLib | Opzionale; progetto e JAR separati |
| Mod Menu + Cloth Config o YACL | Opzionale; almeno uno abilita la schermata di configurazione in gioco |

## Installazione

1. Installa **Fabric Loader** per Minecraft 26.3 o 26.4 Snapshot 8 (Java 25 richiesto).
2. Aggiungi **Fabric API**, **Fabric Language Kotlin** e il JAR di Eldritch Surge nella cartella `mods`.
3. (Opzionale) Aggiungi **Mod Menu** + **Cloth Config** o **YACL** per la schermata di configurazione in gioco.
4. (Opzionale) Installa **EnchLib** separatamente per le integrazioni estese.

I JAR di rilascio sono disponibili nella cartella [`Jar/`](Jar/) e sul repository GitHub:
- `eldrich-surge-26.3+-2.0.0.jar`
- `eldrich-surge-26.4-snapshot-8+-2.0.0.jar`

## Catalogo e documentazione

Il catalogo completo dei 119 incantamenti è disponibile nella [wiki](docs/wiki/Enchantments.md). Ogni incantamento ha una scheda dedicata con effetto, livello massimo, oggetti supportati, ottenimento e incompatibilità.

La [home della wiki](docs/wiki/Home.md) contiene guide alla configurazione, al tavolo avanzato, ai rituali e alle fusioni.

## Configurazione del mondo

I preset globali sono in `config/eldrich-surge/enchantment_caps.json`. Ogni salvataggio mantiene le proprie override in `eldrich-surge/enchantment_caps.json` nella directory del mondo. Il server invia lo snapshot attivo ai client e accetta modifiche solo da operatori con livello 2+. Vedi [Configurazione](docs/wiki/Configuration.md).

## Sviluppo

L'architettura del progetto e il contesto delle meccaniche sono dettagliati in [`project_context/context.md`](project_context/context.md).

Le verifiche standalone sono registrate centralmente in `gradle/verification.gradle.kts`; aggiungi un task name e un entry point per includere un nuovo controllo in `gradlew check`.

Build con `gradlew build`. EnchLib è caricato come dipendenza runtime separata opzionale quando il suo JAR è disponibile nel progetto sibling configurato; non è mai incorporato in Eldritch Surge.

## Licenza

MIT.