package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import ing.boykiss.inventoryoverhaul.client.gui.widget.OptionSlider;

import java.util.Arrays;

public class EnumSlider<T extends Enum<?>> implements ConfigSlider {
    private final ConfigOption.WidgetSize size;
    private final OptionSlider<T> widget;

    public EnumSlider(WidgetData widgetData) throws IllegalAccessException {
        size = widgetData.configOption().size();

        @SuppressWarnings("unchecked")
        T currentOption = (T) widgetData.field().get(widgetData.clientConfig());
        @SuppressWarnings("unchecked")
        T[] enumConstants = (T[]) widgetData.type().getEnumConstants();

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
    public OptionSlider<T> getWidget() {
        return widget;
    }
}
