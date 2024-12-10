package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.TpaAutoManager;

import java.util.Objects;
import java.util.Set;

public class TpaAutoCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpaauto")
                .then(CommandManager.literal("allow")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> allowAutoTpa(
                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString())))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> allowAutoTpa(
                                                Objects.requireNonNull(context.getSource().getPlayer()),
                                                StringArgumentType.getString(context, "playername"))))))
                .then(CommandManager.literal("deny")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> denyAutoTpa(
                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString())))
                        .then(CommandManager.literal("offline")
                                .then(CommandManager.argument("playername", StringArgumentType.word())
                                        .executes(context -> denyAutoTpa(
                                                Objects.requireNonNull(context.getSource().getPlayer()),
                                                StringArgumentType.getString(context, "playername"))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showAutoTpaList(
                                context.getSource().getPlayer()))));
    }


    private static int allowAutoTpa(ServerPlayerEntity allower, String allowedName) {
        if (Objects.equals(allower.getName().getString(), allowedName)) {
            allower.sendMessage(Text.literal("Try with someone else")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        String message = TpaAutoManager.add(allower, allowedName)
                ? "Successfully allowed " + allowedName + " to auto tpa you"
                : allowedName + " is already allowed to auto tpa you";

        allower.sendMessage(Text.literal(message));

        return 1;
    }


    private static int denyAutoTpa(ServerPlayerEntity allower, String allowedName) {
        if (Objects.equals(allower.getName().getString(), allowedName)) {
            allower.sendMessage(Text.literal("Try with someone else")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        String message = TpaAutoManager.remove(allower, allowedName)
                ? "Successfully denied " + allowedName + " to auto tpa you"
                : allowedName + " is already denied to auto tpa you";

        allower.sendMessage(Text.literal(message));

        return 1;
    }


    private static int showAutoTpaList(ServerPlayerEntity allower) {
        Set<String> allowed = TpaAutoManager.getAllowed(allower);

        if (allowed == null || allowed.isEmpty()) {
            allower.sendMessage(Text.literal("You have not allowed anyone to auto tpa you"));
            return -1;
        }

        allower.sendMessage(Text.literal("You have allowed to auto tpa you: ")
                .append(Text.literal(String.join(", ", allowed))));

        return 1;
    }
}
