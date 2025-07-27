package ing.boykiss.inventoryoverhaul.client.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.util.annotations.RequireFieldAnnotation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RequireFieldAnnotation(ConfigOption.class)
public class ClientConfig extends AbstractClientConfig {
    @ConfigOption(size = ConfigOption.ButtonSize.BIG, gridY = 0)
    public HotbarScrollDirection hotbarScrollDirection = HotbarScrollDirection.ROW;
    @ConfigOption(size = ConfigOption.ButtonSize.BIG, gridY = 1)
    public HotbarScrollMode hotbarScrollMode = HotbarScrollMode.CONTINUOUS;

    public enum HotbarScrollDirection {
        ROW,
        COLUMN
    }

    public enum HotbarScrollMode {
        CONTINUOUS,
        SPLIT
    }
}
