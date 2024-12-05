package tpafull.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import tpafull.data.PlayerManager;

public class PlayerJoinEventHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            PlayerManager.setName(player, player.getName().getString());

        });
    }
}