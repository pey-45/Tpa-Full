package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.managers.LastDeathManager;

import java.util.Objects;

public class LastDeathCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("lastdeath")
                .executes(context -> tpLastDeath(
                        context.getSource().getPlayer()))
                .then(CommandManager.literal("where")
                        .executes(context -> showLastDeath(
                                context.getSource().getPlayer()))));
    }


    private static int tpLastDeath(ServerPlayerEntity player) {
        if (!LastDeathManager.hasNoLastDeath(player)) {
            player.sendMessage(Text.literal("No last death position to teleport to")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        double x = LastDeathManager.getX(player);
        double y = LastDeathManager.getY(player);
        double z = LastDeathManager.getZ(player);

        player.teleport(LastDeathManager.getLastDeathWorld(player), x, y, z, player.getYaw(), player.getPitch());

        LastDeathManager.removeLastDeath(player);

        player.sendMessage(Text.literal("Teleporting to last death...")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }


    private static int showLastDeath(ServerPlayerEntity player) {
        if (LastDeathManager.hasNoLastDeath(player)) {
            player.sendMessage(Text.literal("No last death position to show")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        int x = (int) LastDeathManager.getX(player);
        int y = (int) LastDeathManager.getY(player);
        int z = (int) LastDeathManager.getZ(player);

        String worldToString = Objects.requireNonNull(Objects.requireNonNull(LastDeathManager.getLastDeathWorld(player)).getRegistryKey().getValue().getPath());

        player.sendMessage(Text.literal("Last death is at " + x + " " + y + " " + z + " in " + worldToString));

        return 1;
    }
}
