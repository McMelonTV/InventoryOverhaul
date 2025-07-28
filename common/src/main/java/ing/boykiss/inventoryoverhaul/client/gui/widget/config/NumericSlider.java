package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.client.gui.widget.Slider;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class NumericSlider implements ConfigSlider {
    private final ConfigOption.WidgetSize size;
    private final AbstractSliderButton widget;

    public NumericSlider(WidgetData widgetData, double min, double max, double step) throws IllegalAccessException {
        size = widgetData.configOption().size();
//        widget = Button.builder(ConfigWidget.getWidgetText(widgetData.field().getName(), widgetData.field().get(widgetData.clientConfig()).toString()), (b) -> {
//
//        }).width(widgetData.configOption().size().getSize()).build();
        widget = new Slider(widgetData.configOption().size().getSize(), ConfigWidget.WIDGET_HEIGHT, Component.empty(), 0);
        // TODO
    }

    @Override
    public ConfigOption.WidgetSize getSize() {
        return size;
    }

    @Override
    public AbstractSliderButton getWidget() {
        return widget;
    }
}
