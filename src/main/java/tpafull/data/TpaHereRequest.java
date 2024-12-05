package tpafull.data;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TpaHereRequest {
    private final UUID id;
    private final ServerPlayerEntity sender;

    public TpaHereRequest(ServerPlayerEntity sender) {
        this.id = UUID.randomUUID();
        this.sender = sender;
    }

    public UUID getId() {
        return this.id;
    }

    public ServerPlayerEntity getSender() {
        return this.sender;
    }
}
