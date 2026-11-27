# Eldritch Surge 2.0.0 — Prima Release Pubblica

[Home](Home) · [Catalogo](Enchantments) · [Tavolo avanzato](Tavolo-avanzato) · [Comandi e gamerule](Comandi-e-gamerule)

**Minecraft 26.3 / 26.4 Snapshot 8 · Fabric · 119 incantamenti nel catalogo**

> Questa è la **prima release pubblica** di Eldritch Surge. Il mod è stato sviluppato e testato internamente fino alla versione 1.9.1.1; la 2.0.0 rappresenta il primo rilascio ufficiale per la comunità.

## Novità rispetto alla 1.8.0

La versione 1.8.0 ha introdotto: conservazione Soulbond al respawn, comandi `/eldrich`, persistenza dei reagenti, costi XP scalati per rarità e supporto a 26.4 Snapshot 8. Consulta [Novità 1.8.0](Versione-1.8.0) per il dettaglio completo.

La 2.0.0 è una release di stabilizzazione e pubblicazione che:

- **Convalida completa:** tutte le verifiche automatiche (`gradlew check`) passano su 26.3 — 13 runner che coprono catalogo, codec, ricette, matematica degli incantesimi, bilanciamento e integrità delle risorse.
- **Catalogo definitivo:** 119 incantamenti registrati, con 15 categorie per effetto, 13 categorie tassonomiche per mob e 119 schede wiki dedicate.
- **Documentazione completa:** wiki strutturata con 40+ pagine, sidebar organizzata, footer aggiornato e collegamenti al sorgente.
- **Jar duali:** build separate per Minecraft 26.3 e 26.4 Snapshot 8, entrambe con sources.
- **Compatibilità verificata:** EnchLib (opzionale), Mod Menu, Cloth Config e YACL testati.

## JAR disponibili

| Minecraft | File |
|---|---|
| 26.3 | `eldrich-surge-26.3+-2.0.0.jar` |
| 26.4 Snapshot 8 | `eldrich-surge-26.4-snapshot-8+-2.0.0.jar` |

## Come installare

1. Installa **Fabric Loader** per Minecraft 26.3 o 26.4 Snapshot 8 (Java 25 richiesto).
2. Aggiungi **Fabric API** e **Fabric Language Kotlin** compatibili.
3. Posiziona il JAR di Eldritch Surge nella cartella `mods`.
4. (Opzionale) Aggiungi **Mod Menu** + **Cloth Config** o **YACL** per la schermata di configurazione in gioco.
5. (Opzionale) Installa **EnchLib** separatamente per le integrazioni estese.

## Note

- Eldritch Surge e EnchLib sono mod **separate**; EnchLib non è incluso nel download.
- Ogni mondo conserva le proprie impostazioni di configurazione in `eldrich-surge/enchantment_caps.json`.
- I comandi `/eldrich` e `/eldritch` richiedono **livello operatore 2+**.
- Licenza **MIT**.

## Versioni precedenti

- [1.8.0](Versione-1.8.0) · [1.7.1](Versione-1.7.1) · [1.7.0](Versione-1.7.0)
