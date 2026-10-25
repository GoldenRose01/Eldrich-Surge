# ⚒️ Incudine e fusioni

[Home](Home) · [Compatibilità](Compatibilita) · [Catalogo](Enchantments)

## Ricette speciali

I libri devono contenere **esattamente** gli incantamenti indicati: un incantamento estraneo impedisce la ricetta. Le cinque componenti devono trovarsi nello stesso libro a sinistra.

| A sinistra | A destra | Risultato | Costo |
|---|---|---|---|
| Libro: Butcher, Undead Slayer, Bane of End, Exorcist, Wrath of the Abyss | Stella del Nether | Blade of Apocalypse I | 30 livelli |
| Libro: Witch Hunter, Creeping Threat, Flogging, Herbicide, Smoother | Stella del Nether | Star Fate I | 30 livelli |
| Libro con il solo Katana | Stella del Nether | Gream Reaper allo stesso livello, massimo X | 30 livelli |
| Libro con il solo Blade of Apocalypse | Libro con il solo Star Fate | Gream Reaper con la somma dei livelli, massimo X | 30 livelli |
| Libro con il solo Unbreaking III | Libro con il solo Unbreaking III | DeathBreak I | Costo dell’incudine, almeno 1 |

Blade e Star Fate possono essere scambiati fra sinistra e destra. Il costo fisso di 30 appartiene a `SpecialAnvilRecipes`, non alla trasformazione dei due libri Unbreaking.

## Limite dei componenti

Il filtro delle offerte dei tavoli non aggiunge componenti quando l’oggetto ne ha già tre. L’incudine blocca un risultato **che non è un libro incantato** se aggiungere componenti aumenta il totale oltre tre. I libri sono esentati da questo controllo, permettendo di prepararne cinque per la fusione.

## DeathBreak e riparazioni

DeathBreak imposta il componente di indistruttibilità e rimuove Unbreaking quando applicato all’incudine. [Welding](Incantamento-welding) riduce il costo delle operazioni che riparano effettivamente l’oggetto: `max(1, ceil(costo / (4 × min(livello, 5))))`.

Una ricetta speciale viene valutata prima della gestione ordinaria del risultato. Per applicare il libro ottenuto rimangono rilevanti oggetto supportato e regole del mondo.

Fonti: [SpecialAnvilRecipes](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpecialAnvilRecipes.java), [AnvilEnchantmentsMixin](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/AnvilEnchantmentsMixin.java).
