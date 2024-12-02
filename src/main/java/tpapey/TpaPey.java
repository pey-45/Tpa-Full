package tpapey;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class TpaPey implements ModInitializer {
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> TpaCommand.registerCommands(dispatcher));
	}
}
