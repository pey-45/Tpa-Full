package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

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
}
