package tpapey;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tpapey.ui.ConfirmationPopup;


public class TpaPeyClient implements ClientModInitializer {
    public static final Identifier RECEIVE_POPUP_PACKET_ID = Identifier.of("tpapey", "receive_popup");
    public static final Identifier RESPONSE_POPUP_PACKET_ID = Identifier.of("tpapey", "response_popup");

    @Override
    public void onInitializeClient() {

        ClientPlayNetworking.registerGlobalReceiver(new CustomPayload.Id<UnknownCustomPayload>(RECEIVE_POPUP_PACKET_ID), (payload, context) -> {
            MinecraftClient client = context.client();

            client.execute(() -> client.setScreen(new ConfirmationPopup(Text.literal("TP?"), null, null)));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TpaSender.registerCommands(dispatcher));
    }

    public static void sendPopupResponse() {
        ClientPlayNetworking.send(new UnknownCustomPayload(RESPONSE_POPUP_PACKET_ID));
    }
}

