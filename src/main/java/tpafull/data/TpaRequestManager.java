package tpafull.data;

import net.minecraft.server.network.ServerPlayerEntity;
import tpafull.events.PlayerEventHandler;

import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TpaRequestManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> tpaRequests = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> tpaHereRequests = new ConcurrentHashMap<>();

    public static boolean removeRequestsSentBy(Deque<TpaRequest> requests, ServerPlayerEntity sender) {
        return requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
    }

    public static void cleanAllRequestsSentBy(ServerPlayerEntity sender) {
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaHereRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
    }

    public static Deque<TpaRequest> getRequestsFrom(ServerPlayerEntity player, TpaMode mode) {
        return (mode == TpaMode.TPA ? tpaRequests : tpaHereRequests).computeIfAbsent(player, k -> new ConcurrentLinkedDeque<>());
    }

    public static TpaRequest findRequest(ServerPlayerEntity player) {
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaRequests.values()) {
            for (TpaRequest request : requests) {
                if (request.getSender() == sender) {
                    return request;
                }
            }
        }
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaHereRequests.values()) {
            for (TpaRequest request : requests) {
                if (request.getSender() == sender) {
                    return request;
                }
            }
        }

        return null;
    }

    public static TpaRequest removeRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaRequests.values()) {
            for (TpaRequest request : requests) {
                if (request.getSender() == sender) {
                    requests.remove(request);
                }
            }
        }
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaHereRequests.values()) {
            for (TpaRequest request : requests) {
                if (request.getSender() == sender) {
                    return request;
                }
            }
        }

        return null;
    }
}

