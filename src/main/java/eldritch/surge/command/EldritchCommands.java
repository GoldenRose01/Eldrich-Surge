package eldritch.surge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class EldritchCommands {
    private static final DynamicCommandExceptionType UNKNOWN_ENCHANTMENT = new DynamicCommandExceptionType(
            id -> Component.translatable("commands.eldritch-surge.surge.unknown_enchantment", id)
    );
    private static final SimpleCommandExceptionType EMPTY_HAND = new SimpleCommandExceptionType(
            Component.translatable("commands.eldritch-surge.surge.empty_hand")
    );

    private EldritchCommands() {
    }

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register(EldritchCommands::register);
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.commands.CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("surge")
                .then(Commands.argument("enchname", StringArgumentType.word())
                        .suggests(EldritchCommands::suggestEnchantments)
                        .then(Commands.argument("lvl", IntegerArgumentType.integer(0))
                                .suggests(EldritchCommands::suggestLevels)
                                .executes(EldritchCommands::executeSurge))));
    }

    private static int executeSurge(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            throw EMPTY_HAND.create();
        }

        String enchantmentName = StringArgumentType.getString(context, "enchname");
        int level = IntegerArgumentType.getInteger(context, "lvl");
        Holder.Reference<Enchantment> enchantment = resolveEnchantment(context.getSource(), enchantmentName)
                .orElseThrow(() -> UNKNOWN_ENCHANTMENT.create(enchantmentName));

        stack.enchant(enchantment, level);
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();

        context.getSource().sendSuccess(
                () -> Component.translatable(
                        "commands.eldritch-surge.surge.success",
                        Enchantment.getFullname(enchantment, level),
                        stack.getDisplayName()
                ),
                true
        );
        return level;
    }

    private static CompletableFuture<Suggestions> suggestEnchantments(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = context.getSource()
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT);

        Set<String> suggestions = new LinkedHashSet<>();
        enchantments.listElementIds()
                .sorted(Comparator.comparing(ResourceKey::toString))
                .forEach(key -> {
                    Identifier id = key.identifier();
                    suggestions.add(id.toString());
                    suggestions.add(id.getPath());
                });

        suggestions.stream()
                .filter(suggestion -> suggestion.startsWith(builder.getRemainingLowerCase()))
                .forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> suggestLevels(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        int maxLevel = StringArgumentType.getString(context, "enchname").isBlank()
                ? 10
                : resolveEnchantment(context.getSource(), StringArgumentType.getString(context, "enchname"))
                .map(entry -> Math.max(1, entry.value().getMaxLevel()))
                .orElse(10);

        int suggestionCeiling = Math.min(Math.max(maxLevel, 10), 25);
        for (int level = 1; level <= suggestionCeiling; level++) {
            builder.suggest(level);
        }
        if (maxLevel > suggestionCeiling) {
            builder.suggest(maxLevel);
        }
        builder.suggest(maxLevel + 1);
        return builder.buildFuture();
    }

    private static Optional<Holder.Reference<Enchantment>> resolveEnchantment(CommandSourceStack source, String rawName) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = source.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        Identifier fullId = rawName.contains(":") ? Identifier.tryParse(rawName) : null;
        if (fullId != null) {
            return enchantments.get(ResourceKey.create(Registries.ENCHANTMENT, fullId));
        }

        return enchantments.listElements()
                .filter(entry -> entry.unwrapKey()
                        .map(key -> key.identifier().getPath().equals(rawName))
                        .orElse(false))
                .sorted(Comparator.comparing(entry -> entry.unwrapKey()
                        .map(key -> key.identifier().getNamespace().equals("minecraft") ? "0" + key.identifier() : "1" + key.identifier().toString())
                        .orElse("2")))
                .findFirst();
    }
}
