package tpapey;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class TpaCommand {
    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("voy")
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(context.getSource(), EntityArgumentType.getPlayer(context, "target"))))
        );

        dispatcher.register(CommandManager.literal("va")
                .executes(context -> acceptTpaRequest(context.getSource()))
        );

        dispatcher.register(CommandManager.literal("ok")
                .executes(context -> denyTpaRequest(context.getSource()))
        );
    }

    private static int sendTpaRequest(ServerCommandSource source, ServerPlayerEntity target) {
        TpaManager.sendRequest(source.getPlayer(), target);
        source.sendFeedback(() -> Text.literal("Solicitud enviada a " + target.getName().getString()), false);
        return 1;
    }

    private static int acceptTpaRequest(ServerCommandSource source) {
        boolean success = TpaManager.acceptRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Solicitud aceptada."), false);
        } else {
            source.sendFeedback(() -> Text.literal("No tienes solicitudes pendientes."), false);
        }
        return 1;
    }

    private static int denyTpaRequest(ServerCommandSource source) {
        boolean success = TpaManager.denyRequest(source.getPlayer());
        if (success) {
            source.sendFeedback(() -> Text.literal("Solicitud rechazada."), false);
        } else {
            source.sendFeedback(() -> Text.literal("No tienes solicitudes pendientes."), false);
        }
        return 1;
    }
}

