package tpafull.managers;

import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import tpafull.data.TpaMode;
import tpafull.data.TpaRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TpaRequestManager {
    private static final ConcurrentHashMap<ServerPlayerEntity, List<TpaRequest>> tpaRequests = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<ServerPlayerEntity, List<TpaRequest>> tpaHereRequests = new ConcurrentHashMap<>();

    public static boolean removeRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        List<TpaRequest> requests = tpaRequests.get(target);
        if (requests != null) {
            if (requests.removeIf(request -> request.getSender() == sender)) {
                return true;
            }
        }

        List<TpaRequest> hereRequests = tpaHereRequests.get(target);
        if (hereRequests != null) {
            return hereRequests.removeIf(request -> request.getSender() == sender);
        }

        return false;
    }

    public static void removeAnyRequestBy(ServerPlayerEntity sender) {
        // Iterate every player
        for (HashMap.Entry<ServerPlayerEntity, List<TpaRequest>> entry : tpaRequests.entrySet()) {
            ServerPlayerEntity target = entry.getKey();
            List<TpaRequest> requests = entry.getValue();
            // For every player, removes every request sent by sender
            requests.removeIf(request -> request.getSender() == sender);
            if (requests.isEmpty()) {
                tpaRequests.remove(target);
            }
        }

        // Same for tpahere
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
        List<TpaRequest> requests = (mode == TpaMode.TPA ? tpaRequests : tpaHereRequests).computeIfAbsent(target, k -> new ArrayList<>());
        requests.add(new TpaRequest(sender, mode));
    }


    public static TpaMode getModeFromRequest(ServerPlayerEntity sender, ServerPlayerEntity target) {
        List<TpaRequest> targetTpaRequests = tpaRequests.get(target);
        List<TpaRequest> targetTpaHereRequests = tpaHereRequests.get(target);

        if (targetTpaRequests != null) {
            for (TpaRequest request : tpaRequests.get(target)) {
                if (request.getSender() == sender) {
                    return request.getMode();
                }
            }
        }
        else if (targetTpaHereRequests != null) {
            for (TpaRequest request : tpaHereRequests.get(target)) {
                if (request.getSender() == sender) {
                    return request.getMode();
                }
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

