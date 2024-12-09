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
                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString()))))
                .then(CommandManager.literal("offline")
                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                .executes(context -> unblockTpa(
                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                        StringArgumentType.getString(context, "playername")))))
                .then(CommandManager.literal("unblock")
                        .then(CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> unblockTpa(
                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                        EntityArgumentType.getPlayer(context, "player").getName().getString()))
                                .then(CommandManager.literal("offline")
                                        .then(CommandManager.argument("playername", StringArgumentType.word())
                                                .executes(context -> unblockTpa(
                                                        Objects.requireNonNull(context.getSource().getPlayer()),
                                                        StringArgumentType.getString(context, "playername")))))))
                .then(CommandManager.literal("list")
                        .executes(context -> showBlockTpaList(
                                Objects.requireNonNull(context.getSource().getPlayer())))));
    }


    private static int blockTpa(ServerPlayerEntity blocker, String blockedName) {
        if (Objects.equals(blocker.getName().getString(), blockedName)) {
            blocker.sendMessage(Text.literal("Try with someone else")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        String message = TpaBlockManager.block(blocker, blockedName)
                ? "Successfully blocked " + blockedName
                : blockedName + " is already blocked";

        blocker.sendMessage(Text.literal(message)
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }


    private static int unblockTpa(ServerPlayerEntity blocker, String blockedName) {
        if (Objects.equals(blocker.getName().getString(), blockedName)) {
            blocker.sendMessage(Text.literal("Try with someone else")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        String message = TpaBlockManager.unblock(blocker, blockedName)
                ? "Successfully unblocked " + blockedName
                : blockedName + " is already unblocked";

        blocker.sendMessage(Text.literal(message)
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }


    private static int showBlockTpaList(ServerPlayerEntity blocker) {
        Set<String> blocks = TpaBlockManager.getBlocks(blocker);

        if (blocks.isEmpty()) {
            blocker.sendMessage(Text.literal("You have not blocked anyone"));
            return -1;
        }

        blocker.sendMessage(Text.literal("You have blocked: ")
                .append(Text.literal(String.join(", ", blocks))));

        return 1;
    }
}
