package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;

public class NumericInput implements ConfigInput {
    private final ConfigOption.WidgetSize size;
    private final EditBox widget;

    public NumericInput(WidgetData widgetData, double min, double max) throws IllegalAccessException {
        size = widgetData.configOption().size();
        widget = new EditBox(Minecraft.getInstance().font, widgetData.configOption().size().getSize(), WIDGET_HEIGHT, ConfigWidget.getWidgetText(widgetData.field().getName()));
        // TODO
    }

    @Override
    public ConfigOption.WidgetSize getSize() {
        return size;
    }

    @Override
    public EditBox getWidget() {
        return widget;
    }
}
