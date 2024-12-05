package tpafull.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import tpafull.data.PlayerManager;
import tpafull.data.TpaRequest;
import tpafull.data.TpaRequestManager;

import java.util.Deque;

public class PlayerEventHandler {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            PlayerManager.setName(player, player.getName().getString());
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            TpaRequestManager.removeAllRequestsSentBy(player);
        });
    }
}