package tpafull.managers;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class LastTpManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, GlobalPos> lastTps = new ConcurrentHashMap<>();

    public static void saveLastTp(ServerPlayerEntity player, GlobalPos pos) {
        lastTps.put(player, pos);
    }

    public static boolean hasLastTp(ServerPlayerEntity player) {
        return lastTps.containsKey(player);
    }

    public static ServerWorld getWorld(ServerPlayerEntity player) {
        GlobalPos globalPos = lastTps.get(player);
        if (globalPos == null) {
            return null;
        }

        RegistryKey<World> dimensionKey = globalPos.dimension();

        return Objects.requireNonNull(player.getServer()).getWorld(dimensionKey);
    }

    public static double getX(ServerPlayerEntity player) {
        return lastTps.get(player).pos().getX();
    }

    public static double getY(ServerPlayerEntity player) {
        return lastTps.get(player).pos().getY();
    }

    public static double getZ(ServerPlayerEntity player) {
        return lastTps.get(player).pos().getZ();
    }

    public static void removeLastTp(ServerPlayerEntity sender) {
        lastTps.remove(sender);
    }
}

