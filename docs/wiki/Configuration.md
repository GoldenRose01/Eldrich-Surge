# ⚙️ Configurazione del mondo

[Home](Home) · [Comandi](Comandi-e-gamerule) · [Integrazione EnchLib](Integrazione-EnchLib)

## Dove vengono salvati i valori

| Ambito | Percorso |
|---|---|
| Preset globale dell’istanza | `config/eldrich-surge/enchantment_caps.json` |
| Mondo attivo | `<mondo>/eldrich-surge/enchantment_caps.json` |
| EnchLib, se installata | `<mondo>/config/enchlib/` |

Il server carica le impostazioni del salvataggio e invia il valore attivo ai client. Non confondere i due percorsi del mondo: Eldrich Surge e EnchLib conservano i propri file.

## Proprietà configurabili

- Disponibilità nei tavoli normale e avanzato.
- Idoneità al loot, nei percorsi che consultano la proprietà.
- Massimo per incudine e massimo per tavolo; zero indica il valore registrato nei percorsi dei cap.
- Stato abilitato, probabilità dei tavoli, classe di rarità, regole degli oggetti e incompatibilità ricevute dal ponte EnchLib compatibile.

La configurazione globale prepara i predefiniti; il file del mondo conserva le scelte del salvataggio. Per sapere se un valore cambia un particolare sistema, verifica che quel sistema lo consulti: abilitare «loot» non aggiunge automaticamente una tabella di bottino.

## Permessi

Le richieste di modifica delle impostazioni del mondo richiedono `COMMANDS_GAMEMASTER`, normalmente **operatore di livello 2 o superiore**. Il server controlla i permessi anche se il client invia direttamente una richiesta. Il comando `/surge` ha un registrar distinto: vedi la sua pagina per il comportamento attuale.

## Editor in gioco

Installa Mod Menu e un provider supportato, Cloth Config oppure YACL. Se entrambi sono presenti, puoi scegliere la schermata. Le due interfacce usano le stesse impostazioni e gli stessi controlli del server.

## Configuratore esterno

Nel checkout di sviluppo il configuratore Windows è in `external_tools/config-tool/config.exe`, insieme alle sue dipendenze e icone. Modifica il file `run/config/eldrich-surge/enchantment_caps.json` e legge le risorse del progetto: non è un editor universale di qualsiasi salvataggio installato altrove.

## EnchLib

La wiki corrente documenta EnchLib 1.6.1. Le vecchie note che indicavano un’API 1.5.0 ancora da pubblicare non descrivono il checkout attuale. Il ponte registra e legge soltanto le API disponibili, mantenendo le due mod separate.

Fonti: [EnchantmentCapsConfig](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/config/EnchantmentCapsConfig.java), [WorldEnchantConfigSync](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/network/WorldEnchantConfigSync.java).
