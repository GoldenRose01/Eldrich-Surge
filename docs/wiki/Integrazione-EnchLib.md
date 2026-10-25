# 🔗 Integrazione EnchLib

[Home](Home) · [Configurazione](Configuration) · [Wiki EnchLib](https://github.com/GoldenRose01/enchlib/wiki)

## Cosa cambia installandola

Eldrich Surge registra in EnchLib metadati, categorie degli oggetti, categorie bersaglio, incompatibilità e preset dei tavoli. Le due mod mantengono runtime e distribuzione separati; EnchLib non è incorporato nel JAR di Eldrich Surge.

| Sistema | Senza EnchLib | Con EnchLib compatibile |
|---|---|---|
| Categorie dei mob | Tag di fallback di Eldrich Surge | Assegnazioni del mondo tramite API |
| Cap e regole dei tavoli | Configurazione di Eldrich Surge | Valori sincronizzati dal ponte EnchLib |
| Editor di EnchLib | Non disponibile | Impostazioni dei contenuti rilevati dal registry |
| Meccaniche degli incantamenti | Codice e datapack di Eldrich Surge | Restano nel codice e nei datapack di Eldrich Surge |

## Categorie multiple

`animals`, `magik`, `undead`, `rebel`, `fungi`, `hell`, `void`, `water`, `arthropods`, `cubic` e `flying` sono categorie, non dimensioni. Un mob può averne più di una. I bonus vengono calcolati per le corrispondenze previste: Blade of Apocalypse e Star Fate non scelgono soltanto la prima categoria.

Per esempio, un’entità classificata sia `undead` sia `hell` può contribuire due volte al calcolo di Blade. Cambiare una categoria in EnchLib modifica il bersaglio della regola senza richiedere una modifica alla mod del mob.

## Autorità del server

Le impostazioni del mondo appartengono al server. Le modifiche client sono richieste, accettate soltanto quando i permessi lo consentono. Un editor visualizzato correttamente non garantisce che una mod esterna consumi ogni proprietà configurabile: il ponte di Eldrich Surge copre i punti esplicitamente integrati.

Fonti: [EnchLibIntegration](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/EnchLibIntegration.java), [EnchLibMobCategories](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/EnchLibMobCategories.java).
