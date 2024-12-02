package tpapey;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class TpaReceiver {
    private static final Map<ServerPlayerEntity, ServerPlayerEntity> tpaRequests = new HashMap<>();

    public static void sendRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        tpaRequests.put(target, sender);
        target.sendMessage(Text.literal(sender.getName().getString() + " quiere teletransportarse a ti. Usa /tpaccept o /tpdeny."), false);
    }

    public static boolean acceptRequest(ServerPlayerEntity target) {
        ServerPlayerEntity sender = tpaRequests.remove(target);
        if (sender != null) {
            sender.teleport((ServerWorld) target.getWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
            return true;
        }
        return false;
    }

    public static boolean denyRequest(ServerPlayerEntity target) {
        ServerPlayerEntity sender = tpaRequests.remove(target);
        if (sender != null) {
            sender.sendMessage(Text.literal("Tu solicitud de teletransporte fue rechazada."), false);
            return true;
        }
        return false;
    }
}
