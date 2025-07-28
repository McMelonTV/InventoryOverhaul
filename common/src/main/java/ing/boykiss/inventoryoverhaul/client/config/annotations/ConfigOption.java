package ing.boykiss.inventoryoverhaul.client.config.annotations;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigOption {
    WidgetSize size() default WidgetSize.DEFAULT;

    /**
     * only applicable to enums, and numerics with defined min and max
     */
    boolean slider() default false;

    /**
     * only applicable to numerics
     */
    double sliderStep() default -1;

    /**
     * only applicable to numerics
     */
    double min() default -1;

    /**
     * only applicable to numerics
     */
    double max() default -1;

    enum WidgetSize {
        /**
         * this should only be used for checkboxes
         */
        SQUARE(20),
        TINY(50),
        SMALL(75),
        MEDIUM(100),
        DEFAULT(150),
        BIG(200),
        FULL(300);

        @Getter
        private final int size;

        WidgetSize(int size) {
            this.size = size;
        }
    }
}
