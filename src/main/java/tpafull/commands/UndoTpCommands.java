package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.LastTpManager;

public class UndoTpCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("undotp")
                .executes(context -> undoTp(
                        context.getSource().getPlayer())));
    }


    private static int undoTp(ServerPlayerEntity player) {
        if (LastTpManager.hasLastTp(player)) {
            player.sendMessage(Text.literal("No teleport to undo")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        player.teleport(LastTpManager.getLastTpWorld(player), LastTpManager.getX(player), LastTpManager.getY(player), LastTpManager.getZ(player), player.getYaw(), player.getPitch());
        LastTpManager.removeLastTp(player);

        return 1;
    }
}
