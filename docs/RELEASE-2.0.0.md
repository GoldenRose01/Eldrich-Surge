# Eldritch Surge — Prima Release Pubblica (2.0.0)

**Data:** 2026-11-26  
**Versione:** 2.0.0  
**Minecraft:** 26.3 / 26.4 Snapshot 8  
**Loader:** Fabric  
**Licenza:** MIT  
**Repository:** https://github.com/GoldenRose01/Eldrich-Surge  
**Wiki:** https://github.com/GoldenRose01/Eldrich-Surge/wiki

---

## Panoramica

Eldritch Surge è un **mod Fabric per l'incantamento** che rivoluziona la progressione degli incantesimi in Minecraft. Aggiunge **119 incantamenti personalizzati**, un **Tavolo degli Incantamenti Avanzato**, **Libri Magici** per rituali, **fusioni all'incudine** speciali e **regole di mondo configurabili**.

## Caratteristiche principali

### 119 incantamenti personalizzati
Distribuiti in 7 famiglie tematiche:
- **Tiro e proiettili** (9) — Accuracy Shot, Elasticity, Pop, Sniper, Theft e altri
- **Armatura e mobilità** (38) — Health Upgrade, Snowshoeing, Soft Falling, Engine, Sea Flakes e altri
- **Combattimento** (54) — Katana, Blade of Apocalypse, Gream Reaper, Neptune's Will, Bloody Mary e altri
- **Rituali** (5) — Cocktail Spell, Ragnarok, Red Moon, Storm Spell, Trench Spell
- **Durabilità** (8) — Soulbond, DeathBreak, Welding, Curse of Incompatibility e altri
- **Scavo e strumenti** (5) — Digger, Excavator, Lumberjack, Smelting, Veinminer
- **Inventario** (3) — Vacuum, Siphon, Refill (Shulker Box)

### Tavolo degli Incantamenti Avanzato
- Ricetta di crafting con **frammenti di ametista**
- **Formazione 5×5** in oro e quarzo cesellato per sbloccare livelli superiori
- **Costi XP scalati per rarità** (15–35 livelli)
- **Persistenza dei reagenti** salvati in NBT (non si perdono al riavvio)
- **Rilascio sicuro** dei reagenti alla rottura del blocco
- Selezione diretta degli incantesimi (nessuna RNG nascosta)

### Libri Magici e Rituali
Cinque rituali lanciabili tramite libri magici:
- **Cocktail Spell** — copia gli effetti attivi del lanciatore ai giocatori vicini
- **Ragnarok** — evoca 4 cavalieri Wither Skeleton
- **Red Moon** — evoca un'onda di 30 zombie equipaggiati
- **Storm Spell** — scatena una tempesta con 5 fulmini
- **Trench Spell** — scava una voragine fino a Y=-55

### Fusioni all'Incudine
- **Blade of Apocalypse** — fusione di 5 incantesimi di categoria + Stella del Nether
- **Star Fate** — fusione di 5 incantesimi difensivi + Stella del Nether
- **Gream Reaper** — Katana + Stella del Nether (o Blade + Star Fate)
- **DeathBreak** — due libri Unbreaking III

### Comandi e Gamerule
Comando `/eldrich <setting> <on/off>` (alias `/eldritch`) per operatori (livello 2+):
- `keepLoyaltyTridents` — preserva i tridenti con Lealtà alla morte
- `sweepingRequiresEnchantment` — richiede Sweeping Edge per il fendente
- `lethalPoison` — permette al veleno di essere letale

Gamerule custom: `eldrich-surge:pvp_enchantment_modifier`, `lethal_poison`, `keep_loyalty_tridents`, `sweeping_requires_enchantment`.

### Configurazione per Mondo
- Ogni mondo ha il proprio file `enchantment_caps.json`
- Preset globale in `config/eldrich-surge/enchantment_caps.json`
- Disponibilità per tavolo, loot e livelli configurabili per ogni incantamento
- Sincronizzazione server → client con controlli di permessi

### Integrazione EnchLib (Opzionale)
- Editor visuale per categorie di mob e regole degli incantesimi
- API per compatibilità con datapack esterni
- Mod separata, non inclusa nel download

## Installazione

1. Installa **Fabric Loader** per Minecraft 26.3 o 26.4 Snapshot 8 (Java 25 richiesto)
2. Aggiungi **Fabric API** e **Fabric Language Kotlin**
3. Posiziona il JAR di Eldritch Surge nella cartella `mods`
4. (Opzionale) **Mod Menu** + **Cloth Config** o **YACL** per configurazione in gioco
5. (Opzionale) **EnchLib** per integrazioni estese

## Compatibilità

| Componente | Versione |
|---|---|
| Minecraft | 26.3 · 26.4 Snapshot 8 |
| Mod Loader | Fabric |
| Fabric API | Richiesta (`0.161.0+26.3` / `0.166.2+26.4`) |
| Fabric Language Kotlin | Richiesta (`1.13.12+kotlin.2.4.0`) |
| EnchLib | Opzionale (separato) |
| Mod Menu + Cloth Config / YACL | Opzionale (almeno uno) |

## JAR di Rilascio

| Minecraft | File |
|---|---|
| 26.3 | `eldrich-surge-26.3+-2.0.0.jar` |
| 26.4 Snapshot 8 | `eldrich-surge-26.4-snapshot-8+-2.0.0.jar` |

## Documentazione

- **Wiki completa:** https://github.com/GoldenRose01/Eldrich-Surge/wiki
- **Catalogo 119 incantamenti:** [Enchantments.md](wiki/Enchantments.md)
- **Tavolo avanzato:** [Tavolo-avanzato.md](wiki/Tavolo-avanzato.md)
- **Comandi e gamerule:** [Comandi-e-gamerule.md](wiki/Comandi-e-gamerule.md)
- **Configurazione:** [Configuration.md](wiki/Configuration.md)
- **Fusioni all'incudine:** [Incudine.md](wiki/Incudine.md)
- **Rituali:** [Rituali.md](wiki/Rituali.md)
- **Compatibilità:** [Compatibilita.md](wiki/Compatibilita.md)

## Note Tecniche

- **Linguaggi:** Java 25 (core, mixin, menu, screen) + Kotlin 2.4.0 (client init, data generator, entity taxonomy)
- **Build:** `gradlew build` (tutti i test passano)
- **Verifiche automatiche:** 13 runner che coprono catalogo, codec, ricette, matematica, bilanciamento e risorse
- **Mixins:** 38 server + 6 client (compatibilità JAVA_25)
- **Namespace:** `eldrich-surge` (standardizzato)

## Licenza

MIT License — vedi [LICENSE](LICENSE).

---

> **Nota:** Questa è la prima release pubblica. Eldritch Surge è stato sviluppato e testato internamente fino alla versione 1.9.1.1. La 2.0.0 rappresenta il primo rilascio ufficiale per la comunità, con documentazione completa e build verificate.
