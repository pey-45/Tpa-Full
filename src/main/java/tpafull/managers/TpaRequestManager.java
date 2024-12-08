package tpafull.managers;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import tpafull.data.TpaMode;
import tpafull.data.TpaRequest;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TpaRequestManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, List<TpaRequest>> tpaRequests = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ServerPlayerEntity, List<TpaRequest>> tpaHereRequests = new ConcurrentHashMap<>();

    public static boolean removePossibleRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        List<TpaRequest> requests = tpaRequests.get(target);

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

        List<TpaRequest> hereRequests = tpaHereRequests.get(target);
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


    public static void removeAllRequestsFrom(ServerPlayerEntity sender) {

        for (HashMap.Entry<ServerPlayerEntity, List<TpaRequest>> entry : tpaRequests.entrySet()) {
            ServerPlayerEntity target = entry.getKey();
            List<TpaRequest> requests = entry.getValue();
            requests.removeIf(request -> request.getSender() == sender);
            if (requests.isEmpty()) {
                tpaRequests.remove(target);
            }
        }

        for (HashMap.Entry<ServerPlayerEntity, List<TpaRequest>> entry : tpaHereRequests.entrySet()) {
            ServerPlayerEntity target = entry.getKey();
            List<TpaRequest> requests = entry.getValue();
            requests.removeIf(request -> request.getSender() == sender);
            if (requests.isEmpty()) {
                tpaHereRequests.remove(target);
            }
        }
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
        for (List<TpaRequest> requests : tpaRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
        for (List<TpaRequest> requests : tpaHereRequests.values()) {
            requests.removeIf(targetTpaRequest -> targetTpaRequest.getSender() == sender);
        }
    }
}

