package ing.boykiss.inventoryoverhaul.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.keybind.ModifierKeybind;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Shadow
    private double accumulatedScrollX;

    @Shadow
    private Minecraft minecraft;

    // start vanilla
    private static int getNextScrollWheelSelection(double j, int k, int l) {
        int i = (int)Math.signum(j);
        k -= i;
        k = Math.max(-1, k);

        while (k < 0) {
            k += l;
        }

        while (k >= l) {
            k -= l;
        }

        return k;
    }
    // end vanilla

    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
    private void onScroll(long l, double d, double e, CallbackInfo ci) {
        int k = (int) this.accumulatedScrollX;
        Inventory inventory = this.minecraft.player.getInventory();
        boolean modifierKeyHeld = ModifierKeybind.MODIFIER_KEYMAPPING.isDown();

        ClientConfig clientConfig = ClientConfig.getInstance();
        ClientConfig.HotbarScrollMode hotbarScrollMode = clientConfig.getHotbarScrollMode();
        ClientConfig.HotbarScrollDirection hotbarScrollDirection = clientConfig.getHotbarScrollDirection();

        Hotbar hotbar = ((IMixinInventory) inventory).inventoryoverhaul$getHotbar();

        boolean byRow = hotbarScrollDirection == ClientConfig.HotbarScrollDirection.ROW;
        if (hotbarScrollMode == ClientConfig.HotbarScrollMode.CONTINUOUS) {
            int base = byRow
                    ? inventory.selected
                    : hotbar.getSlotIndexColumn(inventory.selected);
            int selectionSize = Inventory.getSelectionSize();
            int next = getNextScrollWheelSelection(k, base, selectionSize);
            int slotIndex = byRow
                    ? next
                    : hotbar.getSlotIndexColumn(next);
            inventory.swapPaint(slotIndex);
        } else if (hotbarScrollMode == ClientConfig.HotbarScrollMode.SPLIT) {
            Tuple<Integer, Integer> currentXY = hotbar.getSlotXY(inventory.selected);
            boolean scrollByColumn = (byRow && modifierKeyHeld) || (!byRow && !modifierKeyHeld);
            int nextX = currentXY.getA();
            int nextY = currentXY.getB();

            if (scrollByColumn) {
                nextY = getNextScrollWheelSelection(k, currentXY.getB(), hotbar.getSizeY());
            } else {
                nextX = getNextScrollWheelSelection(k, currentXY.getA(), hotbar.getSizeX());
            }

            inventory.swapPaint(hotbar.getSlotIndex(nextX, nextY));
        }
    }

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
    private void onScroll(Inventory instance, double i) {
    }
}
