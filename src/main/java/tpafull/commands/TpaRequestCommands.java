package tpafull.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tpafull.data.TpaMode;
import tpafull.managers.TpaAutoManager;
import tpafull.managers.TpaBlockManager;
import tpafull.managers.TpaRequestManager;
import tpafull.utils.GlobalScheduler;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TpaRequestCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tpa")
                .then(CommandManager.argument("receiver", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(
                                Objects.requireNonNull(context.getSource().getPlayer()),
                                EntityArgumentType.getPlayer(context, "receiver"),
                                TpaMode.TPA))));

        dispatcher.register(CommandManager.literal("tpahere")
                .then(CommandManager.argument("receiver", EntityArgumentType.player())
                        .executes(context -> sendTpaRequest(
                                Objects.requireNonNull(context.getSource().getPlayer()),
                                EntityArgumentType.getPlayer(context, "receiver"),
                                TpaMode.TPAHERE))));

        dispatcher.register(CommandManager.literal("tpaccept")
                .then(CommandManager.argument("requester", EntityArgumentType.player())
                        .executes(context -> acceptTpaRequest(
                                context.getSource().getPlayer(),
                                EntityArgumentType.getPlayer(context, "requester")))));

        dispatcher.register(CommandManager.literal("tpadeny")
                .then(CommandManager.argument("requester", EntityArgumentType.player())
                        .executes(context -> denyTpaRequest(
                                context.getSource().getPlayer(),
                                EntityArgumentType.getPlayer(context, "requester")))));
    }


    private static int sendTpaRequest(ServerPlayerEntity sender, ServerPlayerEntity receiver, TpaMode mode) {
       if (Objects.equals(sender, receiver)) {
            sender.sendMessage(Text.literal("Try with someone else")
                    .styled(style -> style
                            .withColor(Formatting.RED)));
            return -1;
       }

       // Verify if not blocked
       Set<String> receiverBlocks = TpaBlockManager.getBlocks(receiver);
       if (receiverBlocks != null && receiverBlocks.contains(sender.getName().getString())) {
           sender.sendMessage(Text.literal("You are blocked by " + receiver.getName().getString())
                   .styled(style -> style
                           .withColor(Formatting.RED)));
           return -1;
       }


        // Teleport if request is TPA and sender is in receiver's auto tpa list
        Set<String> receiverAutoTpaers = TpaAutoManager.getAllowed(receiver);
        if (receiverAutoTpaers != null && receiverAutoTpaers.contains(sender.getName().getString()) && mode == TpaMode.TPA) {
            sender.teleport(receiver.getServerWorld(), receiver.getX(), receiver.getY(), receiver.getZ(), receiver.getYaw(), receiver.getPitch());

            sender.sendMessage(Text.literal("Teleporting...")
                    .styled(style -> style
                            .withColor(Formatting.GREEN)));

            receiver.sendMessage(Text.literal(sender.getName().getString())
                    .styled(style -> style
                            .withColor(Formatting.AQUA))
                    .append(Text.literal(" automatically teleported to you!")));

            return 1;
        }

        // Add new request to the receiver after cleaning any other request by sender
        TpaRequestManager.removeAnyRequestBy(sender);
        TpaRequestManager.addRequest(sender, receiver, mode);

        // Schedule timeout
        GlobalScheduler.schedule(() -> {
            if (TpaRequestManager.removeRequest(sender, receiver)) {
                sender.sendMessage(Text.literal(mode + " request to " + receiver.getName().getString() + " timed out")
                        .styled(style -> style
                                .withColor(Formatting.RED)));

                receiver.sendMessage(Text.literal(mode + " request from " + sender.getName().getString() + " timed out")
                        .styled(style -> style
                                .withColor(Formatting.RED)));
            }
        }, 60, TimeUnit.SECONDS);

        // Send form to receiver
        String message = mode == TpaMode.TPA ? " wants to teleport to you!" : " wants you to teleport to him!";
        receiver.sendMessage(Text.literal(sender.getName().getString())
                        .styled(style -> style
                                .withColor(Formatting.AQUA))
                        .append(Text.literal(message)
                                .styled(style -> style
                                        .withColor(Formatting.WHITE)))
                        .append(Text.literal("\n[Accept] ")
                                .styled(style -> style
                                        .withColor(Formatting.GREEN)
                                        .withBold(true)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept " + sender.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to accept")))))
                        .append(Text.literal("[Deny]")
                                .styled(style -> style
                                        .withColor(Formatting.RED)
                                        .withBold(true)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny " + sender.getName().getString()))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to reject"))))));

        sender.sendMessage(Text.literal(mode + " request sent to " + receiver.getName().getString()));

        return 1;
    }


    private static int acceptTpaRequest(ServerPlayerEntity acceptor, ServerPlayerEntity requester) {
        // Gets the mode while verifying if the request exists
        TpaMode mode = TpaRequestManager.getModeFromRequest(requester, acceptor);
        if (mode == null) {
            acceptor.sendMessage(Text.literal("No pending requests from ")
                    .styled(style -> style
                            .withColor(Formatting.RED))
                    .append(Text.literal(requester.getName().getString())
                            .styled(style -> style
                                    .withColor(Formatting.AQUA))));
            return -1;
        }

        requester.sendMessage(Text.literal(acceptor.getName().getString() + " accepted your request!")
                .styled(style -> style
                        .withColor(Formatting.GREEN)));

        acceptor.sendMessage(Text.literal("Request accepted"));

        // Teleport
        if (TpaRequestManager.getModeFromRequest(requester, acceptor) == TpaMode.TPA) {
            requester.teleport(acceptor.getServerWorld(), acceptor.getX(), acceptor.getY(), acceptor.getZ(), acceptor.getYaw(), acceptor.getPitch());
            requester.sendMessage(Text.literal("Teleporting..."));
        } else if (TpaRequestManager.getModeFromRequest(requester, acceptor) == TpaMode.TPAHERE) {
            acceptor.teleport(requester.getServerWorld(), requester.getX(), requester.getY(), requester.getZ(), requester.getYaw(), requester.getPitch());
            acceptor.sendMessage(Text.literal("Teleporting..."));
        }

        // Remove the request
        TpaRequestManager.removeRequest(requester, acceptor);

        return 1;
    }

    private static int denyTpaRequest(ServerPlayerEntity acceptor, ServerPlayerEntity requester) {
        // Request does no longer exist
        if (!TpaRequestManager.removeRequest(requester, acceptor)) {
            acceptor.sendMessage(Text.literal("No pending requests"));
            return -1;
        }

        requester.sendMessage(Text.literal(acceptor.getName().getString() + " rejected your request")
                .styled(style -> style
                        .withColor(Formatting.RED)));

        acceptor.sendMessage(Text.literal("Request rejected"));

        return 1;
    }
}


