package tpapey.commands;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import tpapey.data.TpaMode;
import tpapey.data.TpaRequest;

import java.util.HashMap;

public class TpaReceiver {
    private static final HashMap<ServerPlayerEntity, TpaRequest> tpaRequests = new HashMap<>();

    public static void sendTpaRequest(ServerPlayerEntity sender, ServerPlayerEntity target, TpaMode mode) {
        String message = mode == TpaMode.TPA ? " wants to teleport to you (tpa)" : " wants you to teleport to him (tpahere)";

        tpaRequests.put(target, new TpaRequest(sender, mode));

        target.sendMessage(Text.literal(sender.getName().getString())
                        .styled(style -> style
                                .withColor(Formatting.AQUA))
                        .append(Text.literal(message)
                                .styled(style -> style
                                        .withColor(Formatting.WHITE)
                                )
                        )
                        .append(Text.literal("\n"))
                        .append(
                                Text.literal("[Accept]")
                                        .styled(style -> style
                                                .withColor(Formatting.GREEN)
                                                .withBold(true)
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to accept")))
                                        )
                        )
                        .append(" ")
                        .append(
                                Text.literal("[Deny]")
                                        .styled(style -> style
                                                .withColor(Formatting.RED)
                                                .withBold(true)
                                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"))
                                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to reject")))
                                        )
                        ),
                false
        );

    }

    public static boolean acceptRequest(ServerPlayerEntity target) {
        TpaRequest request = tpaRequests.get(target);
        if (request == null) {
            return false;
        }

        TpaMode mode = request.mode();
        ServerPlayerEntity sender = tpaRequests.remove(target).sender();

        if (mode == TpaMode.TPA) {
            sender.teleport((ServerWorld) target.getWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
        } else {
            target.teleport((ServerWorld) sender.getWorld(), sender.getX(), sender.getY(), sender.getZ(), sender.getYaw(), sender.getPitch());
        }

        return true;
    }

    public static boolean denyRequest(ServerPlayerEntity target) {
        return tpaRequests.remove(target) != null;
    }

    public static void doTpaHome(ServerPlayerEntity sender, BlockPos target) {
        sender.teleport(sender.getServerWorld(), target.getX(), target.getY(), target.getZ(), sender.getYaw(), sender.getPitch());
    }
}
