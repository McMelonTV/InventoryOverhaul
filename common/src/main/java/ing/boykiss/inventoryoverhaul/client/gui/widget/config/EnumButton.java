package ing.boykiss.inventoryoverhaul.client.gui.widget.config;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.client.config.annotations.ConfigOption;
import net.minecraft.client.gui.components.Button;

import java.util.Arrays;

public class EnumButton implements ConfigButton {
    private final ConfigOption.WidgetSize size;
    private final Button widget;

    public EnumButton(WidgetData widgetData) throws IllegalAccessException {
        size = widgetData.configOption().size();
        widget = Button.builder(ConfigWidget.getWidgetText(widgetData.field().getName(), widgetData.field().get(widgetData.clientConfig()).toString()), (b) -> {
            try {
                Object currentOption = widgetData.field().get(widgetData.clientConfig());

                Object[] enumConstants = widgetData.type().getEnumConstants();
                int currentIndex = Arrays.asList(enumConstants).indexOf(currentOption);
                int nextIndex = (currentIndex + 1) % enumConstants.length;
                Object next = enumConstants[nextIndex];

                widgetData.field().set(widgetData.clientConfig(), next);

                widgetData.clientConfig().save();

                b.setMessage(ConfigWidget.getWidgetText(widgetData.field().getName(), next.toString()));
            } catch (IllegalAccessException e) {
                InventoryOverhaul.LOGGER.error(e.getMessage());
            }
        }).width(widgetData.configOption().size().getSize()).build();
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
