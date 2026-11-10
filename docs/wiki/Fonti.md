# 📚 Fonti e manutenzione

[Home](Home) · [Catalogo](Enchantments)

Questa wiki documenta Eldrich Surge **1.7.1**, con JAR per **Minecraft 26.3 e 26.4 Snapshot 7**. Documentazione aggiornata il **10 novembre 2026**. I risultati di compilazione e controllo riportati si riferiscono ai JAR preparati il 7 novembre; non equivalgono a prove in gioco.

## Origine delle informazioni

- Catalogo registrato: `ModEnchantmentDefinitions.ALL` — 118 voci.
- Livelli, slot, peso e componenti dati: JSON sotto `data/eldrich-surge/enchantment`.
- Regole configurabili degli oggetti e descrizioni: classi sotto `enchantment/ench`.
- Effetti personalizzati: meccaniche, calcolatore dei danni e mixin.
- Gruppi: tag sotto `data/eldrich-surge/tags/enchantment`.

Ogni scheda collega definizione Java e datapack. Le spiegazioni runtime integrano le descrizioni storiche dove sono state verificate divergenze. Le sezioni «effetto dichiarato» indicano invece ciò che descrive il progetto; il riquadro tecnico mostra gli effetti dati effettivamente presenti.

## Limiti noti

I controlli completi sono passati su 26.3. Sullo snapshot 26.4 Snapshot 7 la compilazione è riuscita, ma manca la testa Bloody Mary per `minecraft:frostbite`. I nuovi pannelli dei dettagli richiedono ancora una verifica visiva in gioco.

Le pagine sono in italiano, con nomi e ID originali per facilitare ricerca e comandi. I collegamenti al sorgente puntano a `main` e possono evolvere dopo la pubblicazione.

## Riferimento editoriale

La struttura a indice, tabelle per oggetto, ottenimento e incompatibilità prende come riferimento [Enchantment — Minecraft Wiki](https://minecraft.wiki/w/Enchantment). I testi dei progetti sono stati scritti dai loro sorgenti; non viene riprodotto l’articolo della Minecraft Wiki.

## Aggiornamento

Il generatore in `external_tools/wiki/build.py` ricostruisce catalogo e schede dalle definizioni, applicando le spiegazioni editoriali di `effects.json`. Le guide tematiche sono mantenute in `docs/wiki/`. Pubblicare la wiki non richiede incrementare la versione né produrre un JAR.
