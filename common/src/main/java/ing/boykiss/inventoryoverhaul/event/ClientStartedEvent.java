package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import ing.boykiss.inventoryoverhaul.client.config.AbstractClientConfig;

public class ClientStartedEvent {
    public static void init() {
        ClientLifecycleEvent.CLIENT_STARTED.register(minecraft -> AbstractClientConfig.init());
    }
}
