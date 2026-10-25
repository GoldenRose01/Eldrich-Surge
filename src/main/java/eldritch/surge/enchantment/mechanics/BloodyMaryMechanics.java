package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public final class BloodyMaryMechanics {
    private static final float BASE_DROP_CHANCE = 0.10F;
    private static final float LOOTING_BONUS_PER_LEVEL = 0.10F;

    private BloodyMaryMechanics() {}

    public static void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register(BloodyMaryMechanics::onDeath);
    }

    private static void onDeath(LivingEntity victim, DamageSource source) {
        if (!(victim.level() instanceof ServerLevel level)
                || !(source.getEntity() instanceof ServerPlayer killer)
                || source.getDirectEntity() != killer
                || !level.getGameRules().get(GameRules.MOB_DROPS)) return;
        ItemStack weapon = source.getWeaponItem();
        if (weapon == null || weapon.isEmpty()
                || !weapon.is(net.minecraft.tags.TagKey.create(Registries.ITEM, EldritchSurge.id("enchantable/bloody_mary")))
                || EnchantmentLevels.onItem(level, weapon, "bloody_mary") <= 0) return;

        int lootingLevel = EnchantmentLevels.onMinecraftItem(level, weapon, "looting");
        if (level.getRandom().nextFloat() >= dropChance(lootingLevel)) return;

        ItemStack head = createHead(level, victim, killer, source);
        if (!head.isEmpty()) victim.spawnAtLocation(level, head.copyWithCount(1));
    }

    static float dropChance(int lootingLevel) {
        int effectiveLooting = Math.max(0, lootingLevel);
        return Math.min(1.0F, BASE_DROP_CHANCE + LOOTING_BONUS_PER_LEVEL * effectiveLooting);
    }

    private static ItemStack createHead(ServerLevel level, LivingEntity victim, ServerPlayer killer, DamageSource source) {
        if (victim instanceof ServerPlayer player) {
            ItemStack head = new ItemStack(Items.PLAYER_HEAD);
            head.set(DataComponents.PROFILE, ResolvableProfile.createResolved(player.getGameProfile()));
            head.set(DataComponents.LORE, new ItemLore(java.util.List.of(
                    Component.translatable("tooltip.eldrich-surge.head.killed_by", killer.getName())
                            .withStyle(ChatFormatting.GOLD))));
            return head;
        }
        String id = BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType()).toString();
        // Use the native skulls where Minecraft already supplies the matching head.
        ItemStack nativeHead = switch (id) {
            case "minecraft:zombie", "minecraft:giant" -> new ItemStack(Items.ZOMBIE_HEAD);
            case "minecraft:skeleton" -> new ItemStack(Items.SKELETON_SKULL);
            case "minecraft:wither_skeleton" -> new ItemStack(Items.WITHER_SKELETON_SKULL);
            case "minecraft:creeper" -> new ItemStack(Items.CREEPER_HEAD);
            case "minecraft:piglin" -> new ItemStack(Items.PIGLIN_HEAD);
            case "minecraft:ender_dragon" -> new ItemStack(Items.DRAGON_HEAD);
            default -> ItemStack.EMPTY;
        };
        if (!nativeHead.isEmpty()) {
            String displayName = java.util.Arrays.stream(id.substring(id.indexOf(':') + 1).split("_"))
                    .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                    .collect(java.util.stream.Collectors.joining(" "));
            nativeHead.set(DataComponents.ITEM_NAME,
                    Component.literal(displayName + "'s Head"));
            return nativeHead;
        }
        if (!id.startsWith("minecraft:")) return ItemStack.EMPTY;

        var key = ResourceKey.create(Registries.LOOT_TABLE,
                EldritchSurge.id("bloody_mary/entities/" + BuiltInRegistries.ENTITY_TYPE.getKey(victim.getType()).getPath()));
        var table = level.getServer().reloadableRegistries().getLootTable(key);
        var params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, victim)
                .withParameter(LootContextParams.ORIGIN, victim.position())
                .withParameter(LootContextParams.DAMAGE_SOURCE, source)
                .withParameter(LootContextParams.ATTACKING_ENTITY, killer)
                .withParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, killer)
                .withParameter(LootContextParams.LAST_DAMAGE_PLAYER, killer)
                .create(LootContextParamSets.ENTITY);
        java.util.List<ItemStack> heads = new java.util.ArrayList<>();
        table.getRandomItemsRaw(params, heads::add);
        return heads.stream().filter(stack -> !stack.isEmpty()).findFirst().orElse(ItemStack.EMPTY);
    }
}
