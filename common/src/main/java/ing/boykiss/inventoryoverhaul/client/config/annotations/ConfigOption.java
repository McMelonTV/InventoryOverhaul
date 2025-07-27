package ing.boykiss.inventoryoverhaul.client.config.annotations;

import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigOption {
    ButtonSize size() default ButtonSize.MEDIUM;

    int gridX() default -1;

    int gridY() default -1;

    enum ButtonSize {
        SMALL(100),
        MEDIUM(150),
        BIG(200),
        FULL(300);

        @Getter
        private final int size;

        ButtonSize(int size) {
            this.size = size;
        }
    }
}
