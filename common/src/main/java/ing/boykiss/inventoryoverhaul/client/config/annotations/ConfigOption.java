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

    enum WidgetSize {
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
