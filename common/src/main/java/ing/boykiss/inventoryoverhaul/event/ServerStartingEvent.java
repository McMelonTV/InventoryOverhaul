package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.common.LifecycleEvent;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;

public class ServerStartingEvent {
    public static void init() {
        LifecycleEvent.SERVER_STARTING.register(server -> {
            InventoryOverhaul.server = server;
        });
    }
}
