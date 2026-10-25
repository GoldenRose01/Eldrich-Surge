# 📚 Fonti e manutenzione

[Home](Home) · [Catalogo](Enchantments)

Questa wiki documenta il checkout di Eldrich Surge **1.6.2 / Minecraft 26.3**, verificato il **25 ottobre 2026**. Non assegna a una meccanica una garanzia di test in gioco soltanto perché compare nel catalogo.

## Origine delle informazioni

- Catalogo registrato: `ModEnchantmentDefinitions.ALL` — 104 voci.
- Livelli, slot, peso e componenti dati: JSON sotto `data/eldrich-surge/enchantment`.
- Regole configurabili degli oggetti e descrizioni: classi sotto `enchantment/ench`.
- Effetti personalizzati: meccaniche, calcolatore dei danni e mixin.
- Gruppi: tag sotto `data/eldrich-surge/tags/enchantment`.

Ogni scheda collega definizione Java e datapack. Le spiegazioni runtime integrano le descrizioni storiche dove sono state verificate divergenze. Le sezioni «effetto dichiarato» indicano invece ciò che descrive il progetto; il riquadro tecnico mostra gli effetti dati effettivamente presenti.

## Limiti noti

Alla generazione risultavano due controlli dei dati non superati: il test del catalogo si aspettava ancora Swiftness anche sulle balestre; il test dei bonus delle armi si aspettava una struttura JSON non presente. La wiki segue i dati e i controlli runtime correnti, senza correggere implicitamente le meccaniche.

Le pagine sono in italiano, con nomi e ID originali per facilitare ricerca e comandi. I collegamenti al sorgente puntano a `main` e possono evolvere dopo la pubblicazione.

## Riferimento editoriale

La struttura a indice, tabelle per oggetto, ottenimento e incompatibilità prende come riferimento [Enchantment — Minecraft Wiki](https://minecraft.wiki/w/Enchantment). I testi dei progetti sono stati scritti dai loro sorgenti; non viene riprodotto l’articolo della Minecraft Wiki.

## Aggiornamento

Il generatore in `external_tools/wiki/build.py` ricostruisce catalogo e schede dalle definizioni, applicando le spiegazioni editoriali di `effects.json`. Le guide tematiche sono mantenute in `docs/wiki/`. Pubblicare la wiki non richiede incrementare la versione né produrre un JAR.
