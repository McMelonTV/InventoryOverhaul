package ing.boykiss.inventoryoverhaul.client.gui.widget;

import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import lombok.Getter;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigWidget<T extends AbstractWidget> {
    private static final String OPTION_NAME_TRANSLATE_KEY_FORMAT = "options.%s";
    private static final String OPTION_VALUE_TRANSLATE_KEY_FORMAT = "options.%s.%s";

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

            if (!type.isEnum())
                return new ConfigWidget<>(configOption.size(), Button.builder(Component.literal(""), (b) -> {

                }).width(configOption.size().getSize()).build());

            //enum
            return new ConfigWidget<>(configOption.size(), Button.builder(getButtonText(field.getName(), field.get(clientConfig).toString()), (b) -> {
                Object currentOption;
                try {
                    currentOption = field.get(clientConfig);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                Object[] enumConstants = type.getEnumConstants();
                int currentIndex = Arrays.asList(enumConstants).indexOf(currentOption);
                int nextIndex = (currentIndex + 1) % enumConstants.length;
                Object next = enumConstants[nextIndex];
                try {
                    field.set(clientConfig, next);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                clientConfig.save();

                b.setMessage(getButtonText(field.getName(), next.toString()));
            }).width(configOption.size().getSize()).build());
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static Component getButtonText(String optionName, String optionValue) {
        return Component.translatable(getOptionNameTranslateKey(optionName), Component.translatable(getOptionValueTranslateKey(optionName, optionValue)));
    }

    public static Component getButtonText(String optionName) {
        return Component.translatable(getOptionNameTranslateKey(optionName));
    }

    private static String getOptionNameTranslateKey(String optionName) {
        return String.format(OPTION_NAME_TRANSLATE_KEY_FORMAT, optionName);
    }

    private static String getOptionValueTranslateKey(String optionName, String optionValue) {
        return String.format(OPTION_VALUE_TRANSLATE_KEY_FORMAT, optionName, optionValue);
    }
}
