package tpapey;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.CustomPayload;

public class TpaPeyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(new CustomPayload.Id<>(TpaPey.SHOW_POPUP_PACKET_ID), (payload, context) -> {
            ClientPlayerEntity player = context.player();

            // TO DO
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TpaSender.registerCommands(dispatcher);
        });
    }
}

