package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class ErrorWidget implements ConfigWidget<StringWidget> {
    private final ConfigOption.WidgetSize size;
    private final StringWidget widget;

    public ErrorWidget(ConfigOption configOption) {
        widget = new StringWidget(configOption.size().getSize(), WIDGET_HEIGHT, Component.literal("Error"), Minecraft.getInstance().font).setColor(0xFFFF0000);
        size = configOption.size();
    }

    @Override
    public ConfigOption.WidgetSize getSize() {
        return size;
    }

    @Override
    public StringWidget getWidget() {
        return widget;
    }
}
