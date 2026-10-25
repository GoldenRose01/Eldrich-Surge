# ⌨️ Comandi e gamerule

[Home](Home) · [Configurazione](Configuration) · [Wiki EnchLib](https://github.com/GoldenRose01/enchlib/wiki/Comandi)

## Comando della mod

La sintassi è **`/surge <incantamento> <livello>`**, senza spazi nel nome dell’ID. Esempi:

```text
/surge eldrich-surge:katana 5
/surge eldrich-surge:soft_falling 1
```

Il comando usa l’oggetto nella mano principale, richiede un giocatore e un oggetto non vuoto, offre completamento per gli ID del registry e accetta un intero non negativo. Nel registrar corrente non è presente un vincolo `.requires` sul comando: non va documentato come riservato automaticamente agli operatori. È un’applicazione diretta, distinta dai filtri dei tavoli.

## Gamerule della mod

| ID completo | Predefinito | Regola |
|---|---|---|
| `eldrich-surge:keep_loyalty_tridents` | `false` | Regola registrata per i tridenti Loyalty; nessun utilizzo operativo trovato nei sorgenti controllati |
| `eldrich-surge:sweeping_requires_enchantment` | `true` | Regola registrata per il fendente; nessun utilizzo operativo trovato nei sorgenti controllati |
| `eldrich-surge:pvp_enchantment_modifier` | `1.0` | Moltiplica i bonus di danno che passano da `applyPvpModifier` contro i giocatori |
| `eldrich-surge:lethal_poison` | `false` | Regola registrata per il veleno; nessun utilizzo operativo trovato nei sorgenti controllati |

```text
/gamerule eldrich-surge:pvp_enchantment_modifier 0.5
/gamerule eldrich-surge:lethal_poison true
```

Il modificatore PvP non è un interruttore universale di ogni danno del gioco: si applica ai contributi che il codice instrada nel suo metodo.

Fonti: [comandi](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/command/EldritchCommands.java), [gamerule](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/game/EldritchGameRules.java).
