package tpafull;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tpafull.commands.*;
import tpafull.events.PlayerDeathEvent;
import tpafull.events.ServerStoppedEvent;
import tpafull.managers.AutoTpaManager;
import tpafull.managers.HomeManager;
import tpafull.managers.TpaBlockManager;

public class TpaFull implements ModInitializer {
	@Override
	public void onInitialize() {
		PlayerDeathEvent.initialize();
		ServerStoppedEvent.initialize();

		HomeManager.loadData();
		AutoTpaManager.loadData();
		TpaBlockManager.loadData();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			TpaRequestCommands.register(dispatcher);
			AutoTpaCommands.register(dispatcher);
			BlockTpaCommands.register(dispatcher);
			UndoTpCommands.register(dispatcher);
			LastDeathCommands.register(dispatcher);
			HomeCommands.register(dispatcher);
		});
	}
}
