package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Checkbox;

public class BooleanCheckbox implements ConfigCheckbox {
    private final ConfigOption.WidgetSize size;
    private final Checkbox widget;

    public BooleanCheckbox(WidgetData widgetData) throws IllegalAccessException {
        size = widgetData.configOption().size();
        widget = Checkbox.builder(ConfigWidget.getWidgetText(widgetData.field().getName(), widgetData.field().get(widgetData.clientConfig()).toString()), Minecraft.getInstance().font).maxWidth(widgetData.configOption().size().getSize()).build();
        // TODO
    }

    @Override
    public ConfigOption.WidgetSize getSize() {
        return size;
    }

    @Override
    public Checkbox getWidget() {
        return widget;
    }
}
