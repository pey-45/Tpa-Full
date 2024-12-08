package tpafull.managers;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LastDeathManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, GlobalPos> lastDeaths = new ConcurrentHashMap<>();

    public static boolean hasLastDeath(ServerPlayerEntity player) {
        return lastDeaths.containsKey(player);
    }

    public static void saveLastDeath(ServerPlayerEntity player, GlobalPos pos) {
        lastDeaths.put(player, pos);
    }

    public static ServerWorld getWorld(ServerPlayerEntity player) {
        GlobalPos globalPos = lastDeaths.get(player);
        if (globalPos == null) {
            return null;
        }

        RegistryKey<World> dimensionKey = globalPos.dimension();

        return Objects.requireNonNull(player.getServer()).getWorld(dimensionKey);
    }

    public static double getX(ServerPlayerEntity player) {
        return lastDeaths.get(player).pos().getX();
    }

    public static double getY(ServerPlayerEntity player) {
        return lastDeaths.get(player).pos().getY();
    }

    public static double getZ(ServerPlayerEntity player) {
        return lastDeaths.get(player).pos().getZ();
    }

    public static void removeLastDeath(ServerPlayerEntity sender) {
        lastDeaths.remove(sender);
    }
}

