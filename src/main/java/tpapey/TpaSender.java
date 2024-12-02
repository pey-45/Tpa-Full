package tpapey;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.UnknownCustomPayload;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TpaSender {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"))))
        );

        dispatcher.register(CommandManager.literal("tpaccept")
                .executes(context -> acceptTpaRequest(context.getSource()))
        );

        dispatcher.register(CommandManager.literal("tpadeny")
                .executes(context -> denyTpaRequest(context.getSource()))
        );
    }

    private static int sendTpaRequest(ServerCommandSource source, ServerPlayerEntity target) {
        TpaReceiver.sendRequest(source.getPlayer(), target);
        source.sendFeedback(() -> Text.literal("Solicitud enviada a " + target.getName().getString()), false);
        TpaPey.sendPopup(target);
        return 1;
    }

    private static int acceptTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.acceptRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Solicitud aceptada."), false);
        } else {
            source.sendFeedback(() -> Text.literal("No tienes solicitudes pendientes."), false);
        }
        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source) {
        boolean success = TpaReceiver.denyRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Solicitud rechazada."), false);
        } else {
            source.sendFeedback(() -> Text.literal("No tienes solicitudes pendientes."), false);
        }
        return 1;
    }
}
