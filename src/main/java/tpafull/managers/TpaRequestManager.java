package tpafull.managers;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import tpafull.data.TpaMode;
import tpafull.data.TpaRequest;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TpaRequestManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> tpaRequests = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> tpaHereRequests = new ConcurrentHashMap<>();

    public static boolean removePossibleRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        ConcurrentLinkedDeque<TpaRequest> requests = tpaRequests.get(target);

        if (requests != null) {
            for (TpaRequest request : requests) {
                if (request.getSender() == sender) {
                    requests.remove(request);
                    if (requests.isEmpty()) {
                        tpaRequests.remove(target);
                    }
                    return true;
                }
            }
        }

        ConcurrentLinkedDeque<TpaRequest> hereRequests = tpaHereRequests.get(target);
        if (hereRequests != null) {
            for (TpaRequest request : hereRequests) {
                if (request.getSender() == sender) {
                    hereRequests.remove(request);
                    if (hereRequests.isEmpty()) {
                        tpaHereRequests.remove(target);
                    }
                    return true;
                }
            }
        }

        return false;
    }


    public static boolean removeAllRequestsFrom(ServerPlayerEntity sender) {

        for (HashMap.Entry<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> entry : tpaRequests.entrySet()) {
            ServerPlayerEntity target = entry.getKey();
            ConcurrentLinkedDeque<TpaRequest> requests = entry.getValue();
            requests.removeIf(request -> request.getSender() == sender);
            if (requests.isEmpty()) {
                tpaRequests.remove(target);
            }
            return true;
        }

        for (HashMap.Entry<ServerPlayerEntity, ConcurrentLinkedDeque<TpaRequest>> entry : tpaHereRequests.entrySet()) {
            ServerPlayerEntity target = entry.getKey();
            ConcurrentLinkedDeque<TpaRequest> requests = entry.getValue();
            requests.removeIf(request -> request.getSender() == sender);
            if (requests.isEmpty()) {
                tpaHereRequests.remove(target);
            }
            return true;
        }

        return false;
    }


    public static void addRequest(ServerPlayerEntity sender, ServerPlayerEntity target, TpaMode mode) {
        ConcurrentLinkedDeque<TpaRequest> requests = (mode == TpaMode.TPA ? tpaRequests : tpaHereRequests).computeIfAbsent(target, k -> new ConcurrentLinkedDeque<>());
        requests.add(new TpaRequest(sender, mode));
    }


    public static @Nullable TpaMode getModeFromRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        for (TpaRequest request : tpaRequests.get(target)) {
            if (request.getSender() == sender) {
                return request.getMode();
            }
        }
        for (TpaRequest request : tpaHereRequests.get(target)) {
            if (request.getSender() == sender) {
                return request.getMode();
            }
        }
        return null;
    }

    public static void cleanAllRequests(ServerPlayerEntity sender) {
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
        for (ConcurrentLinkedDeque<TpaRequest> requests : tpaHereRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
    }
}

