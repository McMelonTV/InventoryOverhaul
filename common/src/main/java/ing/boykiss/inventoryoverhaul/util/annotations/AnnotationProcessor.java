package ing.boykiss.inventoryoverhaul.util.annotations;

import java.lang.reflect.Field;

public class AnnotationProcessor {
    public static void validateRequireFieldAnnotations(Class<?> clazz) {
        if (clazz.isAnnotationPresent(RequireFieldAnnotation.class)) {
            RequireFieldAnnotation annotation = clazz.getAnnotation(RequireFieldAnnotation.class);
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(annotation.value())) {
                    throw new RuntimeException(
                            "Field '" + field.getName() +
                                    "' in class '" + clazz.getName() +
                                    "' is missing @" + annotation.value().getName()
                    );
                }
            }
        }
    }
}
