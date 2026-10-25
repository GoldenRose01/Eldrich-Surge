# ⛏️ Rapporti con gli incantamenti vanilla

[Home](Home) · [Catalogo](Enchantments) · [Catalogo preset EnchLib](https://github.com/GoldenRose01/enchlib/wiki/Incantamenti)

| Vanilla | Comportamento nel percorso di Eldrich Surge |
|---|---|
| Sharpness | Offerte sostituite con Katana |
| Smite | Offerte sostituite con Undead Slayer |
| Bane of Arthropods | Offerte sostituite con Creeping Threat |
| Piercing | Rimane `minecraft:piercing`; override datapack con massimo X e supporto balestra |
| Unbreaking III | Due libri puri producono DeathBreak I all’incudine |

Sharpness, Smite e Bane of Arthropods vengono esclusi dai percorsi di disponibilità della configurazione della mod; i loro ID vanilla non vengono cancellati dal registry. Oggetti già esistenti o comandi possono quindi ancora riferirsi agli ID vanilla.

La logica di Smite può essere estesa ai mob `undead` di EnchLib non già coperti dal tag vanilla, evitando un secondo bonus per quelli già gestiti da Minecraft.

Clearmind e il vecchio duplicato moddato di Piercing non appartengono ai 104 incantamenti registrati nel sorgente documentato. Per descrizioni delle meccaniche vanilla generali, consulta [Minecraft Wiki](https://minecraft.wiki/w/Enchantment).

Fonti: [filtro e sostituzioni](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java), [Piercing](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/resources/data/minecraft/enchantment/piercing.json).
