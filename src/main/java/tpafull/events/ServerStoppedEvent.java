package tpafull.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import tpafull.utils.GlobalScheduler;

public class ServerStoppedEvent {
    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server ->
                GlobalScheduler.shutdown());
    }
}
