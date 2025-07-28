package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.client.gui.widget.Slider;
import net.minecraft.client.gui.components.AbstractSliderButton;

// TODO: Untested
public class NumericSlider implements ConfigSlider {
    private final ConfigOption.WidgetSize size;
    private final AbstractSliderButton widget;

    public NumericSlider(WidgetData widgetData, double min, double max, double step) throws IllegalAccessException {
        size = widgetData.configOption().size();
        double value = Double.parseDouble(widgetData.field().get(widgetData.clientConfig()).toString());
        widget = new Slider(widgetData.configOption().size().getSize(), ConfigWidget.WIDGET_HEIGHT, (v) -> ConfigWidget.getWidgetText(widgetData.field().getName(), Double.toString(v)), (v) -> {
            try {
                widgetData.field().set(widgetData.clientConfig(), v);

                widgetData.clientConfig().save();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, value, min, max, step);
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
