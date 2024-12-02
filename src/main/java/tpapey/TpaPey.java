package tpapey;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TpaPey implements ModInitializer {
	public static final Identifier RECEIVE_POPUP_PACKET_ID = Identifier.of("tpapey", "receive_popup");
	public static final Identifier RESPONSE_POPUP_PACKET_ID = Identifier.of("tpapey", "response_popup");


	@Override
	public void onInitialize() {
		ServerPlayNetworking.registerGlobalReceiver(new CustomPayload.Id<UnknownCustomPayload>(RESPONSE_POPUP_PACKET_ID), (payload, context) -> {
			ServerPlayerEntity player = context.player();
			MinecraftServer server = context.server();

			server.execute(() -> player.sendMessage(Text.literal("¡Paquete recibido!"), false));
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TpaSender.registerCommands(dispatcher);
		});
	}

	public static void sendPopup(ServerPlayerEntity player) {
		ServerPlayNetworking.send(player, new UnknownCustomPayload(RECEIVE_POPUP_PACKET_ID));
	}
}
