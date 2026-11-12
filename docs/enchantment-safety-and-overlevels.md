# Audit degli incantesimi e livelli oltre il massimo

## Progressione

Il livello scritto sull'oggetto resta invariato. Non cambiano i massimi di acquisizione di tavoli, incudine o configurazione EnchLib.
Il massimo originale `M` viene dal catalogo `ModEnchantmentDefinitions`, non dal massimo personalizzato: aumentare il limite nell'editor non sposta la soglia della curva.

Per un incantesimo della mod con massimo originale maggiore di uno:

```text
L <= 0: forza = 0
1 <= L <= M: forza = L
L > M: forza = M + ln(1 + L - M)
```

Gli incantesimi a livello unico restano effetti di attivazione. Gli incantesimi vanilla non vengono riscalati globalmente.
Gli attributi continui, il volo, Elasticity e gli effetti numerici nei JSON conservano la parte frazionaria. I percorsi che richiedono livelli interi usano l'arrotondamento all'intero piu vicino: possono quindi esserci piccoli intervalli senza un nuovo incremento visibile.
La conversione avviene una volta per oggetto, prima di sommare i contributi dell'equipaggiamento. Quattro oggetti ai rispettivi massimi ordinari non vengono trattati come un singolo oggetto fuori scala.

Armored conserva il valore effettivamente implementato prima di questa modifica: **4 punti armatura per livello**. La descrizione preesistente che ne indica 2 non corrisponde al codice; non e stata usata per cambiare il bilanciamento ordinario.

| Armored | Forza effettiva | Bonus armatura |
| --- | ---: | ---: |
| I | 1 | 4 |
| V | 5 | 20 |
| VI | 5,6931 | 22,7726 |
| XXVIII | 8,1781 | 32,7122 |

Questi sono i bonus del modificatore: il valore finale resta soggetto agli altri modificatori e ai limiti dell'attributo Minecraft.

## Copertura

- `EnchantmentLevels`: punto comune per gli effetti Java, con protezione da oggetti nulli/vuoti.
- `SpellCastMechanics`: conversione separata dei livelli memorizzati nei libri rituali.
- `LogarithmicLevelValue`: curva negli effetti JSON di Katana, Impaler Reach, Smithcrafts, Bane of End, Undead Slayer, Curse of Target e Replenish. Non viene applicata una seconda conversione a questi effetti.
- Effetti con vecchi limiti fissi: estensione oltre il massimo per Dodge, Fatigue, Happy Boost, Step Assist, Kinetic Protection, Welding e i principali effetti passivi delle armature. I vecchi limiti degli effetti passivi su piu pezzi restano invariati quando tutti i pezzi hanno livelli ordinari.
- Forged mantiene il tiro originale di 1/3 a I-V; oltre V aggiunge 5 punti percentuali per punto di forza eccedente, fino al 95%.
- Dual Sweeping mantiene il moltiplicatore 2 a I-II; oltre II aggiunge 0,5 per punto di forza eccedente.
- Sea Breeze mantiene la carica originale a I-III; oltre III aggiunge una spinta proporzionale alla forza eccedente, con raggio limitato.
- Le probabilita gia sature, gli effetti binari e i limiti fisici non vengono fatti crescere oltre valori validi solo per mostrare un numero piu alto.

## Problemi individuati e protezioni

| Percorso | Rischio individuato nel codice | Correzione |
| --- | --- | --- |
| Proiettili senza arma | `ItemStack` nullo durante la lettura degli incantesimi | Livello zero, velocita originale; niente recupero dalle mani del proprietario quando manca l'arma |
| Dwarf Flakes / Sea Flakes | `4800 / livello` puo dare zero, seguito da resto per zero | Intervallo minimo di un tick, oltre alla curva logaritmica |
| Trench Spell | Scavo sincrono di un volume enorme: rischio blocco del tick/watchdog | Coda sul thread server, 2048 posizioni per tick complessive, massimo 16 lavori, raggio massimo 32 |
| Danni ed esplosioni secondari | Possibile riattivazione dei bonus e degli effetti offensivi della mod | Guardia per thread con pulizia in `finally`; i danni secondari non avviano altre catene offensive della mod |
| Bonus temporanei delle frecce | Ripristino solo su `TAIL`, esclusi i ritorni anticipati | Ripristino a ogni `RETURN` e prima di iniziare un nuovo impatto |
| Loot aggiuntivo | Moltiplicazioni intere e cicli di creazione potenzialmente enormi | Aritmetica `long`, stack vuoti esclusi, massimo 512 stack bonus per lista prodotta |
| Replenish | Risultato munizione o tiratore mancante | Controlli null prima dell'uso |
| Seeker Blessing | Query spaziali troppo ampie e riferimenti conservati fra sessioni | Raggio massimo 64; pulizia delle mappe allo stop del server |
| End Adaptability | Ricerca di una destinazione con possibile caricamento di chunk | Ricerca limitata ai chunk gia caricati |
| Forged / Fragility | Somma del danno di durabilita soggetta a overflow | Somma in `long` con saturazione prima della conversione |
| Attributi | Modificatori non finiti propagati allo stato dell'entita | Rifiuto di NaN/infinito |
| Cocktail Spell | Iterazione sugli effetti mentre vengono riapplicati | Iterazione su una copia della collezione |

Trench ora procede gradualmente anche ai livelli normali: forma e profondita previste restano quelle originali nei chunk caricati, ma non carica nuovi chunk e rispetta il bordo del mondo. Quando la coda e piena, il libro non viene consumato e puo riprovare; i lavori non terminati non sono persistiti dopo uno stop del server.
Le protezioni sui danni riguardano le catene generate dalla mod; non costituiscono una garanzia contro ogni interazione possibile con altre mod.

## Verifica

- Compilati tutti i sorgenti Java principali e di test con Java 25 e le dipendenze Minecraft 26.3 locali; usate le classi Kotlin gia compilate.
- Eseguiti i 13 programmi di verifica dichiarati in `gradle/verification.gradle.kts`.
- Test della curva per tutte le 118 definizioni: livelli ordinari invariati, valori negativi, massimo + 1, 28 livelli aggiuntivi, 255, 32767 e `Integer.MAX_VALUE`; coerenza dei massimi JSON/catalogo, codec diretto dei nuovi valori e bonus Armored XXVIII.
- Test delle letture null/vuote, delle formule Survival e della guardia contro la ricorsione, anche dopo un'eccezione.
- Verifiche esistenti di risorse, loot, ricette, configurazione, costi e categorie creative.

Il comando Gradle completo non e stato eseguito per il blocco di scrittura sulla cache utente. Non sono stati eseguiti un avvio Fabric, il caricamento completo dei registri con il nuovo tipo di valore, l'applicazione runtime dei mixin o una prova multiplayer. Prima di distribuire una release vanno provati in un mondo di test: OminousItemSpawner, Armored V/XXVIII, frecce con impatti anticipatamente rifiutati, riparazioni fuori scala, Trench simultanei e combinazioni di danni secondari.

Nessun JAR o incremento di versione fa parte di questa modifica.
