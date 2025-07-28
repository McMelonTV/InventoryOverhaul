package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;

// TODO: Untested
public class NumericInput implements ConfigInput {
    private final ConfigOption.WidgetSize size;
    private final EditBox widget;

    public NumericInput(WidgetData widgetData, double min, double max) throws IllegalAccessException {
        size = widgetData.configOption().size();
        double value = Double.parseDouble(widgetData.field().get(widgetData.clientConfig()).toString());
        widget = new EditBox(Minecraft.getInstance().font, widgetData.configOption().size().getSize(), WIDGET_HEIGHT, ConfigWidget.getWidgetText(widgetData.field().getName()));
        if (widgetData.type() == short.class || widgetData.type() == Short.class ||
                widgetData.type() == int.class || widgetData.type() == Integer.class ||
                widgetData.type() == long.class || widgetData.type() == Long.class) {
            widget.setValue(Long.toString((long) value));
        } else {
            widget.setValue(Double.toString(value));
        }
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
