# 📜 Project Context — Eldritch Surge

> Questo documento è la **fonte unica di verità e comprensione** dell'intero progetto Eldritch Surge.
> È collocato nella cartella `project_context/` (completamente disaccoppiata dal sistema di build Gradle) per consentire a qualunque assistente o sviluppatore di comprendere immediatamente l'architettura, tutte le meccaniche di gioco, il catalogo dei 119 incantamenti, il funzionamento dei sistemi interni, le convenzioni di codice e le istruzioni di build e rilascio.

---

## 1. Panoramica del Progetto (Project Overview)

* **Nome Mod:** Eldritch Surge (`eldrich-surge`)
* **Piattaforma e Mod Loader:** Fabric (Minecraft 26.3 e Minecraft 26.4 Snapshot 8)
* **Versione Mod Corrente:** `1.8.0`
* **Linguaggi:** Java 25 (core, mixin, screen, menu), Kotlin 2.4.0 (entity taxonomy, generatori dati e init client)
* **Dipendenze Fondamentali:**
  * Fabric API (`0.161.0+26.3` / `0.166.2+26.4`)
  * Fabric Language Kotlin (`1.13.12+kotlin.2.4.0`)
  * Java 25 (`options.release = 25`)
* **Mod Esterne di Appoggio (`External_mod/`):**
  * `fabric-api-0.166.2+26.3.jar`
  * `modmenu-21.0.1.jar`
  * `cloth-config-fabric-26.3.159.jar`
  * `yet_another_config_lib_v3-3.9.7+26.3-fabric.jar` (YACL v3)
  * *(Opzionale)* EnchLib (`enchlib_version=1.6.1`)
* **Regole di Versioning (`AGENTS.md`):**
  * Incrementare l'ultimo numero di versione (es. da 1.8.0 a 1.8.1) **esclusivamente quando l'utente richiede esplicitamente la produzione di un nuovo JAR**. Le normali modifiche al codice, documentazione o risorse non incrementano la versione.
  * Se l'utente specifica una versione precisa, usare esattamente quella.
  * Includere sempre i JAR sia per l'ultima release stabile (26.3) sia per l'ultimo snapshot supportato (26.4 Snapshot 8).
  * **Non eliminare mai** i file JAR storici presenti nella directory `Jar/`.

---

## 2. Architettura del Codice e Mappa dei Package

Il codice sorgente è organizzato nei seguenti source set:
* `src/main/java`: logica di gioco comune, menu, incantesimi, comandi, configurazione, mixin server/comuni.
* `src/main/kotlin`: tassonomia delle entità (`EldritchEntityTaxonomy.kt`).
* `src/main/resources`: datapack (incantesimi, tag, loot table Bloody Mary), asset (lingue `it_it.json`, `en_us.json`, texture, modelli), mixin config e configurazione JSON.
* `src/client/java` e `src/client/kotlin`: interfacce grafiche (GUI tavolo avanzato, schermate config ModMenu/YACL/ClothConfig), tooltip avanzati, mixin client, HUD armatura.
* `src/test/java`: suite di test unitari e verifiche per Gradle check.

### Dettaglio dei Package Java (`src/main/java/eldritch/surge`)

