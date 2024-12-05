package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import tpafull.data.TpaMode;
import tpafull.data.TpaRequest;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommandRegister {
    private static final ConcurrentHashMap<ServerPlayerEntity, Deque<TpaRequest>> tpaRequests = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ServerPlayerEntity, Deque<TpaRequest>> tpaHereRequests = new ConcurrentHashMap<>();

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
        return -1;
    }


    private static int acceptTpaRequest(ServerCommandSource source, ServerPlayerEntity target, TpaMode mode) {
        return -1;
    }

    private static int denyTpaRequest(ServerCommandSource source, ServerPlayerEntity target) {
        return -1;
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
