package tpafull.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import tpafull.managers.LastDeathManager;

public class PlayerDeathEvent {
    public static void initialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {

            if (entity instanceof ServerPlayerEntity player) {
                GlobalPos pos = new GlobalPos(player.getServerWorld().getRegistryKey(), new BlockPos(player.getBlockPos()));
                LastDeathManager.saveLastDeath(player, pos);
            }
        });
    }
}
