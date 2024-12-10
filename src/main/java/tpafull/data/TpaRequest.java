package tpafull.data;

import net.minecraft.server.network.ServerPlayerEntity;

public record TpaRequest(ServerPlayerEntity sender, TpaMode mode) {
}
