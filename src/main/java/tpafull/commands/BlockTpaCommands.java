package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.TpaBlockManager;

import java.util.Objects;
import java.util.Set;

public class BlockTpaCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("blocktpa")
                .then(CommandManager.literal("block")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> blockTpa(
                                        context.getSource().getPlayer(),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString()))))
                .then(CommandManager.literal("offline")
                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                .executes(context -> unblockTpa(
                                        context.getSource().getPlayer(),
                                        StringArgumentType.getString(context, "playername")))))
                .then(CommandManager.literal("unblock")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> unblockTpa(
                                        context.getSource().getPlayer(),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString()))
                                .then(CommandManager.literal("offline")
                                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                                .executes(context -> unblockTpa(
                                                        context.getSource().getPlayer(),
                                                        StringArgumentType.getString(context, "playername")))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showBlockTpaList(
                                Objects.requireNonNull(context.getSource().getPlayer())))));
    }


    private static int blockTpa(ServerPlayerEntity blocker, String blockedName) {
       String message = TpaBlockManager.block(blocker, blockedName) ? " successfully unblocked" : " is already blocked";

        blocker.sendMessage(Text.literal(blockedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int unblockTpa(ServerPlayerEntity blocker, String blockedName) {
        String message = TpaBlockManager.unblock(blocker, blockedName) ? " successfully unblocked" : " was already unblocked";

        blocker.sendMessage(Text.literal(blockedName)
                .styled(style -> style
                        .withColor(Formatting.AQUA))
                .append(Text.literal(message)
                        .styled(style -> style
                                .withColor(Formatting.GREEN))));

        return 1;
    }


    private static int showBlockTpaList(ServerPlayerEntity blocker) {
        Set<String> blocks = TpaBlockManager.getBlocks(blocker);

        blocker.sendMessage(Text.literal("You have blocked: ")
                .append(Text.literal(String.join(", ", blocks))));

        return 1;
    }
}
