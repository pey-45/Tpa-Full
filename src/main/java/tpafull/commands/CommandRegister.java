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
import net.minecraft.util.math.BlockPos;
import tpafull.data.TpaMode;
import tpafull.data.TpaRequest;
import tpafull.data.TpaRequestManager;
import tpafull.utils.GlobalScheduler;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

public class CommandRegister {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.literal("target")
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("tpahere")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("tpaccept")
                .executes(context -> -1)
                .then(CommandManager.argument("sender", EntityArgumentType.player())
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("tpadeny")
                .executes(context -> -1)
                .then(CommandManager.argument("sender", EntityArgumentType.player())
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("blocktpa")
                .then(CommandManager.literal("block")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> -1)))
                .then(CommandManager.literal("unblock")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> -1))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", EntityArgumentType.player())
                                        .executes(context -> -1))))
                .then(CommandManager.literal("list")
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("autotpa")
                .then(CommandManager.literal("allow")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> -1)))
                .then(CommandManager.literal("deny")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> -1))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", EntityArgumentType.player())
                                        .executes(context -> -1))))
                .then(CommandManager.literal("list")
                        .executes(context -> -1)));


        dispatcher.register(CommandManager.literal("undo")
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


    private static int sendTpaRequest(ServerCommandSource source, ServerPlayerEntity target, TpaMode mode) {
        ServerPlayerEntity sender = source.getPlayer();
        Objects.requireNonNull(sender);

        TpaRequest request = new TpaRequest(sender, mode);

        // Get tpa/tpahere requests from the target depending on the mode
        Deque<TpaRequest> targetRequests = TpaRequestManager.getRequestsFrom(target, mode);

        // Remove any other request by sender
        TpaRequestManager.removeRequestsSentBy(targetRequests, sender);

        targetRequests.add(request);

        // Schedule timeout
        GlobalScheduler.schedule(() -> {
            if (targetRequests.remove(request)) {
                String modeString = mode == TpaMode.TPA ? "Tpa" : "TpaHere";
                sender.sendMessage(Text.literal(modeString + " request to " + target.getName().getString() + " timed out")
                        .styled(style -> style
                                .withColor(Formatting.RED)));

                sender.sendMessage(Text.literal(modeString + " request from " + sender.getName().getString() + " timed out")
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

        return 1;
    }


    private static int acceptTpaRequest(ServerCommandSource source, ServerPlayerEntity target) {
        ServerPlayerEntity sender = source.getPlayer();
        Objects.requireNonNull(sender);

        if (!target.networkHandler.isConnectionOpen()) {
            sender.sendMessage(Text.literal(target.getName() + "is not connected")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        TpaRequest request = TpaRequestManager.findRequest(sender, target);
        if (!(request = TpaRequestManager.removeRequestsSentBy(TpaRequestManager.getRequestsFrom(sender, mode), target))) {
            sender.sendMessage(Text.literal("No pending requests"));
        }


        if (mode == TpaMode.TPA) {
            target.teleport(sender.getServerWorld(), sender.getX(), sender.getY(), sender.getZ(), sender.getYaw(), sender.getPitch());
            TpaRequestManager.cleanAllRequestsSentBy(target);
        } else {
            sender.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
        }

        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source, ServerPlayerEntity target) {
        ServerPlayerEntity sender = source.getPlayer();
        Objects.requireNonNull(sender);

        if (!target.networkHandler.isConnectionOpen()) {
            sender.sendMessage(Text.literal(target.getName() + "is not connected")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }


    }


    private static int blockTpa(ServerCommandSource source, ServerPlayerEntity target) {
        return -1;
    }


    private static int unblockTpa(ServerCommandSource source, ServerPlayerEntity target) {
        return -1;
    }


    private static int unblockTpaOffline(ServerCommandSource source, String targetname) {
        return -1;
    }


    private static int showBlockTpaList(ServerCommandSource source) {
        return -1;
    }


    private static int allowAutoTpa(ServerCommandSource source, ServerPlayerEntity target) {
        return -1;
    }


    private static int denyAutoTpa(ServerCommandSource source, ServerPlayerEntity target) {
        return -1;
    }


    private static int denyAutoTpaOffline(ServerCommandSource source, String targetname) {
        return -1;
    }


    private static int showAutoTpaList(ServerCommandSource source) {
        return -1;
    }


    private static int undoTpa(ServerCommandSource source) {
        return -1;
    }


    private static int tpaLastDeath(ServerCommandSource source) {
        return -1;
    }


    private static int showLastDeath(ServerCommandSource source) {
        return -1;
    }


    private static int tpaHome(ServerCommandSource source) {
        return -1;
    }


    private static int showHome(ServerCommandSource source) {
        return -1;
    }


    private static int setHome(ServerCommandSource source, BlockPos pos) {
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
