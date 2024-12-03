package tpapey.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import tpapey.data.Messages;
import tpapey.data.TpaMode;

import java.util.Objects;

public class TpaSender {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("tpa")
                .executes(context -> sendTpaRequest(context.getSource(), DefaultTpaManager.getDefaultTpa(Objects.requireNonNull(context.getSource().getPlayer())), TpaMode.TPA))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"), TpaMode.TPA)))
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

        dispatcher.register(CommandManager.literal("tpadefault")
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> setDefaultTpa(context.getSource(), EntityArgumentType.getPlayer(context, "player")))))
                .then(CommandManager.literal("get")
                        .executes(context -> getDefaultTpa(context.getSource())))
                .then(CommandManager.literal("remove")
                        .executes(context -> removeDefaultTpa(context.getSource())))

        );

        dispatcher.register(CommandManager.literal("home")
                .executes(context -> sendTpaHomeRequest(context.getSource(), HomeManager.getHome(Objects.requireNonNull(context.getSource().getPlayer()))))
                .then(CommandManager.literal("set")
                        .executes(context -> setHome(context.getSource(), new BlockPos(
                                (int) Objects.requireNonNull(context.getSource().getPlayer()).getX(),
                                (int) context.getSource().getPlayer().getY(),
                                (int) context.getSource().getPlayer().getZ()))))
                .then(CommandManager.literal("get")
                        .executes(context -> getHome(context.getSource())))
                .then(CommandManager.literal("remove")
                        .executes(context -> removeHome(context.getSource())))
        );
    }


    private static int sendTpaRequest(ServerCommandSource source, ServerPlayerEntity target, TpaMode mode) {
        if (target == null) {
            Objects.requireNonNull(source.getPlayer()).sendMessage(Messages.NO_DEFAULT_TARGET);
            return -1;
        }
        TpaReceiver.sendTpaRequest(source.getPlayer(), target, mode);
        source.sendFeedback(() -> Messages.REQUEST_SENT.apply(target.getName().getString()), false);
        return 1;
    }


    private static int acceptTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.acceptRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Messages.REQUEST_ACCEPTED, false);
        } else {
            source.sendFeedback(() -> Messages.NO_PENDING_REQUESTS, false);
        }
        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.denyRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Messages.REQUEST_REJECTED, false);
        } else {
            source.sendFeedback(() -> Messages.NO_PENDING_REQUESTS, false);
        }
        return 1;
    }


    private static int setDefaultTpa(ServerCommandSource source, ServerPlayerEntity target) {
        DefaultTpaManager.setDefaultTpa(Objects.requireNonNull(source.getPlayer()), target);
        source.sendFeedback(() -> Messages.DEFAULT_TARGET_SET.apply(target.getName().getString()), false);
        return 1;
    }

    private static int getDefaultTpa(ServerCommandSource source) {
        ServerPlayerEntity target = DefaultTpaManager.getDefaultTpa(Objects.requireNonNull(source.getPlayer()));
        if (target == null) {
            source.getPlayer().sendMessage(Messages.NO_DEFAULT_TARGET);
            return -1;
        }
        source.sendFeedback(() -> Messages.DEFAULT_TARGET.apply(target.getName().getString()), false);
        return 1;
    }

    private static int removeDefaultTpa(ServerCommandSource source) {
        DefaultTpaManager.removeDefaultTpa(Objects.requireNonNull(source.getPlayer()));
        source.sendFeedback(() -> Messages.DEFAULT_TARGET_REMOVED, false);
        return 1;
    }


    private static int sendTpaHomeRequest(ServerCommandSource source, BlockPos pos) {
        if (pos == null) {
            Objects.requireNonNull(source.getPlayer()).sendMessage(Messages.NO_HOME);
            return -1;
        }
        TpaReceiver.doTpaHome(Objects.requireNonNull(source.getPlayer()), pos);
        source.sendFeedback(() -> Messages.TELEPORTED_HOME, false);
        return 1;
    }

    private static int setHome(ServerCommandSource source, BlockPos pos) {
        HomeManager.setHome(Objects.requireNonNull(source.getPlayer()), pos);
        source.sendFeedback(() -> Messages.HOME_SET.apply(pos), false);
        return 1;
    }

    private static int getHome(ServerCommandSource source) {
        BlockPos pos = HomeManager.getHome(Objects.requireNonNull(source.getPlayer()));
        if (pos == null) {
            source.getPlayer().sendMessage(Messages.NO_HOME);
            return -1;
        }
        source.sendFeedback(() -> Messages.HOME.apply(pos), false);
        return 1;
    }

    private static int removeHome(ServerCommandSource source) {
        HomeManager.removeHome(Objects.requireNonNull(source.getPlayer()));
        source.sendFeedback(() -> Messages.HOME_REMOVED, false);
        return 1;
    }
}
