package tpafull.data;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TpaRequest {
    private final UUID id;
    private final ServerPlayerEntity sender;

    public TpaRequest(ServerPlayerEntity sender) {
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