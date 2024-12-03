package tpapey.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpapey.data.TpaMode;

import java.time.format.TextStyle;
import java.util.Objects;

public class TpaSender {
    private static final Text noDefaultTargetMessage = Text.literal("No default tpa target specified\nUse ")
            .styled(style -> style.withColor(Formatting.RED))
            .append(
                    Text.literal("/tpa default set [player]")
                            .styled(style -> style
                                    .withColor(Formatting.AQUA)
                                    .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tpa default set "))
                            )
            )
            .append(
                    Text.literal(" to set it")
                            .styled(style -> style.withColor(Formatting.RED))
            );


    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("tpa")
                .executes(context -> sendTpaRequest(context.getSource(), DefaultTpaManager.getDefaultTpa(Objects.requireNonNull(context.getSource().getPlayer())), TpaMode.TPA))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"), TpaMode.TPA)))
                .then(CommandManager.literal("default")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("name", EntityArgumentType.player())
                                        .executes(context -> setDefaultTpa(context.getSource(), EntityArgumentType.getPlayer(context, "name")))))
                        .then(CommandManager.literal("remove")
                                .executes(context -> removeDefaultTpa(context.getSource())))
                        .then(CommandManager.literal("get")
                                .executes(context -> getDefaultTpa(context.getSource()))))
        );

        dispatcher.register(CommandManager.literal("tpahere")
                .executes(context -> sendTpaRequest(context.getSource(), DefaultTpaManager.getDefaultTpa(Objects.requireNonNull(context.getSource().getPlayer())), TpaMode.TPAHERE))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"), TpaMode.TPAHERE)))
        );

        dispatcher.register(CommandManager.literal("tpaccept")
                .executes(context -> acceptTpaRequest(context.getSource()))
        );

        dispatcher.register(CommandManager.literal("tpadeny")
                .executes(context -> denyTpaRequest(context.getSource()))
        );
    }


    private static int setDefaultTpa(ServerCommandSource source, ServerPlayerEntity target) {
        DefaultTpaManager.setDefaultTpa(Objects.requireNonNull(source.getPlayer()), target);
        source.sendFeedback(() -> Text.literal("Default tpa target set to " + target.getName().getString()), false);
        return 1;
    }

    private static int getDefaultTpa(ServerCommandSource source) {
        ServerPlayerEntity target = DefaultTpaManager.getDefaultTpa(Objects.requireNonNull(source.getPlayer()));
        if (target == null) {
            source.getPlayer().sendMessage(noDefaultTargetMessage);
            return -1;
        }
        source.sendFeedback(() -> Text.literal(target.getName().getString()), false);
        return 1;
    }

    private static int removeDefaultTpa(ServerCommandSource source) {
        DefaultTpaManager.removeDefaultTpa(Objects.requireNonNull(source.getPlayer()));
        source.sendFeedback(() -> Text.literal("Default tpa target removed"), false);
        return 1;
    }


    private static int sendTpaRequest(ServerCommandSource source, ServerPlayerEntity target, TpaMode mode) {
        if (target == null) {
            Objects.requireNonNull(source.getPlayer()).sendMessage(noDefaultTargetMessage);
            return -1;
        }
        TpaReceiver.sendTpaRequest(source.getPlayer(), target, mode);
        source.sendFeedback(() -> Text.literal("Request sent to " + target.getName().getString()), false);
        return 1;
    }


    private static int acceptTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.acceptRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Request accepted"), false);
        } else {
            source.sendFeedback(() -> Text.literal("No pending requests"), false);
        }
        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.denyRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Request rejected"), false);
        } else {
            source.sendFeedback(() -> Text.literal("No pending requests"), false);
        }
        return 1;
    }
}
