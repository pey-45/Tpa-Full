package tpafull;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityCombatEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import tpafull.commands.CommandRegister;
import tpafull.managers.LastDeathManager;
import tpafull.managers.LastTpManager;
import tpafull.utils.GlobalScheduler;

public class TpaFull implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				CommandRegister.registerCommands(dispatcher));

		ServerLifecycleEvents.SERVER_STOPPED.register(server ->
				GlobalScheduler.shutdown());

		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (entity instanceof ServerPlayerEntity player) {
				GlobalPos pos = new GlobalPos(player.getServerWorld().getRegistryKey(), new BlockPos(player.getBlockPos()));
				LastDeathManager.saveLastDeath(player, pos);
			}
		});
	}
}
