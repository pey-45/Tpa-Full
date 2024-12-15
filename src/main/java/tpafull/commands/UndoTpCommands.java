package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.UndoTpManager;

public class UndoTpCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("undotp")
                .executes(context -> undoTp(
                        context.getSource().getPlayer())));
    }


    private static int undoTp(ServerPlayerEntity player) {
        if (UndoTpManager.hasNoLastTp(player)) {
            player.sendMessage(Text.literal("No teleport to undo")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }


        player.teleport(UndoTpManager.getLastTpWorld(player), UndoTpManager.getX(player), UndoTpManager.getY(player), UndoTpManager.getZ(player), player.getYaw(), player.getPitch());
        UndoTpManager.removeLastTp(player);

        player.sendMessage(Text.literal("Teleporting back...")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }
}
