package eldritch.surge.enchantment.mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.WeatherData;

public final class SpellCastMechanics {
    private SpellCastMechanics() {
    }

    public static boolean castFromDroppedBook(ServerLevel level, ItemEntity itemEntity) {
        Entity owner = itemEntity.getOwner();
        if (!(owner instanceof LivingEntity caster) || itemEntity.getAge() > 1) {
            return false;
        }

        ItemStack stack = itemEntity.getItem();
        String spell = SpellBookItems.spellId(stack).orElse(null);
        if (spell == null) {
            return false;
        }

        int levelValue = Math.max(1, spellLevel(stack, spell));
        switch (spell) {
            case "cocktail_spell" -> castCocktail(level, caster, levelValue);
            case "ragnarok" -> castRagnarok(level, itemEntity.blockPosition());
            case "red_moon" -> castRedMoon(level, itemEntity.blockPosition());
            case "storm_spell" -> castStorm(level, caster);
            case "trench_spell" -> castTrench(level, itemEntity.blockPosition(), levelValue);
            default -> {
                return false;
            }
        }

        itemEntity.discard();
        return true;
    }

    private static void castCocktail(ServerLevel level, LivingEntity caster, int spellLevel) {
        double radius = 8.0D + spellLevel * 8.0D;
        for (ServerPlayer player : level.getPlayers(player -> player.distanceToSqr(caster) <= radius * radius)) {
            for (MobEffectInstance effect : caster.getActiveEffects()) {
                player.addEffect(new MobEffectInstance(effect), caster);
            }
        }
    }

    private static void castStorm(ServerLevel level, LivingEntity caster) {
        WeatherData weather = level.getWeatherData();
        weather.setClearWeatherTime(0);
        weather.setRainTime(20 * 60 * 5);
        weather.setThunderTime(20 * 60 * 5);
        weather.setRaining(true);
        weather.setThundering(true);
        level.setRainLevel(1.0F);
        level.setThunderLevel(1.0F);

        for (int i = 0; i < 5; i++) {
            int x = caster.getBlockX() + level.getRandom().nextIntBetweenInclusive(-32, 32);
            int z = caster.getBlockZ() + level.getRandom().nextIntBetweenInclusive(-32, 32);
            BlockPos target = level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, new BlockPos(x, caster.getBlockY(), z));
            Entity entity = createEntity(level, "lightning_bolt");
            if (entity instanceof LightningBolt lightning) {
                lightning.setPos(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
                if (caster instanceof ServerPlayer player) {
                    lightning.setCause(player);
                }
                level.addFreshEntity(lightning);
            }
        }
    }

    private static void castRagnarok(ServerLevel level, BlockPos origin) {
        for (int i = 0; i < 4; i++) {
            double x = origin.getX() + 0.5D + level.getRandom().nextIntBetweenInclusive(-3, 3);
            double z = origin.getZ() + 0.5D + level.getRandom().nextIntBetweenInclusive(-3, 3);
            double y = origin.getY() + 0.2D;

            Entity horse = createEntity(level, "skeleton_horse");
            Entity rider = createEntity(level, "wither_skeleton");
            if (horse == null || rider == null) {
                continue;
            }

            horse.setPos(x, y, z);
            rider.setPos(x, y, z);
            if (rider instanceof LivingEntity living) {
                equip(living, Items.NETHERITE_SWORD, Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS);
            }

            level.addFreshEntity(horse);
            level.addFreshEntity(rider);
            rider.startRiding(horse);
        }
    }

    private static void castRedMoon(ServerLevel level, BlockPos origin) {
        for (int i = 0; i < 30; i++) {
            double x = origin.getX() + 0.5D + level.getRandom().nextIntBetweenInclusive(-8, 8);
            double z = origin.getZ() + 0.5D + level.getRandom().nextIntBetweenInclusive(-8, 8);
            double y = origin.getY() + 0.2D;

            Entity zombie = createEntity(level, "zombie");
            if (zombie == null) {
                continue;
            }

            zombie.setPos(x, y, z);
            if (zombie instanceof LivingEntity living) {
                equip(living, Items.DIAMOND_SWORD, Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
            }

            if (i % 5 == 0) {
                Entity horse = createEntity(level, "zombie_horse");
                if (horse != null) {
                    horse.setPos(x, y, z);
                    level.addFreshEntity(horse);
                    level.addFreshEntity(zombie);
                    zombie.startRiding(horse);
                    continue;
                }
            }

            level.addFreshEntity(zombie);
        }
    }

    private static Entity createEntity(ServerLevel level, String id) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace(id));
        return type == null ? null : type.create(level, EntitySpawnReason.TRIGGERED);
    }

    private static void equip(LivingEntity entity, Item mainHand, Item helmet, Item chestplate, Item leggings, Item boots) {
        entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(mainHand));
        entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(helmet));
        entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(chestplate));
        entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(leggings));
        entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(boots));
    }

    private static int spellLevel(ItemStack stack, String spell) {
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .filter(entry -> entry.getKey().unwrapKey()
                        .map(key -> key.identifier().equals(Identifier.fromNamespaceAndPath("eldritch-surge", spell)))
                        .orElse(false))
                .mapToInt(entry -> entry.getIntValue())
                .findFirst()
                .orElse(0);
    }

    private static void castTrench(ServerLevel level, BlockPos origin, int spellLevel) {
        int radius = 2 + spellLevel;
        int minY = Math.max(level.getMinY(), -55);
        for (int x = origin.getX() - radius; x <= origin.getX() + radius; x++) {
            for (int z = origin.getZ() - radius; z <= origin.getZ() + radius; z++) {
                for (int y = origin.getY(); y >= minY; y--) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (!state.isAir() && !state.is(Blocks.BEDROCK)) {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }
    }
}
