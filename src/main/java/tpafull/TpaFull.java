package tpafull;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import tpafull.commands.CommandRegister;
import tpafull.events.PlayerJoinEventHandler;
import tpafull.utils.GlobalScheduler;

public class TpaFull implements ModInitializer {
	@Override
	public void onInitialize() {
		PlayerJoinEventHandler.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				CommandRegister.registerCommands(dispatcher));

		ServerLifecycleEvents.SERVER_STOPPED.register(server ->
				GlobalScheduler.shutdown());
	}
}
