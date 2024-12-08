package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class InfoCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpafull")
                .executes(context -> -1)
                .then(CommandManager.literal("help")
                        .executes(context -> -1)
                        .then(CommandManager.argument("command", StringArgumentType.greedyString())
                                .executes(context -> 1))));
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
