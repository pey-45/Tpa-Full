package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.GlobalPos;
import tpafull.managers.HomeManager;

import java.util.Objects;

public class HomeCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("home")
                .executes(context -> tpHome(
                        context.getSource().getPlayer()))
                .then(CommandManager.literal("where")
                        .executes(context -> showHome(
                                context.getSource().getPlayer())))
                .then(CommandManager.literal("set")
                        .executes(context -> setHome(
                                Objects.requireNonNull(context.getSource().getPlayer()))))
                .then(CommandManager.literal("remove")
                        .executes(context -> removeHome(
                                Objects.requireNonNull(context.getSource().getPlayer())))));
    }


    private static int tpHome(ServerPlayerEntity player) {
        if (HomeManager.hasNoHome(player)) {
            player.sendMessage(Text.literal("No home set")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        double x = HomeManager.getX(player);
        double y = HomeManager.getY(player);
        double z = HomeManager.getZ(player);

        player.teleport(HomeManager.getHomeWorld(player), x, y, z, player.getYaw(), player.getPitch());

        player.sendMessage(Text.literal("Teleporting home...")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }


    private static int showHome(ServerPlayerEntity player) {
        if (HomeManager.hasNoHome(player)) {
            player.sendMessage(Text.literal("No home set")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
        }

        int x = (int) HomeManager.getX(player);
        int y = (int) HomeManager.getY(player);
        int z = (int) HomeManager.getZ(player);

        String worldString = Objects.requireNonNull(Objects.requireNonNull(HomeManager.getHomeWorld(player)).getRegistryKey().getValue().getPath());

        player.sendMessage(Text.literal("Home is at " + x + ", " + y + ", " + z + " in " + worldString));

        return 1;
    }


    private static int setHome(ServerPlayerEntity player) {
        GlobalPos pos = GlobalPos.create(player.getServerWorld().getRegistryKey(), player.getBlockPos());

        HomeManager.setHome(player, pos);

        player.sendMessage(Text.literal("Home set")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }

    private static int removeHome(ServerPlayerEntity player) {
        if (HomeManager.hasNoHome(player)) {
            player.sendMessage(Text.literal("No home set already")
                    .styled(style -> style
                            .withColor(Formatting.GREEN)));
            return -1;
        }

        HomeManager.removeHome(player);

        player.sendMessage(Text.literal("Home removed")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        return 1;
    }
}
