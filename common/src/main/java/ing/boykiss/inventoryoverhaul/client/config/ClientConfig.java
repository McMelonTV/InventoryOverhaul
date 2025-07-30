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

    @ConfigOption(size = ConfigOption.WidgetSize.BIG, slider = true)
    public HotbarAnchorX hotbarAnchorX = HotbarAnchorX.CENTER;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarOffsetX = 1;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarPaddingX = 0;

    @ConfigOption(size = ConfigOption.WidgetSize.BIG, slider = true)
    public HotbarAnchorY hotbarAnchorY = HotbarAnchorY.BOTTOM;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarOffsetY = 1;

    @ConfigOption(size = ConfigOption.WidgetSize.TINY)
    public int hotbarPaddingY = 0;

    @ConfigOption(size = ConfigOption.WidgetSize.FULL, slider = true, min = 0.5d, max = 2.0d, sliderStep = 0.05d)
    public double hotbarScale = 1.0d;

    public enum HotbarScrollDirection {
        ROW,
        COLUMN
    }

    public enum HotbarScrollMode {
        CONTINUOUS,
        SPLIT
    }

    public enum HotbarAnchorX {
        LEFT,
        LEFT_CENTER,
        CENTER,
        RIGHT_CENTER,
        RIGHT
    }

    public enum HotbarAnchorY {
        TOP,
        TOP_CENTER,
        CENTER,
        BOTTOM_CENTER,
        BOTTOM
    }
}
