package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ServerExplosion;

public final class SurvivalEnchantmentMechanics {
    public static final String FISHING_SCORE = "eldrich_surge_fanatic_catches";
    private static final net.minecraft.resources.Identifier FATIGUE_SPEED = EldritchSurge.id("fatigue_speed");
    private static final net.minecraft.resources.Identifier STEP_HEIGHT = EldritchSurge.id("step_assist_height");
    private static final net.minecraft.resources.Identifier STEP_JUMP = EldritchSurge.id("step_assist_jump");
    private static final net.minecraft.resources.Identifier KINETIC_ARMOR = EldritchSurge.id("kinetic_armor");

    private SurvivalEnchantmentMechanics() {}

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) tick(player);
        });
    }

    public static int fishingScore(ItemStack rod) {
        return Math.clamp(rod.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                .copyTag().getIntOr(FISHING_SCORE, 0), 0, 100);
    }

    public static int completeCatch(ItemStack rod) {
        int score = SurvivalEnchantmentMath.nextFishingScore(fishingScore(rod));
        CustomData.update(DataComponents.CUSTOM_DATA, rod, tag -> tag.putInt(FISHING_SCORE, score));
        return score;
    }

    public static float actionSpeed(LivingEntity entity, ItemStack item) {
        return SurvivalEnchantmentMath.actionSpeed(EnchantmentLevels.onItem(entity.level(), item, "curse_of_fatigue"));
    }

    public static int elytraLevel(LivingEntity entity, String id) {
        ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        return chest.is(Items.ELYTRA) ? EnchantmentLevels.onItem(entity.level(), chest, id) : 0;
    }

    public static boolean dodge(ServerLevel level, LivingEntity entity) {
        int rank = EnchantmentLevels.onItem(level, entity.getItemBySlot(EquipmentSlot.LEGS), "dodge");
        return rank > 0 && entity.getRandom().nextDouble() < SurvivalEnchantmentMath.dodgeChance(rank);
    }

    public static boolean saveFromDeath(LivingEntity entity, DamageSource source) {
        if (!(entity.level() instanceof ServerLevel level)) return false;
        // Preserve vanilla's explicit bypass for commands and unrecoverable damage.
        if (source.is(net.minecraft.tags.DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack shield = entity.getItemInHand(hand);
            if (!shield.is(Items.SHIELD) || EnchantmentLevels.onItem(level, shield, "immortality") <= 0) continue;
            EnchantmentHelper.updateEnchantments(shield, enchantments -> enchantments.removeIf(holder ->
                    holder.unwrapKey().map(key -> key.identifier().equals(EldritchSurge.id("immortality"))).orElse(false)));
            entity.setHealth(2.0F);
            entity.clearFire();
            level.broadcastEntityEvent(entity, (byte) 35);
            return true;
        }
        return false;
    }

    public static boolean cancelExplosion(ServerExplosion explosion) {
        for (ServerPlayer player : explosion.level().players()) {
            if (!player.isAlive() || player.isSpectator() || !player.isBlocking()
                    || player.position().distanceToSqr(explosion.center()) > 9.0D) continue;
            ItemStack shield = player.getItemBlockingWith();
            if (shield != null && shield.is(Items.SHIELD)
                    && EnchantmentLevels.onItem(explosion.level(), shield, "fuse_cutter") > 0) return true;
        }
        return false;
    }

    private static void tick(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        int steps = EnchantmentLevels.onItem(level, player.getItemBySlot(EquipmentSlot.FEET), "step_assist");
        TransientAttributeModifiers.set(player.getAttribute(Attributes.STEP_HEIGHT), STEP_HEIGHT,
                SurvivalEnchantmentMath.stepHeightBonus(steps));
        TransientAttributeModifiers.set(player.getAttribute(Attributes.JUMP_STRENGTH), STEP_JUMP,
                SurvivalEnchantmentMath.stepJumpBonus(steps));
        int kinetic = player.isFallFlying() ? Math.min(5, elytraLevel(player, "kinetic_protection")) : 0;
        TransientAttributeModifiers.set(player.getAttribute(Attributes.ARMOR), KINETIC_ARMOR, 6.0D * kinetic);
        int fatigue = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            fatigue += Math.min(3, EnchantmentLevels.onItem(level, player.getItemBySlot(slot), "curse_of_fatigue"));
        }
        var speed = player.getAttribute(Attributes.MOVEMENT_SPEED);
        double penalty = SurvivalEnchantmentMath.armorSpeed(fatigue) - 1.0D;
        if (speed != null) {
            var current = speed.getModifier(FATIGUE_SPEED);
            if (penalty == 0.0D) {
                if (current != null) speed.removeModifier(FATIGUE_SPEED);
            } else if (current == null || current.amount() != penalty) {
                speed.addOrUpdateTransientModifier(new AttributeModifier(FATIGUE_SPEED, penalty,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
            }
        }
        if (!player.isAlive() || player.isSpectator()) return;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = player.getItemInHand(hand);
            if (EnchantmentLevels.onItem(level, held, "curse_of_slippery") > 0 && player.getRandom().nextInt(660) == 0) {
                var dropped = player.drop(held.copy(), false, net.minecraft.util.Prediction.SERVER_ONLY);
                if (dropped != null) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                    if (player.isUsingItem() && player.getUsedItemHand() == hand) player.stopUsingItem();
                }
            }
        }
        if (player.tickCount % 60 != 0 || !(player.isInWater() || level.isRainingAt(player.blockPosition())
                || level.isRainingAt(net.minecraft.core.BlockPos.containing(player.getEyePosition())))) return;
        if (EnchantmentLevels.onArmor(level, player, "curse_of_corrosion") > 0) {
            player.hurtServer(level, player.damageSources().magic(), 0.25F);
        }
        if (!player.isAlive()) return;
        boolean blessed = false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (!slot.isArmor()) continue;
            ItemStack armor = player.getItemBySlot(slot);
            if (EnchantmentLevels.onItem(level, armor, "blessing_of_the_sea") > 0) {
                blessed = true;
                if (armor.isDamaged()) armor.setDamageValue(Math.max(0, armor.getDamageValue() - 1));
            }
        }
        if (blessed) player.heal(0.25F);
    }
}
