package ing.boykiss.inventoryoverhaul.client.gui.widget;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

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

    private static @Nullable ConfigWidget<?> create(Field field, ClientConfig clientConfig) {
        try {
            Class<?> type = field.getType();
            ConfigOption configOption = field.getAnnotation(ConfigOption.class);

            boolean slider = configOption.slider();
            double sliderStep = configOption.sliderStep();
            double min = configOption.min();
            double max = configOption.max();

            if (type.isEnum()) {
                return slider
                        ? createEnumSlider(field, type, configOption, clientConfig, sliderStep)
                        : createEnumButton(field, type, configOption, clientConfig);
            }

            if (type == short.class || type == Short.class ||
                    type == int.class || type == Integer.class ||
                    type == long.class || type == Long.class ||

                    type == float.class || type == Float.class ||
                    type == double.class || type == Double.class) {
                return (slider && !Double.isNaN(min) && !Double.isNaN(max))
                        ? createNumericSlider(field, type, configOption, clientConfig, min, max, sliderStep)
                        : createNumericInput(field, type, configOption, clientConfig, min, max);
            }

            if (type == String.class) {
                return createStringInput(field, type, configOption, clientConfig);
            }

            InventoryOverhaul.LOGGER.error("config widget for type {} not implemented", type.getName());
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static ConfigWidget<Button> createEnumButton(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig) throws IllegalAccessException {
        return new ConfigWidget<>(configOption.size(), Button.builder(getWidgetText(field.getName(), field.get(clientConfig).toString()), (b) -> {
            try {
                Object currentOption = field.get(clientConfig);

                Object[] enumConstants = type.getEnumConstants();
                int currentIndex = Arrays.asList(enumConstants).indexOf(currentOption);
                int nextIndex = (currentIndex + 1) % enumConstants.length;
                Object next = enumConstants[nextIndex];

                field.set(clientConfig, next);

                clientConfig.save();

                b.setMessage(getWidgetText(field.getName(), next.toString()));
            } catch (IllegalAccessException e) {
                InventoryOverhaul.LOGGER.error(e.getMessage());
            }
        }).width(configOption.size().getSize()).build());
    }

    private static ConfigWidget<Button> createEnumSlider(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig, double step) throws IllegalAccessException {
        return new ConfigWidget<>(configOption.size(), Button.builder(getWidgetText(field.getName(), field.get(clientConfig).toString()), (b) -> {

        }).width(configOption.size().getSize()).build());
    }

    private static ConfigWidget<EditBox> createNumericInput(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig, double min, double max) throws IllegalAccessException {
        return new ConfigWidget<>(configOption.size(), new EditBox(Minecraft.getInstance().font, configOption.size().getSize(), WIDGET_HEIGHT, getWidgetText(field.getName())));
    }

    private static ConfigWidget<Button> createNumericSlider(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig, double min, double max, double step) throws IllegalAccessException {
        return new ConfigWidget<>(configOption.size(), Button.builder(getWidgetText(field.getName(), field.get(clientConfig).toString()), (b) -> {

        }).width(configOption.size().getSize()).build());
    }

    private static ConfigWidget<EditBox> createStringInput(Field field, Class<?> type, ConfigOption configOption, ClientConfig clientConfig) throws IllegalAccessException {
        return new ConfigWidget<>(configOption.size(), new EditBox(Minecraft.getInstance().font, configOption.size().getSize(), WIDGET_HEIGHT, getWidgetText(field.getName())));
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
}
