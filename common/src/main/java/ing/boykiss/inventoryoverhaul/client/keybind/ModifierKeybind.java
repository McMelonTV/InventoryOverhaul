package ing.boykiss.inventoryoverhaul.client.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModifierKeybind {
    public static final KeyMapping MODIFIER_KEYMAPPING = new KeyMapping("key.inventoryOverhaulModifier", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.categories.inventory");

    public static void init() {
        KeyMappingRegistry.register(MODIFIER_KEYMAPPING);
    }
}
