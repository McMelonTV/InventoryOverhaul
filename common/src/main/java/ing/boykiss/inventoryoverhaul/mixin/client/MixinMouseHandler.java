package ing.boykiss.inventoryoverhaul.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.keybind.ModifierKeybind;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void onScroll(long l, double d, double e, CallbackInfo ci, @Local int k, @Local Inventory inventory) {
        boolean modifierKeyHeld = ModifierKeybind.MODIFIER_KEYMAPPING.isDown();

        ClientConfig clientConfig = ClientConfig.getInstance();
        ClientConfig.HotbarScrollMode hotbarScrollMode = clientConfig.getHotbarScrollMode();
        ClientConfig.HotbarScrollDirection hotbarScrollDirection = clientConfig.getHotbarScrollDirection();

        Hotbar hotbar = ((IMixinInventory) inventory).inventoryoverhaul$getHotbar();

        if (hotbarScrollMode == ClientConfig.HotbarScrollMode.CONTINUOUS) {
            if (hotbarScrollDirection == ClientConfig.HotbarScrollDirection.HORIZONTAL) {
                int next = ScrollWheelHandler.getNextScrollWheelSelection(k, inventory.getSelectedSlot(), Inventory.getSelectionSize());
                inventory.setSelectedSlot(next);
            } else if (hotbarScrollDirection == ClientConfig.HotbarScrollDirection.VERTICAL) {
                int next = ScrollWheelHandler.getNextScrollWheelSelection(k, hotbar.getSlotIndexColumn(inventory.getSelectedSlot()), Inventory.getSelectionSize());
                int slotIndex = hotbar.getSlotIndexColumn(next);
                inventory.setSelectedSlot(slotIndex);
            }
        } else if (hotbarScrollMode == ClientConfig.HotbarScrollMode.SPLIT) {
            boolean horizontal = hotbarScrollDirection == ClientConfig.HotbarScrollDirection.HORIZONTAL;
            Tuple<Integer, Integer> currentXY = hotbar.getSlotXY(inventory.getSelectedSlot());
            if ((horizontal && modifierKeyHeld) || (!horizontal && !modifierKeyHeld)) {
                // column
                int nextY = ScrollWheelHandler.getNextScrollWheelSelection(k, currentXY.getB(), hotbar.getSizeY());
                inventory.setSelectedSlot(hotbar.getSlotIndex(currentXY.getA(), nextY));
            } else {
                // row
                int nextX = ScrollWheelHandler.getNextScrollWheelSelection(k, currentXY.getA(), hotbar.getSizeX());
                inventory.setSelectedSlot(hotbar.getSlotIndex(nextX, currentXY.getB()));
            }
        }
    }

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;setSelectedSlot(I)V"))
    private void onScroll(Inventory instance, int i) {
    }
}
