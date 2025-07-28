package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.client.gui.widget.OptionSlider;
import net.minecraft.client.gui.components.AbstractSliderButton;

import java.util.Arrays;

public class EnumSlider implements ConfigSlider {
    private final ConfigOption.WidgetSize size;
    private final AbstractSliderButton widget;

    public EnumSlider(WidgetData widgetData) throws IllegalAccessException {
        size = widgetData.configOption().size();

        Object currentOption = widgetData.field().get(widgetData.clientConfig());
        Object[] enumConstants = widgetData.type().getEnumConstants();

        widget = new OptionSlider<>(widgetData.configOption().size().getSize(), ConfigWidget.WIDGET_HEIGHT, (v) -> ConfigWidget.getWidgetText(widgetData.field().getName(), v.toString()), (v) -> {
            try {
                widgetData.field().set(widgetData.clientConfig(), v);

                widgetData.clientConfig().save();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }, currentOption, Arrays.asList(enumConstants));
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
