# ⌨️ Comandi e gamerule

[Home](Home) · [Configurazione](Configuration) · [Wiki EnchLib](https://github.com/GoldenRose01/enchlib/wiki/Comandi)

## Comandi della mod

La mod mette a disposizione il comando principale **`/eldrich`** (con l'alias **`/eldritch`**) per la gestione e configurazione delle regole globali di gioco. Richiede i permessi di operatore (livello 2 o superiore).

### Sintassi di configurazione opzioni

```text
/eldrich <setting> <on/off>
/eldritch <setting> <on/off>
```

Impostazioni disponibili:
- **`keepLoyaltyTridents`** (`on`/`off`): quando attivo, i tridenti con l'incantesimo Lealtà vengono preservati e restituiti al giocatore anche in caso di morte o se cadono nel vuoto.
- **`sweepingRequiresEnchantment`** (`on`/`off`): quando attivo, l'attacco a spazzata delle spade richiede obbligatoriamente la presenza dell'incantesimo Sweeping Edge.
- **`lethalPoison`** (`player`/`mob`/`all`/`off`): controlla se il danno periodico dell'effetto Veleno può azzerare la salute ed eliminare l'entità invece di fermarsi a mezzo cuore. Può essere limitato ai soli giocatori (`player`), ai mob (`mob`), a tutte le entità (`all`) o disattivato (`off`).

Esempi:
```text
/eldrich keepLoyaltyTridents on
/eldrich sweepingRequiresEnchantment on
/eldrich lethalPoison all
```

## Gamerule della mod

Le seguenti gamerule personalizzate governano i sistemi runtime del mod e possono essere consultate e modificate con il comando vanilla `/gamerule`:

| ID completo | Predefinito | Descrizione e funzionamento |
|---|---|---|
| `eldrich-surge:keep_loyalty_tridents` | `false` | Se abilitata, memorizza e ripristina i tridenti lanciati con Lealtà prevenendone la perdita nel vuoto o alla morte del giocatore. |
| `eldrich-surge:sweeping_requires_enchantment` | `true` | Se abilitata, disabilita il colpo a spazzata vanilla sulle spade a meno che l'arma non possieda Sweeping Edge. |
| `eldrich-surge:pvp_enchantment_modifier` | `1.0` | Moltiplicatore globale applicato ai bonus di danno degli incantesimi quando si infligge danno PvP ad altri giocatori. |
| `eldrich-surge:lethal_poison` | `false` | Se abilitata, permette al veleno di infliggere danno letale eliminando le entità colpite. |

Esempi:
```text
/gamerule eldrich-surge:pvp_enchantment_modifier 0.5
/gamerule eldrich-surge:lethal_poison true
/gamerule eldrich-surge:keep_loyalty_tridents true
```

Fonti: [comandi](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/command/EldritchCommands.java), [gamerule](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/game/EldritchGameRules.java).
