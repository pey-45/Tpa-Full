package tpafull.data;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class TpaRequest {
    private final ServerPlayerEntity sender;
    private final TpaMode mode;

    public TpaRequest(ServerPlayerEntity sender, TpaMode mode) {
        this.sender = sender;
        this.mode = mode;
    }

    public ServerPlayerEntity getSender() {
        return this.sender;
    }

    public TpaMode getMode() {
        return this.mode;
    }
}
