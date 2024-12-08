package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.AutoTpaManager;

import java.util.Set;

public class AutoTpaCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("autotpa")
                .then(CommandManager.literal("allow")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> allowAutoTpa(
                                        context.getSource().getPlayer(),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString()))))
                .then(CommandManager.literal("offline")
                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                .executes(context -> allowAutoTpa(
                                        context.getSource().getPlayer(),
                                        StringArgumentType.getString(context, "playername")))))
                .then(CommandManager.literal("deny")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> denyAutoTpa(
                                        context.getSource().getPlayer(),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString())))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> denyAutoTpa(
                                                context.getSource().getPlayer(),
                                                StringArgumentType.getString(context, "playername"))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showAutoTpaList(
                                context.getSource().getPlayer()))));
    }


    private static int allowAutoTpa(ServerPlayerEntity allower, String allowedName) {
        String message = AutoTpaManager.add(allower, allowedName) ? " successfully allowed to auto tpa you" : " is already allowed to auto tpa you";

        allower.sendMessage(Text.literal(allowedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int denyAutoTpa(ServerPlayerEntity allower, String allowedName) {
        String message = AutoTpaManager.remove(allower, allowedName) ? " successfully denied to auto tpa you" : " was already not allowed to auto tpa you";

        allower.sendMessage(Text.literal(allowedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int showAutoTpaList(ServerPlayerEntity allower) {
        Set<String> allowed = AutoTpaManager.getAllowed(allower);

        allower.sendMessage(Text.literal("You have allowed to auto tpa you: ")
                .append(Text.literal(String.join(", ", allowed))));

        return 1;
    }
}
