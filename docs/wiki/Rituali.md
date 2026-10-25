# 📜 Libri magici e rituali

[Home](Home) · [Tavolo avanzato](Tavolo-avanzato) · [Catalogo](Enchantments)

## Preparazione e lancio

1. Inserisci un libro normale nel tavolo avanzato.
2. Usa un frammento di eco come reagente.
3. Seleziona un’offerta valida; per gli slot superiori serve la formazione.
4. Getta il libro magico nel mondo. Il rituale viene esaminato quando il libro raggiunge terra.
5. Dopo un lancio riuscito, l’entità del libro viene consumata.

Cocktail Spell e Storm Spell richiedono che sia disponibile un proprietario vivente del libro; non tutti i metodi di creazione o spostamento di un oggetto a terra conservano questo riferimento.

| Rituale | Effetto |
|---|---|
| [Cocktail Spell](Incantamento-cocktail-spell) | Copia gli effetti attivi del lanciatore sui giocatori entro `8 + 8 × livello` blocchi |
| [Ragnarok](Incantamento-ragnarok) | Evoca quattro cavalieri Wither Skeleton su cavalli scheletrici, equipaggiati |
| [Red Moon](Incantamento-red-moon) | Evoca un’ondata di trenta zombie equipaggiati, con varianti a cavallo |
| [Storm Spell](Incantamento-storm-spell) | Imposta il temporale e genera cinque fulmini entro 32 blocchi |
| [Trench Spell](Incantamento-trench-spell) | Scava un quadrato con raggio `2 + livello`, verso Y −55 o il limite inferiore del mondo |

## Effetti sul mondo

Evocazioni, fulmini e scavo modificano realmente il mondo. Trench Spell non è una semplice animazione; il suo codice evita aria e blocchi indistruttibili. Il consumo avviene dopo il riconoscimento e l’esecuzione del rituale.

I libri «cast spells» identificano i rituali lanciabili; la sola classificazione storica «Ritual» non basta.

Fonte: [SpellCastMechanics](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/enchantment/mechanics/SpellCastMechanics.java).
