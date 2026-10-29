# Fishing, shield, defense and water enchantments

| Enchantment | Items / levels | Behavior |
| --- | --- | --- |
| Fanatic | Fishing rod, I | Each completed catch adds one percentage point to treasure chance. The counter belongs to the rod, persists in its custom data, preserves unrelated custom data, and caps at 100. Empty casts and hooking entities do not count. The tooltip displays the accumulated bonus. |
| Dodge | Leggings, I–III | Cancels an eligible damage event with probability 12.5%, 25%, or 33%, preserves the normal damage cooldown and displays a brief Dodge confirmation. |
| Fuse Cutter | Shield, I | Cancels the whole explosion when its center is at most three blocks from a player actively blocking with the enchanted shield. |
| Immortality | Shield, I | A held shield saves its owner at the lethal-damage stage, leaving one heart and removing only Immortality. Does not require blocking. Vanilla damage that bypasses invulnerability (such as `/kill`) still bypasses death protection. |
| Curse of Slippery | Swords, axes, pickaxes, shovels, hoes, spears, tridents, bows, crossbows, shields, fishing rods; I | Each cursed held stack has a 1/660 drop chance per server tick: 33 seconds on average at 20 TPS. Applies to either hand and drops the complete stack with its components intact. |
| Curse of Incompatibility | Durability-bearing equipment, I | Rejects pairing with every other enchantment through the standard compatibility check, including anvils and the advanced table. Does not erase enchantments forcibly applied with commands. |
| Curse of Fatigue | Durability-bearing equipment, I–III | Action speed is multiplied by 0.8 per level (20%, 36%, 48.8% slower), affecting mining, attack cooldown, held-item charging and swing animation. Four armor slots contribute up to twelve levels, multiplying movement speed by 0.95 per total level (45.964% slower at twelve). Incompatible with Efficiency. |
| Curse of Corrosion | Armor, I | While touching water or exposed to rain, applies 0.25 health points (1/8 heart) of magic damage every 60 ticks. Multiple pieces do not multiply the damage. Normal damage defenses still apply. |
| Blessing of the Sea | Armor, I | In the same conditions and interval, heals 0.25 health points once and repairs each blessed armor piece by one durability point. These values are defaults selected where the request did not specify amounts. |
| Accuracy Shot | Bow, I | Removes shot spread and gravity. Arrows disappear after 600 ticks (30 seconds at 20 TPS), including embedded arrows; the expiry is saved so reloading does not restart it. Uses the bow stored on the projectile, not an item switched into the shooter's hand later. Aims along the crosshair; does not home onto targets. |
| Fear | Sword, I | A successful direct player hit adds 20 ticks to the creeper's fuse length. Repeated hits stack; vanilla stores the resulting fuse in saved entity data. Capped at 32,767 ticks to prevent overflow in vanilla's short-valued fuse field. |
| Kinetic Protection | Elytra, I–V | Adds 6 armor points per level while gliding (30 at V). Wall collision damage is additionally reduced by 24% per level, capped at 80% (standard protection-style cap). The armor modifier is removed after flight or unequipping. |
| Happy Boost | Elytra, I–III | Multiplies gliding displacement by 1.5 / 1.8 / 2.1 on both client and server. Does not repeatedly multiply stored velocity, avoiding exponential acceleration. |
| Step Assist | Boots, I–III | Adds 0.4 to vanilla step height at every level: automatic climbing stops at one block. Higher obstacles require pressing jump; III adds 0.05 jump strength to clear 1.5 blocks. Uses synchronized attributes and removes both bonuses when unequipped. Other mods can still contribute their own modifiers. |

Fanatic accounts for the vanilla fishing weights and Luck of the Sea. Its bonus can grant treasure outside open-water conditions, including guaranteed treasure at the cap. Custom datapacks that replace fishing pool weights may change the exact additive probability below the cap.

Neptune’s Will now also adds 5 damage per level to thrown tridents, using the projectile’s saved weapon. Held weapons do not add this bonus again to projectile hits.

## Verification

`verifySurvivalEnchantmentMath` checks probabilities, additive fishing chance, armor stacking, flight speed, step height and impact reduction. `verifySurvivalEnchantmentResources` decodes all fourteen resources with Minecraft's codec and resolves supported vanilla/mod item tags and translations.

Manual gameplay checks still recommended:

- Fish with two rods, retry empty casts, hook an entity, relog and transfer rods; verify separate persistent counters and guaranteed treasure at the cap.
- Compare Dodge at all levels against melee, projectiles, fall and environmental damage.
- Trigger explosions just inside/outside three blocks with the shield lowered, raised during its activation delay, and fully blocking; inspect nearby blocks and entities.
- Receive lethal damage holding Immortality in either hand; verify one heart, the remaining shield enchantments and consumption of only one rescue.
- Measure cursed tool mining, bow/crossbow charging and swing speed; equip/remove all four cursed armor pieces and check movement recovery.
- Observe Slippery in either hand; recover the dropped item and inspect its durability/enchantments/counter.
- Verify incompatibilities in both anvil input orders and in normal/advanced enchanting.
- Test water, exposed rain, a roof and dry weather with Corrosion and Blessing; verify intervals, health and repair.
- Fire Accuracy Shot at a distant crosshair target, change held weapons, and reload while an arrow is embedded; verify straight flight and expiry without a timer reset.
- Strike an ignited creeper with Fear and compare its explosion delay with an unenchanted sword; arrows and failed hits must not extend it.
- Equip Kinetic Protection and inspect armor on the ground/in flight, then compare wall collision damage.
- Compare the same flight path at Happy Boost I/II/III and check rocket boosting and landing.
- Walk into full blocks, layered snow and a block plus slab at each Step Assist level; remove the boots and verify normal step height returns.
