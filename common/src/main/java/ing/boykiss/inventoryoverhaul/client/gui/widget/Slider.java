package ing.boykiss.inventoryoverhaul.client.gui.widget;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;
import java.util.function.Function;

// TODO
public class Slider extends AbstractSliderButton {
    private final Function<Double, Component> componentProvider;
    private final Consumer<Double> valueConsumer;
    private final double min;
    private final double max;
    private final double step;

    public Slider(int width, int height, Function<Double, Component> componentProvider, Consumer<Double> valueConsumer, double value, double min, double max, double step) {
        super(0, 0, width, height, componentProvider.apply(norm(value, min, max)), norm(value, min, max));
        this.componentProvider = componentProvider;
        this.valueConsumer = valueConsumer;
        this.min = min;
        this.max = max;
        this.step = step;
    }

    /**
     * [min,max] -> [0,1]
     */
    protected static double norm(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    /**
     * [0,1] -> [min,max]
     */
    protected static double real(double value, double min, double max) {
        return min + value * (max - min);
    }

    /**
     * [min,max] -> [0,1]
     */
    protected double norm(double value) {
        return (value - min) / (max - min);
    }

    /**
     * [0,1] -> [min,max]
     */
    protected double real(double value) {
        return min + value * (max - min);
    }

    /**
     * real number in, real number out
     */
    protected double snap(double value) {
        return clamp(min + Math.round((value - min) / step) * step);
    }

    protected double clamp(double value) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    protected void updateMessage() {
        this.setMessage(componentProvider.apply(value));
    }

    @Override
    protected void applyValue() {
        double realValue = real(value);
        double realValueSnapped = snap(realValue);
        value = norm(realValueSnapped);

        this.valueConsumer.accept(value);
    }
}
