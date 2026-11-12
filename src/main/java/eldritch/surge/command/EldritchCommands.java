package eldritch.surge.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;

public final class EldritchCommands {
    private static final DynamicCommandExceptionType UNKNOWN_SETTING = new DynamicCommandExceptionType(
            setting -> Component.translatable("commands.eldrich-surge.setting.unknown", setting)
    );
    private static final DynamicCommandExceptionType INVALID_SETTING_VALUE = new DynamicCommandExceptionType(
            val -> Component.translatable("commands.eldrich-surge.setting.invalid_value", val)
    );

    private EldritchCommands() {
    }

    public static void initialize() {
        CommandRegistrationCallback.EVENT.register(EldritchCommands::register);
    }

    private static void register(CommandDispatcher<CommandSourceStack> dispatcher, net.minecraft.commands.CommandBuildContext context, Commands.CommandSelection selection) {
        var settingArg = Commands.argument("setting", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    builder.suggest("keep_loyalty_tridents");
                    builder.suggest("sweeping_requires_enchantment");
                    builder.suggest("lethal_poison");
                    return builder.buildFuture();
                });

        var valueArg = Commands.argument("value", StringArgumentType.word())
                .suggests((ctx, builder) -> {
                    String setting = "";
                    try {
                        setting = StringArgumentType.getString(ctx, "setting").toLowerCase(java.util.Locale.ROOT);
                    } catch (IllegalArgumentException ignored) {
                    }
                    if ("lethal_poison".equals(setting)) {
                        builder.suggest("off");
                        builder.suggest("player");
                        builder.suggest("mob");
                        builder.suggest("all");
                    } else {
                        builder.suggest("on");
                        builder.suggest("off");
                    }
                    return builder.buildFuture();
                })
                .executes(EldritchCommands::executeSetting);

        var eldrichNode = dispatcher.register(Commands.literal("eldrich")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .then(settingArg.then(valueArg)));

        dispatcher.register(Commands.literal("eldritch")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER))
                .redirect(eldrichNode));
    }

    private static int executeSetting(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String setting = StringArgumentType.getString(context, "setting").toLowerCase(java.util.Locale.ROOT);
        String rawValue = StringArgumentType.getString(context, "value").toLowerCase(java.util.Locale.ROOT);

        if ("lethal_poison".equals(setting)) {
            String mode;
            if (rawValue.equals("off") || rawValue.equals("false") || rawValue.equals("0") || rawValue.equals("disable")) {
                mode = "off";
            } else if (rawValue.equals("player") || rawValue.equals("players")) {
                mode = "player";
            } else if (rawValue.equals("mob") || rawValue.equals("mobs")) {
                mode = "mob";
            } else if (rawValue.equals("all") || rawValue.equals("on") || rawValue.equals("true") || rawValue.equals("1") || rawValue.equals("enable")) {
                mode = "all";
            } else {
                throw INVALID_SETTING_VALUE.create(rawValue);
            }

            eldritch.surge.config.EnchantmentCapsConfig.setLethalPoisonMode(mode);
            eldritch.surge.config.EnchantmentCapsConfig.saveGlobal();

            var server = context.getSource().getServer();
            if (server != null) {
                boolean gameruleEnabled = !"off".equals(mode);
                for (var level : server.getAllLevels()) {
                    level.getGameRules().set(eldritch.surge.game.EldritchGameRules.LETHAL_POISON, gameruleEnabled, server);
                }
            }

            context.getSource().sendSuccess(
                    () -> Component.translatable(
                            "commands.eldrich-surge.setting.success",
                            setting,
                            mode
                    ),
                    true
            );
            return 1;
        }

        boolean boolValue;
        if (rawValue.equals("on") || rawValue.equals("true") || rawValue.equals("1") || rawValue.equals("enable")) {
            boolValue = true;
        } else if (rawValue.equals("off") || rawValue.equals("false") || rawValue.equals("0") || rawValue.equals("disable")) {
            boolValue = false;
        } else {
            throw INVALID_SETTING_VALUE.create(rawValue);
        }

        switch (setting) {
            case "keep_loyalty_tridents" -> eldritch.surge.config.EnchantmentCapsConfig.setKeepLoyaltyTridents(boolValue);
            case "sweeping_requires_enchantment" -> eldritch.surge.config.EnchantmentCapsConfig.setSweepingRequiresEnchantment(boolValue);
            default -> throw UNKNOWN_SETTING.create(setting);
        }

        eldritch.surge.config.EnchantmentCapsConfig.saveGlobal();

        var server = context.getSource().getServer();
        if (server != null) {
            for (var level : server.getAllLevels()) {
                switch (setting) {
                    case "keep_loyalty_tridents" -> level.getGameRules().set(eldritch.surge.game.EldritchGameRules.KEEP_LOYALTY_TRIDENTS, boolValue, server);
                    case "sweeping_requires_enchantment" -> level.getGameRules().set(eldritch.surge.game.EldritchGameRules.SWEEPING_REQUIRES_ENCHANTMENT, boolValue, server);
                }
            }
        }

        context.getSource().sendSuccess(
                () -> Component.translatable(
                        "commands.eldrich-surge.setting.success",
                        setting,
                        boolValue ? "on" : "off"
                ),
                true
        );
        return 1;
    }
}
