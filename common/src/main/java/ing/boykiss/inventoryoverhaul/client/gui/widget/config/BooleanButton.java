package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.gui.components.Button;

public class BooleanButton implements ConfigButton {
    private final ConfigOption.WidgetSize size;
    private final Button widget;

    public BooleanButton(WidgetData widgetData) throws IllegalAccessException {
        size = widgetData.configOption().size();
        widget = Button.builder(ConfigWidget.getWidgetText(widgetData.field().getName(), widgetData.field().get(widgetData.clientConfig()).toString()), (b) -> {

        }).width(widgetData.configOption().size().getSize()).build();
        // TODO
    }

    @Override
    public ConfigOption.WidgetSize getSize() {
        return size;
    }

    @Override
    public Button getWidget() {
        return widget;
    }
}
