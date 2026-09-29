# Piano di test in gioco

La checklist completa è nel [foglio Excel spuntabile](gameplay-test-plan.xlsx). Contiene tutti i 100 incantesimi definiti nei dati, più i 5 aggiunti in Eldritch Surge 1.2.1, con cinque colonne: nome, funzionamento/stato implementazione, esito, problemi da correggere e oggetto da usare nel test. La colonna dell’esito ha una tendina `SI / NO / DA TESTARE`; filtri e intestazione bloccata aiutano a procedere per gruppi.

## Come compilarlo

1. Prova l’incantesimo seguendo il comportamento descritto nella seconda colonna.
2. Se l’effetto è corretto, seleziona `SI`; se non funziona o è errato, seleziona `NO`.
3. Scrivi nella quarta colonna cosa hai fatto, cosa ti aspettavi e cosa è successo. Per `NO`, annota anche eventuali messaggi o errori.
4. Se non hai ancora provato, lascia `DA TESTARE`.

Le righe con esito già compilato riportano soltanto ciò che è stato osservato e riferito finora. `SI` può indicare un funzionamento parziale: la quarta colonna riporta le modifiche ancora richieste. La nota sullo stato tecnico distingue gli incantesimi presenti nel catalogo Java, quelli con effetti dichiarati nei dati e i casi senza un collegamento esplicito per ID nei gestori esaminati. Quest’ultimo rilievo è solo un’indicazione: alcune meccaniche possono essere condivise o generiche, quindi la verifica in gioco resta decisiva.

## Risultati già riportati

- Digger I: funziona; i livelli superiori restano da provare quando saranno disponibili.
- Smelting: funziona correttamente sui minerali.
- Ragnarok, Red Moon e Trench: attivazione osservata; Ragnarok e Red Moon devono distribuire le evocazioni in posizioni casuali vicine.
- Excavator, Theft, Curse of Target sulle frecce, Pop, Sniper, Clearmind, Nineleven ed Elasticity: nessun effetto corretto osservato; le note specifiche sono nelle rispettive righe.
- I libri rituale devono attivarsi quando toccano terra.
