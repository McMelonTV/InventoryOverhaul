package ing.boykiss.inventoryoverhaul.client.gui.widget;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigWidget<T extends AbstractWidget> {
    private static final String OPTION_NAME_TRANSLATE_KEY_FORMAT = "options.%s";
    private static final String OPTION_VALUE_TRANSLATE_KEY_FORMAT = "options.%s.%s";
    private static final int WIDGET_HEIGHT = 20;

    @Getter
    private final ConfigOption.WidgetSize size;

    @Getter
    private final T widget;

    protected ConfigWidget(ConfigOption.WidgetSize widgetSize, T widget) {
        this.size = widgetSize;
        this.widget = widget;
    }

    public static List<? extends ConfigWidget<?>> createAll(ClientConfig clientConfig) {
        Class<?> clientConfigClass = clientConfig.getClass();
        return Arrays.stream(clientConfigClass.getDeclaredFields())
                .map(field -> ConfigWidget.create(field, clientConfig))
                .filter(Objects::nonNull)
                .toList();
    }

    private static ConfigWidget<?> create(Field field, ClientConfig clientConfig) {
        Class<?> type = field.getType();
        ConfigOption configOption = field.getAnnotation(ConfigOption.class);

        WidgetData widgetData = new WidgetData(field, type, configOption, clientConfig);

        boolean slider = configOption.slider();
        double sliderStep = configOption.sliderStep();
        double min = configOption.min();
        double max = configOption.max();

        try {
            if (type.isEnum()) {
                return slider
                        ? createEnumSlider(widgetData, sliderStep)
                        : createEnumButton(widgetData);
            }

            if (type == short.class || type == Short.class ||
                    type == int.class || type == Integer.class ||
                    type == long.class || type == Long.class ||

                    type == float.class || type == Float.class ||
                    type == double.class || type == Double.class) {
                return (slider && !Double.isNaN(min) && !Double.isNaN(max))
                        ? createNumericSlider(widgetData, min, max, sliderStep)
                        : createNumericInput(widgetData, min, max);
            }

            if (type == String.class) {
                return createStringInput(widgetData);
            }

            InventoryOverhaul.LOGGER.error("config widget for type {} not implemented", type.getName());
        } catch (IllegalAccessException e) {
            InventoryOverhaul.LOGGER.error(e.getMessage());
        }

        return createErrorWidget(configOption);
    }

    private static ConfigWidget<Button> createEnumButton(WidgetData widgetData) throws IllegalAccessException {
        return new ConfigWidget<>(widgetData.configOption.size(), Button.builder(getWidgetText(widgetData.field.getName(), widgetData.field.get(widgetData.clientConfig).toString()), (b) -> {
            try {
                Object currentOption = widgetData.field.get(widgetData.clientConfig);

                Object[] enumConstants = widgetData.type.getEnumConstants();
                int currentIndex = Arrays.asList(enumConstants).indexOf(currentOption);
                int nextIndex = (currentIndex + 1) % enumConstants.length;
                Object next = enumConstants[nextIndex];

                widgetData.field.set(widgetData.clientConfig, next);

                widgetData.clientConfig.save();

                b.setMessage(getWidgetText(widgetData.field.getName(), next.toString()));
            } catch (IllegalAccessException e) {
                InventoryOverhaul.LOGGER.error(e.getMessage());
            }
        }).width(widgetData.configOption.size().getSize()).build());
    }

    private static ConfigWidget<Button> createEnumSlider(WidgetData widgetData, double step) throws IllegalAccessException {
        return new ConfigWidget<>(widgetData.configOption.size(), Button.builder(getWidgetText(widgetData.field.getName(), widgetData.field.get(widgetData.clientConfig).toString()), (b) -> {

        }).width(widgetData.configOption.size().getSize()).build());
    }

    private static ConfigWidget<EditBox> createNumericInput(WidgetData widgetData, double min, double max) throws IllegalAccessException {
        return new ConfigWidget<>(widgetData.configOption.size(), new EditBox(Minecraft.getInstance().font, widgetData.configOption.size().getSize(), WIDGET_HEIGHT, getWidgetText(widgetData.field.getName())));
    }

    private static ConfigWidget<Button> createNumericSlider(WidgetData widgetData, double min, double max, double step) throws IllegalAccessException {
        return new ConfigWidget<>(widgetData.configOption.size(), Button.builder(getWidgetText(widgetData.field.getName(), widgetData.field.get(widgetData.clientConfig).toString()), (b) -> {

        }).width(widgetData.configOption.size().getSize()).build());
    }

    private static ConfigWidget<EditBox> createStringInput(WidgetData widgetData) throws IllegalAccessException {
        return new ConfigWidget<>(widgetData.configOption.size(), new EditBox(Minecraft.getInstance().font, widgetData.configOption.size().getSize(), WIDGET_HEIGHT, getWidgetText(widgetData.field.getName())));
    }

    private static ConfigWidget<StringWidget> createErrorWidget(ConfigOption configOption) {
        return new ConfigWidget<>(configOption.size(), new StringWidget(configOption.size().getSize(), WIDGET_HEIGHT, Component.literal("Error"), Minecraft.getInstance().font).setColor(0xFFFF0000));
    }

    public static Component getWidgetText(String optionName, String optionValue) {
        return Component.translatable(getOptionNameTranslateKey(optionName), Component.translatable(getOptionValueTranslateKey(optionName, optionValue)));
    }

    public static Component getWidgetText(String optionName) {
        return Component.translatable(getOptionNameTranslateKey(optionName));
    }

    private static String getOptionNameTranslateKey(String optionName) {
        return String.format(OPTION_NAME_TRANSLATE_KEY_FORMAT, optionName);
    }

    private static String getOptionValueTranslateKey(String optionName, String optionValue) {
        return String.format(OPTION_VALUE_TRANSLATE_KEY_FORMAT, optionName, optionValue);
    }

    private record WidgetData(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig) {
    }
}
