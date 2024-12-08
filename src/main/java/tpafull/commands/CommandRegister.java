package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.GlobalPos;
import org.jetbrains.annotations.NotNull;
import tpafull.data.TpaMode;
import tpafull.managers.*;
import tpafull.utils.GlobalScheduler;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandRegister {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"), TpaMode.TPA))));


        dispatcher.register(CommandManager.literal("tpahere")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"), TpaMode.TPAHERE))));


        dispatcher.register(CommandManager.literal("tpaccept")
                .then(CommandManager.argument("requester", EntityArgumentType.player())
                        .executes(context -> acceptTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "requester")))));


        dispatcher.register(CommandManager.literal("tpadeny")
                .then(CommandManager.argument("requester", EntityArgumentType.player())
                        .executes(context -> denyTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "requester")))));


        dispatcher.register(CommandManager.literal("blocktpa")
                .then(CommandManager.literal("block")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> blockTpa(context.getSource(), EntityArgumentType.getPlayer(context, "player").getName().getString()))))
                .then(CommandManager.literal("offline")
                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                .executes(context -> unblockTpa(context.getSource(), StringArgumentType.getString(context, "playername")))))
                .then(CommandManager.literal("unblock")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> unblockTpa(context.getSource(), EntityArgumentType.getPlayer(context, "player").getName().getString()))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> unblockTpa(context.getSource(), StringArgumentType.getString(context, "playername")))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showBlockTpaList(context.getSource()))));


        dispatcher.register(CommandManager.literal("autotpa")
                .then(CommandManager.literal("allow")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> allowAutoTpa(context.getSource(), EntityArgumentType.getPlayer(context, "player").getName().getString()))))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> allowAutoTpa(context.getSource(), StringArgumentType.getString(context, "playername")))))
                .then(CommandManager.literal("deny")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> denyAutoTpa(context.getSource(), EntityArgumentType.getPlayer(context, "player").getName().getString())))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> denyAutoTpa(context.getSource(), StringArgumentType.getString(context, "playername"))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showAutoTpaList(context.getSource()))));


        dispatcher.register(CommandManager.literal("undotp")
                .executes(context -> -1));


        dispatcher.register(CommandManager.literal("lastdeath")
                .executes(context -> -1)
                .then(CommandManager.literal("where")
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("home")
                .executes(context -> -1)
                .then(CommandManager.literal("where")
                        .executes(context -> -1))
                .then(CommandManager.literal("set")
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("tpafull")
                .executes(context -> -1)
                .then(CommandManager.literal("help")
                        .executes(context -> -1)
                        .then(CommandManager.argument("command", StringArgumentType.greedyString())
                                .executes(context -> 1))));
    }


    private static int sendTpaRequest(@NotNull ServerCommandSource source, ServerPlayerEntity target, @NotNull TpaMode mode) {
        ServerPlayerEntity sender = source.getPlayer();
        Objects.requireNonNull(sender);

        if (TpaBlockManager.getBlocks(target).contains(sender.getName().getString())) {
            sender.sendMessage(Text.literal("You are blocked by ")
                    .styled(style -> style
                            .withColor(Formatting.RED))
                    .append(Text.literal(target.getName().getString())
                            .styled(style -> style
                                    .withColor(Formatting.AQUA))));
            return -1;
        }

        // Add new request to the target, cleaning any other
        TpaRequestManager.removePossibleRequest(sender, target);
        TpaRequestManager.addRequest(sender, target, mode);

        // Schedule timeout
        GlobalScheduler.schedule(() -> {
            if (TpaRequestManager.removePossibleRequest(sender, target)) {
                sender.sendMessage(Text.literal(mode + " request to " + target.getName().getString() + " timed out")
                        .styled(style -> style
                                .withColor(Formatting.RED)));

                target.sendMessage(Text.literal(mode + " request from " + sender.getName().getString() + " timed out")
                        .styled(style -> style
                                .withColor(Formatting.RED)));
            }
        }, 30, TimeUnit.SECONDS);

        // Send form to target
        String message = mode == TpaMode.TPA ? " wants to teleport to you (tpa)" : " wants you to teleport to him (tpahere)";
        target.sendMessage(Text.literal(sender.getName().getString())
                        .styled(style -> style
                                .withColor(Formatting.AQUA))
                        .append(Text.literal(message)
                                .styled(style -> style
                                        .withColor(Formatting.WHITE)))
                        .append(Text.literal("\n[Accept] ")
                                        .styled(style -> style
                                                .withColor(Formatting.GREEN)
                                                .withBold(true)
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to accept")))))
                        .append(Text.literal("[Deny]")
                                        .styled(style -> style
                                                .withColor(Formatting.RED)
                                                .withBold(true)
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to reject"))))),
                false
        );
        sender.sendMessage(Text.literal(mode + " request sent to ")
                .append(Text.literal(target.getName().getString())
                        .styled(style -> style
                                .withColor(Formatting.AQUA))));

        return 1;
    }


    private static int acceptTpaRequest(@NotNull ServerCommandSource source, @NotNull ServerPlayerEntity requester) {
        ServerPlayerEntity acceptor = source.getPlayer();
        Objects.requireNonNull(acceptor);

        // Request does no longer exist
        if (!TpaRequestManager.removePossibleRequest(requester, acceptor)) {
            acceptor.sendMessage(Text.literal("No pending requests from " + requester.getName()));
            return -1;
        }

        // Teleport
        TpaMode mode = TpaRequestManager.getModeFromRequest(requester, acceptor);

        if (mode == null) {
            acceptor.sendMessage(Text.literal("Unexpected error")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        if (TpaRequestManager.getModeFromRequest(requester, acceptor) == TpaMode.TPA) {
            requester.teleport(acceptor.getServerWorld(), acceptor.getX(), acceptor.getY(), acceptor.getZ(), acceptor.getYaw(), acceptor.getPitch());
        } else if (TpaRequestManager.getModeFromRequest(requester, acceptor) == TpaMode.TPAHERE) {
            acceptor.teleport(requester.getServerWorld(), requester.getX(), requester.getY(), requester.getZ(), requester.getYaw(), requester.getPitch());
        }

        requester.sendMessage(Text.literal("Request accepted!")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        acceptor.sendMessage(Text.literal("Request accepted!")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        TpaRequestManager.removeAllRequestsFrom(requester);

        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source, ServerPlayerEntity requester) {
        ServerPlayerEntity acceptor = source.getPlayer();
        Objects.requireNonNull(acceptor);

        // Request does no longer exist
        if (!TpaRequestManager.removePossibleRequest(requester, acceptor)) {
            acceptor.sendMessage(Text.literal("No pending requests"));
            return -1;
        }

        requester.sendMessage(Text.literal("Request rejected")
                .styled(style -> style
                        .withColor(Formatting.RED)));

        acceptor.sendMessage(Text.literal("Request rejected")
                .styled(style -> style
                        .withColor(Formatting.RED)));

        return 1;
    }


    private static int blockTpa(ServerCommandSource source, String blockedName) {
        ServerPlayerEntity blocker = source.getPlayer();
        Objects.requireNonNull(blocker);

        String message = TpaBlockManager.block(blocker, blockedName) ? " successfully unblocked" : " is already blocked";

        blocker.sendMessage(Text.literal(blockedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int unblockTpa(ServerCommandSource source, String blockedName) {
        ServerPlayerEntity blocker = source.getPlayer();
        Objects.requireNonNull(blocker);

        String message = TpaBlockManager.unblock(blocker, blockedName) ? " successfully unblocked" : " was already unblocked";

        blocker.sendMessage(Text.literal(blockedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int showBlockTpaList(ServerCommandSource source) {
        ServerPlayerEntity blocker = source.getPlayer();
        Objects.requireNonNull(blocker);

        Set<String> blocks = TpaBlockManager.getBlocks(source.getPlayer());

        blocker.sendMessage(Text.literal("You have blocked: ")
                .append(Text.literal(String.join(", ", blocks))));

        return 1;
    }


    private static int allowAutoTpa(ServerCommandSource source, String allowedName) {
        ServerPlayerEntity allower = source.getPlayer();
        Objects.requireNonNull(allower);

        String message = AutoTpaManager.add(allower, allowedName) ? " successfully allowed to auto tpa you" : " is already allowed to auto tpa you";

        allower.sendMessage(Text.literal(allowedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int denyAutoTpa(ServerCommandSource source, String allowedName) {
        ServerPlayerEntity allower = source.getPlayer();
        Objects.requireNonNull(allower);

        String message = AutoTpaManager.remove(allower, allowedName) ? " successfully denied to auto tpa you" : " was already not allowed to auto tpa you";

        allower.sendMessage(Text.literal(allowedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int showAutoTpaList(ServerCommandSource source) {
        ServerPlayerEntity allower = source.getPlayer();
        Objects.requireNonNull(allower);

        Set<String> allowed = AutoTpaManager.getAllowed(source.getPlayer());

        allower.sendMessage(Text.literal("You have allowed to auto tpa you: ")
                .append(Text.literal(String.join(", ", allowed))));

        return 1;
    }


    private static int undoTp(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        Objects.requireNonNull(player);

        if (LastTpManager.hasLastTp(player)) {
            player.sendMessage(Text.literal("No teleport to undo")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        player.teleport(LastTpManager.getWorld(player), LastTpManager.getX(player), LastTpManager.getY(player), LastTpManager.getZ(player), player.getYaw(), player.getPitch());
        LastTpManager.removeLastTp(player);

        return 1;
    }

    private static int tpLastDeath(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        Objects.requireNonNull(player);

        if (LastDeathManager.hasLastDeath(player)) {
            player.sendMessage(Text.literal("No last death position to teleport to")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        double x = LastDeathManager.getX(player);
        double y = LastDeathManager.getY(player);
        double z = LastDeathManager.getZ(player);

        player.teleport(LastDeathManager.getWorld(player), x, y, z, player.getYaw(), player.getPitch());
        LastDeathManager.removeLastDeath(player);

        return 1;
    }


    private static int showLastDeath(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        Objects.requireNonNull(player);

        if (!LastDeathManager.hasLastDeath(player)) {
            player.sendMessage(Text.literal("No last death position")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        double x = LastDeathManager.getX(player);
        double y = LastDeathManager.getY(player);
        double z = LastDeathManager.getZ(player);
        String world = Objects.requireNonNull(Objects.requireNonNull(LastDeathManager.getWorld(player)).getRegistryKey().getValue().getPath());

        player.sendMessage(Text.literal("Last death: " + x + ", " + y + ", " + z + " in " + world));

        return 1;
    }


    private static int tpaHome(ServerCommandSource source) {
        return -1;
    }


    private static int showHome(ServerCommandSource source) {
        return -1;
    }


    private static int setHome(ServerCommandSource source, GlobalPos pos) {
        return -1;
    }


    private static int showAbout() {
        return -1;
    }


    private static int showHelp() {
        return -1;
    }


    private static int showHelp(String command) {
        return -1;
    }
}
