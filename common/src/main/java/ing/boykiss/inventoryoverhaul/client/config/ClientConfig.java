package ing.boykiss.inventoryoverhaul.client.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientConfig extends AbstractClientConfig {
    private HotbarScrollDirection hotbarScrollDirection = HotbarScrollDirection.HORIZONTAL;
    private HotbarScrollMode hotbarScrollMode = HotbarScrollMode.CONTINUOUS;

    public enum HotbarScrollDirection {
        HORIZONTAL,
        VERTICAL
    }

    public enum HotbarScrollMode {
        CONTINUOUS,
        SPLIT
    }
}
