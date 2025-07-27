package ing.boykiss.inventoryoverhaul.client.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.util.annotations.RequireFieldAnnotation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RequireFieldAnnotation(ConfigOption.class)
public class ClientConfig extends AbstractClientConfig {
    @ConfigOption
    public HotbarScrollDirection hotbarScrollDirection = HotbarScrollDirection.ROW;

    @ConfigOption
    public HotbarScrollMode hotbarScrollMode = HotbarScrollMode.CONTINUOUS;

    @ConfigOption(size = ConfigOption.WidgetSize.BIG)
    public PositionAnchorX hotbarAnchorX = PositionAnchorX.RIGHT;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarOffsetX = 0;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarPaddingX = 10;

    @ConfigOption(size = ConfigOption.WidgetSize.BIG)
    public PositionAnchorY hotbarAnchorY = PositionAnchorY.BOTTOM;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarOffsetY = 0;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarPaddingY = 10;

    public enum HotbarScrollDirection {
        ROW,
        COLUMN
    }

    public enum HotbarScrollMode {
        CONTINUOUS,
        SPLIT
    }

    public enum PositionAnchorX {
        LEFT,
        LEFT_CENTER,
        CENTER,
        RIGHT_CENTER,
        RIGHT
    }

    public enum PositionAnchorY {
        TOP,
        TOP_CENTER,
        CENTER,
        BOTTOM_CENTER,
        BOTTOM
    }
}
