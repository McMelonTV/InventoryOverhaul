package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public interface ConfigWidget<T extends AbstractWidget> {
    String OPTION_NAME_TRANSLATE_KEY_FORMAT = "options.%s";
    String OPTION_VALUE_TRANSLATE_KEY_FORMAT = "options.%s.%s";
    int WIDGET_HEIGHT = 20;

    static List<? extends ConfigWidget<?>> createAll(ClientConfig clientConfig) {
        Class<?> clientConfigClass = clientConfig.getClass();
        return Arrays.stream(clientConfigClass.getDeclaredFields())
                .map(field -> ConfigWidget.create(field, clientConfig))
                .toList();
    }

    private static ConfigWidget<?> create(Field field, ClientConfig clientConfig) {
        Class<?> type = field.getType();
        ConfigOption configOption = field.getAnnotation(ConfigOption.class);

        WidgetData widgetData = new WidgetData(field, type, configOption, clientConfig);

        boolean checkbox = configOption.checkbox();
        boolean slider = configOption.slider();
        double sliderStep = configOption.sliderStep();
        double min = configOption.min();
        double max = configOption.max();

        try {
            if (type.isEnum()) {
                return slider
                        ? new EnumSlider(widgetData, sliderStep)
                        : new EnumButton(widgetData);
            }

            if (type == short.class || type == Short.class ||
                    type == int.class || type == Integer.class ||
                    type == long.class || type == Long.class ||

                    type == float.class || type == Float.class ||
                    type == double.class || type == Double.class) {
                return (slider && !Double.isNaN(min) && !Double.isNaN(max))
                        ? new NumericSlider(widgetData, min, max, sliderStep)
                        : new NumericInput(widgetData, min, max);
            }

            if (type == String.class) {
                return new StringInput(widgetData);
            }

            if (type == boolean.class || type == Boolean.class) {
                return checkbox
                        ? new BooleanCheckbox(widgetData)
                        : new BooleanButton(widgetData);
            }

            InventoryOverhaul.LOGGER.error("config widget for type {} not implemented", type.getName());
        } catch (IllegalAccessException e) {
            InventoryOverhaul.LOGGER.error(e.getMessage());
        }

        return new ErrorWidget(configOption);
    }

    static Component getWidgetText(String optionName, String optionValue) {
        return Component.translatable(getOptionNameTranslateKey(optionName), Component.translatable(getOptionValueTranslateKey(optionName, optionValue)));
    }

    static Component getWidgetText(String optionName) {
        return Component.translatable(getOptionNameTranslateKey(optionName));
    }

    private static String getOptionNameTranslateKey(String optionName) {
        return String.format(OPTION_NAME_TRANSLATE_KEY_FORMAT, optionName);
    }

    private static String getOptionValueTranslateKey(String optionName, String optionValue) {
        return String.format(OPTION_VALUE_TRANSLATE_KEY_FORMAT, optionName, optionValue);
    }

    ConfigOption.WidgetSize getSize();

    T getWidget();

    record WidgetData(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig) {
    }
}