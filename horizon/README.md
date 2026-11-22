# Eldrich Surge per Horizon

Anteprima per Horizon Nightly 26.3 del 22 novembre 2026, build `2b77287`. Include Katana, Smithcrafts e Impaler Reach. I livelli 1–255 usano gli stessi valori lineari e la stessa attenuazione logaritmica oltre il massimo previsto dalle definizioni Fabric.

Copia il file `.hmod` in `horizon-mods/` e aggiungi `mod.eldrich_surge:true` in `horizon-mods.txt`, accanto alla cartella. In multiplayer installa la stessa versione su Horizon client e server.

Su un mondo di test, gli operatori possono ottenere una spada con Katana a livello 28 con:

```mcfunction
/give @s minecraft:iron_sword[minecraft:enchantments={"eldrich-surge:katana":28}]
```

Questa build copre tre incantesimi. Il tavolo avanzato, gli altri incantesimi, le ricette di incudine, l'integrazione con le configurazioni EnchLib e i loro percorsi di acquisizione restano da portare. Per evitare di perdere contenuti, usa un mondo di test separato da quelli che contengono contenuti della versione Fabric.

## Build

Da `horizon/`, esegui `python3 tools/prepare_data.py`, `cargo horizon-mod build` e `cargo horizon-mod check target/horizon-mod/eldrich_surge.hmod`.