| Package | Scopo e Classi Principali |
|---|---|
| `eldritch.surge` | Entry point principale `EldritchSurge.java` (inizializzazione mod, registrazione payload di rete, comandi, gamerule). |
| `eldritch.surge.combat` | • `AdditionalDamageCalculator`: calcola il danno aggiuntivo degli incantesimi tenendo conto della gamerule `PVP_ENCHANTMENT_MODIFIER`.<br>• `SecondaryEnchantmentDamage`: previene la ricorsione infinita o indesiderata di colpi secondari e danni indiretti. |
| `eldritch.surge.command` | • `EldritchCommands`: implementa la gestione dei comandi `/eldrich <setting> <on/off>` e `/eldritch <setting> <on/off>` per regolare a runtime `keepLoyaltyTridents`, `sweepingRequiresEnchantment`, e `lethalPoison` (`player`, `mob`, `all`, `off`). |
| `eldritch.surge.config` | • `EnchantmentCapsConfig`: gestione completa dei cap di livello, disponibilità nei tavoli (normale e avanzato), abilitazione nei drop/loot e compatibilità EnchLib, con salvataggio globale in `config/eldrich-surge/enchantment_caps.json` e snapshot per mondo. Include copy constructor sicuri. |
| `eldritch.surge.creative` | • `CreativeCategory`: enum delle categorie per le tab creative.<br>• `CreativeEnchantmentClassifier`: assegna automaticamente ciascun libro incantato alla giusta scheda creativa (armi, armature, strumenti, libri).<br>• `EldritchCreativeTabs`: registrazione e ordinamento delle tab. |
| `eldritch.surge.enchantment.effect` | • `GameruleDamageEnchantmentEffect`: effetto datapack custom che scala il danno secondo la configurazione gamerule.<br>• `LogarithmicLevelBasedValue`: codec custom per la progressione logaritmica del livello dell'incanto. |
| `eldritch.surge.enchantment.ench` | Contiene le 119 classi di definizione Java degli incantamenti (es. `LumberjackEnchantment`, `DiggerEnchantment`, `ArmoredEnchantment`, ecc.). Ciascuna classe estende o definisce metadati, oggetti applicabili, slot e rarità. |
| `eldritch.surge.enchantment.mechanics` | Meccaniche e logica runtime degli incantesimi:<br>• `BloodyMaryHeadMechanic`: gestione del drop delle teste in base alle 102 loot table dedicate.<br>• `DiggerMiningMechanic`: scavo orientato 3×3 basato sull'angolo di visuale del giocatore.<br>• `EnchLibMobCategories`: bridge con l'API opzionale di EnchLib.<br>• `EnchantmentBookTags`: simboli grafici e classificazione a icone sui libri incantati.<br>• `MobCategoryDamageMechanics`: bonus di danno contro le categorie (undead, void, arthropod, hell, rebel, fungi, ecc.).<br>• `ModEnchantmentDefinition` / `ModEnchantmentDefinitions`: catalogo runtime centrale immutabile dei 119 incantamenti.<br>• `PassiveEnchantmentMechanics`: tick del giocatore, armature, eliche, immunità ambientali, rigenerazione durabilità, attrattori per Shulker (`vacuum`, `siphon`, `refill`).<br>• `ProjectileEnchantmentMechanics`: gestione di frecce, balestre e proiettili (`accuracy_shot`, `pop`, `sniper`, `theft`, `elasticity`).<br>• `SpellCastMechanics`: esecuzione degli incantesimi dei Libri Rituali con controlli sicuri `hasChunkAt` e bounding box nel Nether.<br>• `SpiritCastMechanics`: contatore anime/uccisioni memorizzato che si azzera esclusivamente alla morte del giocatore.<br>• `ToolEnchantmentMechanics`: logica strumenti, scavo istantaneo (`pruning`) e fusione drop (`smelting`). |
| `eldritch.surge.entity` | `EldritchEntityTaxonomy.kt`: mappa di classificazione di tutti i mob vanilla e moddati in 13 categorie basate su tag. |
| `eldritch.surge.game` | • `EldritchGameRules`: registrazione delle gamerule custom.<br>• `LoyaltyTridentStorage`: persistenza sicura dei tridenti con Lealtà per prevenire perdite nel vuoto o alla morte. |
| `eldritch.surge.item` | `SpellBookItem`: classe dell'oggetto Libro degli Incantesimi (Spell Book), gestisce il cast con click destro, i cooldown e il consumo di frammenti d'eco. |
| `eldritch.surge.mechanics.tree` | `TreeFeller`: logica di taglio dell'intero albero con ascia compatibile quando il giocatore è abbassato (`sneak`), utilizzata dall'incantesimo `Lumberjack`. |
| `eldritch.surge.menu` | • `AdvancedEnchantmentMenu`: contenitore e logica di rete del Tavolo Avanzato. Include validazione della struttura 5×5 (eseguita solo all'apertura o cambio slot, non a ogni tick).<br>• `AdvancedInfusingCosts`: calcolo dinamico dei costi XP scalati per rarità (Common: 15, Uncommon: 20, Rare: 25, Epic: 30, Ultra: 35).<br>• `EnchantingReagentStorage` / `ReagentHolder`: memorizzazione persistente su NBT dei reagenti (ametista, eco, lapislazzuli) e rilascio a terra alla distruzione del blocco. |
| `eldritch.surge.mixin` | Mixin critici per agganciare le meccaniche di gioco a Minecraft vanilla:<br>• `BlockEntityMixin`: rilascio sicuro dei reagenti memorizzati quando il blocco del tavolo viene rotto.<br>• `EnchantingTableBlockEntityMixin`: salvataggio e caricamento NBT dei reagenti.<br>• `ServerPlayerRespawnMixin`: conservazione garantita degli oggetti con Soulbond negli slot originali al respawn con `keepInventory = false`.<br>• `PoisonMobEffectMixin`: supporto alla gamerule `lethalPoison` (`player`, `mob`, `all`, `off`).<br>• `SweepAttackMixin`: supporto alla gamerule `sweepingRequiresEnchantment`.<br>• `ThrownTridentMixin`: supporto alla gamerule `keepLoyaltyTridents`.<br>• `EnchantmentMenuMixin`, `AnvilMenuMixin`, `AbstractArrowMixin`, `LivingEntityMixin`, ecc. |
| `eldritch.surge.network` | `WorldEnchantConfigPayload` e `WorldEnchantConfigSync`: sincronizzazione della configurazione tra server e client con controlli di sicurezza (livello operatore 2+). |

---

## 3. Sistemi di Gioco Fondamentali

### A. Tavolo degli Incantamenti Avanzato (Advanced Enchanting Table)
1. **Creazione:** Si crea circondando un Tavolo d'Incantamento vanilla con 4 Frammenti di Ametista.
2. **Formazione Richiesta:** Un cerchio/anello 5×5 con base di Blocchi d'Oro e Quarzo Cesellato centrato sotto il tavolo.
   * Il controllo della formazione avviene **solo all'apertura della GUI o alla modifica degli slot**, eliminando il sovraccarico di tick del server.
3. **Persistenza Reagenti:**
   * I reagenti (Ametista, Frammenti dell'Eco, Lapislazzuli) sono salvati nei dati NBT del blocco (`EnchantingTableBlockEntityMixin`).
   * Alla distruzione del blocco con piccone o esplosione, tutti i reagenti inseriti vengono droppati a terra in modo sicuro (`BlockEntityMixin`).
4. **Costi XP per Livello di Rarità:**
   * **Common:** 15 livelli XP max (1 ametista)
   * **Uncommon:** 20 livelli XP max (1 ametista)
   * **Rare:** 25 livelli XP max (2 ametiste)
   * **Epic:** 30 livelli XP max (3 ametiste)
   * **Ultra:** 35 livelli XP max (4 ametiste)
   * **Unique / Fusion:** non incantabili direttamente nel tavolo (ottenibili solo tramite ricette speciali o loot).

### B. Libri degli Incantesimi e Rituali (Spell Books)
Oggetto dedicato (`SpellBookItem`) utilizzato per gli incantesimi ad attivazione attiva:
1. `Cocktail Spell`: lancia un miscuglio casuale di pozioni ed effetti ad area.
2. `Ragnarok`: evoca uno stormo caotico di fulmini e meteore distruttive attorno all'area target.
3. `Red Moon`: altera la luna provocando ondate di mob ostili potenziati con drop speciali.
4. `Storm Spell`: scatena una tempesta con fulmini focalizzati sulle entità nemiche.
5. `Trench Spell`: scava istantaneamente una profonda voragine verticale ripulendo l'area cubica fino a Y=-55.
* **Sicurezza Spaziale:** Tutte le coordinate di spawn verificano `world.hasChunkAt(...)` e i confini dimensionali del Nether per impedire caricamenti forzati di chunk o spawn sul tetto di bedrock.

### C. Gamerule e Sintassi Comandi (`/eldrich`)
Gli operatori (livello di permesso 2+) possono gestire il comportamento globale con la sintassi unificata:
```
/eldrich <setting> <on/off>
/eldritch <setting> <on/off>
```
Impostazioni disponibili:
* `keepLoyaltyTridents <true/false>`: impedisce la perdita accidentale dei tridenti con Lealtà (se cadono nel vuoto o il giocatore muore, tornano al proprietario).
* `sweepingRequiresEnchantment <true/false>`: quando abilitata, l'attacco a spazzata (fendente) delle spade richiede obbligatoriamente l'incantesimo Sweeping Edge.
* `lethalPoison <player|mob|all|off>`: permette al danno dell'effetto Veleno di essere letale, azzerando la salute dell'entità anziché fermarsi a 0.5 cuori.

### D. Soulbond e Conservazione al Respawn
Quando un giocatore muore con `keepInventory = false`, il vanilla eliminerebbe gli oggetti o li dropperebbe. `ServerPlayerRespawnMixin` intercetta l'evento di respawn e ripristina gli oggetti dotati di `Soulbond` esattamente negli slot originali dell'inventario del giocatore rinato.

### E. Taglio Alberi (Lumberjack)
L'incantesimo `eldrich-surge:lumberjack` (applicabile ad asce in pietra, ferro, oro, diamante, netherite) consente al giocatore abbassato (`sneak`) di abbattere l'intero albero collegato con un solo colpo, integrandosi con il sistema `TreeFeller.java` e con mod di felling esterne.

---

## 4. Catalogo Completo dei 119 Incantamenti

Il catalogo comprende **119 incantamenti personalizzati**, suddivisi in 7 famiglie tematiche:

### 1. Tiro e Proiettili (9 incantamenti)
| ID | Incantamento | Max | Rarità | Oggetti | Effetto Riassuntivo |
|---|---|---|---|---|---|
| `accuracy_shot` | Accuracy Shot | 1 | Rare | Archi | Frecce senza gravità che seguono la mira ed evaporano dopo 30s. |
| `curse_of_target` | Curse of Target | 5 | Uncommon | Archi, Balestre | Applica l'effetto Glowing al bersaglio colpito (1s per livello). |
| `elasticity` | Elasticity | 3 | Legendary | Archi, Balestre | Aumenta la velocità dei proiettili del 25% per livello. |
| `nineleven` | Nineleven | 1 | Ultra | Archi, Balestre | Abbattimento istantaneo (instakill) di Phantom e Pipistrelli. |
| `pop` | Pop | 5 | Epic | Archi, Balestre | Esplosione all'impatto del dardo con raggio scalato per livello. |
| `replenish` | Replenish | 3 | Rare | Archi, Balestre | Possibilità del 33% × livello di non consumare la freccia speciale. |
| `sniper` | Sniper | 1 | Uncommon | Archi, Balestre | Aumenta il danno in proporzione alla distanza dal bersaglio. |
| `swiftness` | Swiftness | 5 | Legendary | Archi, Lance | Aumenta la velocità di movimento durante la carica dell'arma. |
| `theft` | Theft | 5 | Legendary | Archi, Balestre | Duplica casualmente i drop dei nemici abbattuti a distanza. |

### 2. Armatura e Mobilità (38 incantamenti)
| ID | Incantamento | Max | Rarità | Oggetti | Effetto Riassuntivo |
|---|---|---|---|---|---|
| `armored` | Armored | 5 | Common | Elitre | Conferisce +2 punti armatura per livello sull'Elytra (max 10 a liv V). |
| `blessing_of_the_night` | Blessing of the Night | 5 | Legendary | Elmi | Genera esperienza periodicamente durante le ore notturne. |
| `blessing_of_the_sea` | Blessing of the Sea | 1 | Rare | Armature | Cura il giocatore e ripara l'armatura quando immerso o sotto la pioggia. |
| `combustion_protection`| Combustion Protection | 5 | Epic | Armature | Riduce drasticamente danni da fuoco ed esplosioni e rinnova Resistenza al Fuoco. |
| `curse_of_corrosion` | Curse of Corrosion | 1 | Common | Armature | L'armatura infligge danni da usura e bruciatura quando bagnata. |
| `dodge` | Dodge | 3 | Rare | Gambali | Probabilità del 10% × livello di schivare completamente un attacco. |
| `dwarf_flakes` | Dwarf Flakes | 3 | Ultra | Armature | Rigenera durabilità con luce inferiore a 8 (sottoterra). |
| `dwarf_forged` | Dwarf Forged | 5 | Ultra | Armature | Probabilità di annullare il consumo di durabilità nei biomi sotterranei. |
| `end_adaptability` | End Adaptability | 1 | Ultra | Gambali | Teletrasporta su una superficie solida sicura prima di cadere nel vuoto. |
| `end_blessing` | End Blessing | 1 | Legendary | Elmi | Blocca la perdita di saturazione e fame all'interno dell'End. |
| `end_flakes` | End Flakes | 3 | Ultra | Armature | Rigenera durabilità mentre si è sospesi in aria/volo. |
| `end_forged` | End Forged | 5 | Ultra | Armature | Riduce il consumo di durabilità all'interno della dimensione End. |
| `engine` | Engine | 10 | Epic | Elitre | Probabilità del 10% × livello di non consumare il razzo pirotecnico in volo. |
| `god_protection` | God's Protection | 1 | Unique | Armature, Tridenti | Riduzione passiva globale del 60% sui danni subiti (solo loot). |
| `happy_boost` | Happy Boost | 3 | Rare | Elitre | Aumenta la velocità di planata con le elitre (+50%, +80%, +110%). |
| `healing_aura` | Healing Aura | 3 | Epic | Gambali | Cura periodicamente i compagni vicini quando ci si accovaccia. |
| `health_upgrade` | Health Upgrade | 5 | Epic | Pettorali | Aumenta i cuori massimi del giocatore tramite Health Boost permanente. |
| `heart_of_depth` | Heart of Depth | 1 | Legendary | Pettorali | Applica Cecità/Oscurità ai nemici che colpiscono il giocatore. |
| `heart_of_nether` | Heart of Nether | 1 | Legendary | Pettorali | Incendia automaticamente qualsiasi nemico che attacca in mischia. |
| `heart_of_the_sea` | Heart of the Sea | 1 | Legendary | Pettorali | Fornisce Conduit Power e Dolphin's Grace permanenti sott'acqua. |
| `heart_of_the_sky` | Heart of the Sky | 1 | Legendary | Pettorali | Congela e rallenta i nemici che infliggono danno al giocatore. |
| `hell_blessing` | Hell Blessing | 5 | Legendary | Elmi | Genera esperienza mentre si cammina sulla lava o si è avvolti dalle fiamme. |
| `hell_flakes` | Hell Flakes | 3 | Ultra | Armature | Rigenera durabilità dell'armatura quando si subisce calore estremo o fuoco. |
| `hicker` | Hicker | 5 | Epic | Gambali | Conferisce Salto Potenziato (Jump Boost) permanente. |
| `ice_speed` | Ice Speed | 5 | Epic | Stivali | Aumenta la velocità di camminata e corsa quando a terra. |
| `kinetic_protection` | Kinetic Protection | 5 | Rare | Elitre | Riduce il danno da impatto cinetico contro i muri in volo con elitre. |
| `nether_forged` | Nether Forged | 5 | Ultra | Armature | Probabilità di non consumare durabilità all'interno del Nether. |
| `rabbit_foot` | Rabbit Foot | 5 | Ultra | Stivali, Elmi | Aumenta la statistica Fortuna (Luck) di 1 per livello. |
| `sea_flakes` | Sea Flakes | 3 | Ultra | Armature | Rigenera durabilità dell'armatura durante il contatto prolungato con l'acqua. |
| `seeker_blessing` | Seeker Blessing | 5 | Legendary | Elmi | Evidenzia con alone luminoso gli oggetti a terra entro un raggio d'azione. |
| `snowshoeing` | Snowshoeing | 1 | Legendary | Stivali | Camminata su neve polverosa senza sprofondare e immunità al congelamento. |
| `soft_falling` | Soft Falling | 1 | Unique | Stivali | Attiva Caduta Lenta premendo spazio in aria ed elimina il danno da caduta. |
| `soil_falling` | Soil Falling | 1 | Rare | Stivali | Impedisce di distruggere i campi coltivati (farmland) saltandoci sopra. |
| `step_assist` | Step Assist | 3 | Rare | Stivali | Salita automatica dei gradini di 1 blocco senza saltare. |
| `sun_blessing` | Sun Blessing | 5 | Legendary | Elmi | Genera esperienza di giorno con cielo sereno nell'Overworld. |
| `villager_forged` | Villager Forged | 5 | Ultra | Armature | Riduce il consumo di durabilità nei villaggi e nell'Overworld. |
| `vision_blessing` | Vision Blessing | 1 | Legendary | Elmi | Fornisce Visione Notturna permanente. |
| `weapon_protection` | Weapon Protection | 5 | Rare | Armature | Riduce il danno subito dagli attaccanti che impugnano armi od oggetti. |

### 3. Combattimento (51 incantamenti)
Tra cui:
* `katana`: +1.25 danno per livello sulle spade.
* `blade_of_apocalypse`: fusione suprema all'incudine delle 5 componenti di danno categoria.
* `star_fate`: fusione speciale di difesa e contrattacco all'incudine.
* `gream_reaper`: ottenibile combinando Katana + Stella del Nether all'incudine; fornisce +2.5 danno generico per livello.
* `freeze_aspect`, `leeching_aspect`, `heartseeker`, `dash`, `dual_sweeping`, `bloody_mary`, `butcher`, `creeping_threat`, `exorcist`, `flogging`, `herbicide`, `undead_slayer`, `bane_of_end`, `witch_hunter`, `wrath_of_the_abyss`, `catapult`, `comet_slam`, `gravity_well`, `heavy_impact`, `seismic_wave`, `vanguard_charge`, `phalanx_stance`, `smithcrafts`, `immortality`, `fuse_cutter`, ecc.

### 4. Rituali (5 incantamenti su Libri degli Incantesimi)
* `cocktail_spell`, `ragnarok`, `red_moon`, `storm_spell`, `trench_spell`.

### 5. Durabilità e Conservazione (8 incantamenti)
* `curse_of_fatigue`, `curse_of_fragility`, `curse_of_incompatibility`, `curse_of_the_forest`, `deathbreak`, `enduring`, `soulbond`, `welding`.

### 6. Scavo e Strumenti (5 incantamenti)
* `digger`: scavo 3×3 incentrato sull'angolo di scavo.
* `excavator`: aumenta la portata (reach) di interazione con blocchi ed entità.
* `lumberjack`: abbattimento dell'intero albero in un colpo se abbassati (`sneak`).
* `pruning`: rottura istantanea di foglie e piante con cesoie.
* `smelting`: cottura istantanea automatica dei minerali estratti.

### 7. Inventario (3 incantamenti su Shulker Box)
* `refill`: rifornisce automaticamente lo stack nella mano principale pescando dalla Shulker Box.
* `siphon`: instrada gli oggetti raccolti negli stack incompleti della Shulker Box.
* `vacuum`: attrae magneticamente gli oggetti a terra direttamente nella Shulker Box.

---

## 5. Script e Istruzioni di Build, Test e Rilascio

### Compilazione dei JAR di Rilascio (`Jar/`)

1. **JAR per Minecraft 26.3:**
   ```powershell
   ./gradlew.bat jar --console=plain
   ```
   Produce: `Jar/eldrich-surge-26.3+-1.8.0.jar`

2. **JAR per Minecraft 26.4 Snapshot 8:**
   ```powershell
   ./gradlew.bat jar "-Pminecraft_version=26.4-snapshot-8" "-Pfabric_api_version=0.166.2+26.4" --console=plain
   ```
   Produce: `Jar/eldrich-surge-26.4-snapshot-8+-1.8.0.jar`

> [!IMPORTANT]
> Non eseguire il comando PowerShell senza apici doppi sugli argomenti `-P`, altrimenti PowerShell divide l'argomento sul carattere punto (`.`).
> **Non cancellare mai i JAR storici** presenti in `Jar/`.

### Esecuzione della Suite di Verifica (`gradlew check`)
Il file `gradle/verification.gradle.kts` registra 13 runner di verifica che testano l'integrità del catalogo, dei codec, delle ricette, della matematica e della compatibilità:
```powershell
./gradlew.bat check --console=plain
```
I controlli validano:
* `verifyAdvancedEnchantingTableRecipe`
* `verifyAdvancedInfusingCosts`
* `verifyBloodyMaryLoot`
* `verifyBloodyMaryLootingChance`
* `verifyCreativeEnchantmentCategories`
* `verifyEnchantmentResourceCatalog` (convalida tutti i 119 incantamenti)
* `verifyEnchantmentScaling`
* `verifyMissingEnchantmentItems`
* `verifySecondaryEnchantmentDamage`
* `verifySurvivalEnchantmentMath`
* `verifySurvivalEnchantmentResources`
* `verifyWeaponEnchantmentDamage`
* `verifyWorldConfigCodec`

---

## 6. Documentazione e Wiki Ufficiale (`docs/wiki/`)

La documentazione completa per gli utenti è mantenuta direttamente in Markdown sotto `docs/wiki/`:
* `Home.md`: punto d'ingresso e guida rapida.
* `Enchantments.md`: catalogo completo tabellare dei 119 incantesimi.
* `Per-oggetto.md`: suddivisione per tipologia di armatura/strumento/arma.
* `Tavolo-avanzato.md`: guida alla costruzione della formazione 5×5 e progressione XP.
* `Comandi-e-gamerule.md`: guida dettagliata alla sintassi `/eldrich`.
* `Compatibilita.md`, `Incudine.md`, `Ottenimento.md`, `Rituali.md`, `Vanilla.md`, `Versione-1.8.0.md`.
* Schede dedicate per ciascun incantesimo: `Incantamento-<nome>.md`.
