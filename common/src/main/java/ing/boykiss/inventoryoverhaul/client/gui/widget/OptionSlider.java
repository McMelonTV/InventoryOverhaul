package ing.boykiss.inventoryoverhaul.client.gui.widget;

import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class OptionSlider<T> extends Slider {
    public OptionSlider(int width, int height, Function<T, Component> componentProvider, Consumer<T> valueConsumer, T value, List<T> options) {
        super(width, height, (v) -> componentProvider.apply(objectValue(v, options)), (v) -> valueConsumer.accept(objectValue(v, options)), options.indexOf(value), 0, options.isEmpty() ? 0 : options.size() - 1, 1);
    }

    private static <T> T objectValue(double value, List<T> options) {
        return options.get((int) value);
    }
}
