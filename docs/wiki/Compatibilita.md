# ⚖️ Compatibilità e gruppi

[Home](Home) · [Catalogo](Enchantments) · [Incudine](Incudine)

## Come leggere le incompatibilità

Il datapack dichiara `exclusive_set`; le impostazioni del mondo aggiungono regole esplicite per coppie di incantamenti. Il risultato dipende dal sistema che consulta tali regole. Un’etichetta di gruppo non autorizza a ignorare le eccezioni delle fusioni.

## Gruppi dichiarati nei tag del progetto

| Tag | Componenti |
| --- | --- |
| `eldrich-surge:additional_damage` | [Blade of Apocalypse](Incantamento-blade-of-apocalypse), [Comet Slam](Incantamento-comet-slam), [Gream Reaper](Incantamento-gream-reaper), [Heavy Impact](Incantamento-heavy-impact), [Katana](Incantamento-katana), [Star Fate](Incantamento-star-fate), `minecraft:breach`, `minecraft:density`, `minecraft:impaling`, `minecraft:wind_burst` |
| `eldrich-surge:aspect` | [Heartseeker](Incantamento-heartseeker), `minecraft:fire_aspect` |
| `eldrich-surge:blessings` | [Blessing of the Night](Incantamento-blessing-of-the-night), [End Blessing](Incantamento-end-blessing), [Hell Blessing](Incantamento-hell-blessing), [Sun Blessing](Incantamento-sun-blessing), [Vision Blessing](Incantamento-vision-blessing), [Seeker Blessing](Incantamento-seeker-blessing) |
| `eldrich-surge:falling` | [Soft Falling](Incantamento-soft-falling), [Soil Falling](Incantamento-soil-falling) |
| `eldrich-surge:flakes` | [Dwarf Flakes](Incantamento-dwarf-flakes), [End Flakes](Incantamento-end-flakes), [Hell Flakes](Incantamento-hell-flakes), [Sea Flakes](Incantamento-sea-flakes) |
| `eldrich-surge:forged` | [Dwarf Forged](Incantamento-dwarf-forged), [End Forged](Incantamento-end-forged), [Nether Forged](Incantamento-nether-forged), [Villager Forged](Incantamento-villager-forged) |
| `eldrich-surge:hearts` | [Heart of Depth](Incantamento-heart-of-depth), [Heart of Nether](Incantamento-heart-of-nether), [Heart of the Sea](Incantamento-heart-of-the-sea), [Heart of the Sky](Incantamento-heart-of-the-sky) |
| `eldrich-surge:negative_curses` | [Curse of Fragility](Incantamento-curse-of-fragility), [Curse of the Forest](Incantamento-curse-of-the-forest) |
| `eldrich-surge:offhand` | [Phalanx Stance](Incantamento-phalanx-stance) |
| `eldrich-surge:positive_curses` | [Curse of Perish](Incantamento-curse-of-perish), [Curse of Spider](Incantamento-curse-of-spider), [Curse of Target](Incantamento-curse-of-target) |
| `eldrich-surge:protection` | [God's Protection](Incantamento-god-protection), `minecraft:protection`, `minecraft:fire_protection`, `minecraft:blast_protection`, `minecraft:projectile_protection` |
| `eldrich-surge:rituals` | [Cocktail Spell](Incantamento-cocktail-spell), [Ragnarok](Incantamento-ragnarok), [Red Moon](Incantamento-red-moon), [Storm Spell](Incantamento-storm-spell), [Trench Spell](Incantamento-trench-spell) |
| `eldrich-surge:weapon_utility` | [Gravity Well](Incantamento-gravity-well), [Impaler Reach](Incantamento-impaler-reach), [Seismic Wave](Incantamento-seismic-wave), [Vanguard Charge](Incantamento-vanguard-charge), `minecraft:sweeping_edge` |

## Limite dei componenti

Il filtro delle offerte blocca un ulteriore incantamento di categoria quando sull’oggetto sono già presenti almeno tre componenti. Le cinque componenti per Blade of Apocalypse o Star Fate vanno preparate secondo le regole dei libri e delle fusioni all’incudine; il limite del tavolo non descrive da solo ogni percorso di combinazione.

## Eccezioni e impostazioni

EnchLib può fornire categorie di oggetti e coppie incompatibili. Le coppie impostate con la sua API sono simmetriche. Forzare un incantamento tramite comandi non dimostra che l’oggetto sia ottenibile nei tavoli o che il suo effetto si attivi.

[Fonte: filtro dei tavoli](https://github.com/GoldenRose01/Eldrich-Surge/blob/main/src/main/java/eldritch/surge/mixin/EnchantmentMenuMixin.java)
