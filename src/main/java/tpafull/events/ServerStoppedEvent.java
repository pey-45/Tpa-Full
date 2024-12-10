package tpafull.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import tpafull.managers.TpaAutoManager;
import tpafull.managers.HomeManager;
import tpafull.managers.TpaBlockManager;
import tpafull.utils.GlobalScheduler;

public class ServerStoppedEvent {
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            GlobalScheduler.shutdown();
            HomeManager.saveData();
            TpaAutoManager.saveData();
            TpaBlockManager.saveData();
        });
    }
}
