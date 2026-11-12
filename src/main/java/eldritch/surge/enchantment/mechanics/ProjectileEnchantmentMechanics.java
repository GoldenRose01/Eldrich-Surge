package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.util.context.ContextKey;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ProjectileEnchantmentMechanics {
    private static final int MAX_BONUS_STACKS = 512;
    private ProjectileEnchantmentMechanics() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
            applyButcherDrops(context, drops);
            BlockState brokenState = optionalParameter(context, LootContextParams.BLOCK_STATE);
            if (brokenState != null && brokenState.is(BlockTags.CROPS)) {
                Entity breaker = optionalEntity(context, LootContextParams.ATTACKING_ENTITY);
                ItemStack mainHand = breaker instanceof LivingEntity living ? living.getMainHandItem() : ItemStack.EMPTY;
                ItemStack offHand = breaker instanceof LivingEntity living ? living.getOffhandItem() : ItemStack.EMPTY;
                net.minecraft.world.item.ItemInstance contextTool = optionalParameter(context, LootContextParams.TOOL);
                ItemStack tool = contextTool instanceof ItemStack stack ? stack : ItemStack.EMPTY;
                int drainLooting = Math.max(EnchantmentLevels.onItem(context.getLevel(), mainHand, "drain_looting"),
                        EnchantmentLevels.onItem(context.getLevel(), offHand, "drain_looting"));
                drainLooting = Math.max(drainLooting, EnchantmentLevels.onItem(context.getLevel(), tool, "drain_looting"));
                if (drainLooting > 0) {
                    List<ItemStack> cropBonus = new ArrayList<>();
                    for (ItemStack drop : List.copyOf(drops)) {
                        appendBonus(cropBonus, drop, (long) drop.getCount() * 5 * drainLooting);
                    }
                    drops.addAll(cropBonus);
                }
                if (Math.max(EnchantmentLevels.onItem(context.getLevel(), tool, "sickened_of_hell"),
                        Math.max(EnchantmentLevels.onItem(context.getLevel(), mainHand, "sickened_of_hell"),
                                EnchantmentLevels.onItem(context.getLevel(), offHand, "sickened_of_hell"))) > 0) {
                    for (int index = 0; index < drops.size(); index++) {
                        ItemStack drop = drops.get(index);
                        if (drop.is(net.minecraft.world.item.Items.POTATO)) {
                            drops.set(index, new ItemStack(net.minecraft.world.item.Items.BAKED_POTATO, drop.getCount()));
                        } else if (drop.is(net.minecraft.world.item.Items.KELP)) {
                            drops.set(index, new ItemStack(net.minecraft.world.item.Items.DRIED_KELP, drop.getCount()));
                        }
                    }
                }
            }
            Entity directAttacker = optionalEntity(context, LootContextParams.DIRECT_ATTACKING_ENTITY);
            Entity attackingEntity = optionalEntity(context, LootContextParams.ATTACKING_ENTITY);
            AbstractArrow arrow = directAttacker instanceof AbstractArrow directArrow
                    ? directArrow
                    : attackingEntity instanceof AbstractArrow attackingArrow ? attackingArrow : null;
            int level = arrow != null
                    ? projectileLevel(context.getLevel(), arrow, "theft")
                    : attackingEntity instanceof LivingEntity shooter
                    ? Math.max(EnchantmentLevels.onItem(context.getLevel(), shooter.getMainHandItem(), "theft"),
                            EnchantmentLevels.onItem(context.getLevel(), shooter.getOffhandItem(), "theft"))
                    : 0;
            if (level <= 0 || drops.isEmpty()) {
                return;
            }

            List<ItemStack> bonusDrops = new ArrayList<>();
            for (ItemStack drop : List.copyOf(drops)) {
                int extra = context.getRandom().nextInt(level + 1);
                appendBonus(bonusDrops, drop, extra);
            }
            drops.addAll(bonusDrops);
        });
    }

    private static void appendBonus(List<ItemStack> bonus, ItemStack template, long count) {
        if (template == null || template.isEmpty() || template.getMaxStackSize() <= 0) return;
        while (count > 0 && bonus.size() < MAX_BONUS_STACKS) {
            int size = (int) Math.min(count, template.getMaxStackSize());
            bonus.add(template.copyWithCount(size));
            count -= size;
        }
    }

    private static void applyButcherDrops(LootContext context, List<ItemStack> drops) {
        Entity target = optionalEntity(context, LootContextParams.THIS_ENTITY);
        Entity attacker = optionalEntity(context, LootContextParams.ATTACKING_ENTITY);
        if (!(target instanceof LivingEntity victim) || !(attacker instanceof LivingEntity killer)
                || !EnchLibMobCategories.has(context.getLevel(), victim, "animals")) {
            return;
        }

        int butcher = Math.max(EnchantmentLevels.onItem(context.getLevel(), killer.getMainHandItem(), "butcher"),
                EnchantmentLevels.onItem(context.getLevel(), killer.getOffhandItem(), "butcher"));
        if (butcher <= 0) return;

        List<ItemStack> doubledMeat = new ArrayList<>();
        for (ItemStack drop : List.copyOf(drops)) {
            if (isAnimalMeat(drop)) doubledMeat.add(drop.copy());
        }
        drops.addAll(doubledMeat);
    }

    private static boolean isAnimalMeat(ItemStack stack) {
        return stack.is(net.minecraft.world.item.Items.BEEF)
                || stack.is(net.minecraft.world.item.Items.PORKCHOP)
                || stack.is(net.minecraft.world.item.Items.CHICKEN)
                || stack.is(net.minecraft.world.item.Items.MUTTON)
                || stack.is(net.minecraft.world.item.Items.RABBIT);
    }

    private static Entity optionalEntity(LootContext context, ContextKey<Entity> key) {
        return optionalParameter(context, key);
    }

    @SuppressWarnings("unchecked")
    private static <T> T optionalParameter(LootContext context, ContextKey<T> key) {
        if (!context.hasParameter(key)) {
            return null;
        }

        for (String methodName : List.of("getOptional", "getOptionalParameter", "getParameter")) {
            try {
                Method method = context.getClass().getMethod(methodName, ContextKey.class);
                return (T) method.invoke(context, key);
            } catch (NoSuchMethodException ignored) {
                // This Minecraft version uses another name for the same optional lookup.
            } catch (ReflectiveOperationException exception) {
                throw new IllegalStateException("Could not read optional loot context entity", exception);
            }
        }

        throw new IllegalStateException("No optional loot parameter lookup method is available");
    }

    public static float scaleArrowVelocity(ServerLevel level, AbstractArrow arrow, float velocity) {
        double elasticity = projectileStrength(level, arrow, "elasticity");
        return elasticity > 0 ? (float) (velocity * (1.0D + 0.25D * elasticity)) : velocity;
    }

    public static double sniperBonus(ServerLevel level, AbstractArrow arrow) {
        int sniper = projectileLevel(level, arrow, "sniper");
        Entity owner = arrow.getOwner();
        if (sniper <= 0 || owner == null) {
            return 0.0D;
        }

        return Math.min(owner.distanceTo(arrow) * 0.4D, 25.0D);
    }

    public static boolean isNinelevenTarget(ServerLevel level, AbstractArrow arrow, Entity target) {
        int nineleven = projectileLevel(level, arrow, "nineleven");
        String targetId = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).getPath();
        return nineleven > 0 && (targetId.equals("phantom") || targetId.equals("bat"));
    }

    public static void detonatePop(ServerLevel level, AbstractArrow arrow, double x, double y, double z) {
        int pop = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "pop");
        if (pop <= 0) {
            return;
        }

        float radius = 1.5F + 0.5F * pop;
        eldritch.surge.combat.SecondaryEnchantmentDamage.run(() ->
                level.explode(arrow, x, y, z, radius, false, Level.ExplosionInteraction.NONE));
    }

    public static int popLevel(ServerLevel level, AbstractArrow arrow) {
        return projectileLevel(level, arrow, "pop");
    }

    public static void applyCurseOfSpider(ServerLevel level, AbstractArrow arrow, Entity target) {
        int curseLevel = projectileLevel(level, arrow, "curse_of_spider");
        if (curseLevel > 0 && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 30 * curseLevel, curseLevel - 1));
        }
    }

    private static int projectileLevel(ServerLevel level, AbstractArrow arrow, String enchantment) {
        return (int) Math.round(projectileStrength(level, arrow, enchantment));
    }

    private static double projectileStrength(ServerLevel level, AbstractArrow arrow, String enchantment) {
        ItemStack weapon = arrow.getWeaponItem();
        // Spawner/dispenser projectiles have no weapon and must keep vanilla behavior.
        if (weapon == null || weapon.isEmpty()) {
            return 0;
        }
        double levelOnArrow = EnchantmentLevels.onItemStrength(level, weapon, enchantment);
        if (levelOnArrow > 0) return levelOnArrow;

        Entity owner = arrow.getOwner();
        if (owner instanceof LivingEntity living) {
            return Math.max(
                    EnchantmentLevels.onItemStrength(level, living.getMainHandItem(), enchantment),
                    EnchantmentLevels.onItemStrength(level, living.getOffhandItem(), enchantment)
            );
        }
        return 0;
    }
}
