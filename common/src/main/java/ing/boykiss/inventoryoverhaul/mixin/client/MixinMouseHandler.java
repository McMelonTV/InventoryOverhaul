package ing.boykiss.inventoryoverhaul.mixin.client;

import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.keybind.ModifierKeybind;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MixinMouseHandler {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Unique
    private static int inventoryoverhaul$getNextScrollWheelSelection(double j, int k, int l) {
        int i = (int) Math.signum(j);
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

    @Inject(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
    private void onScroll(long l, double d, double e, CallbackInfo ci) {
        Inventory inventory = minecraft.player.getInventory();
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
            int next = inventoryoverhaul$getNextScrollWheelSelection(e, base, selectionSize);
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
                nextY = inventoryoverhaul$getNextScrollWheelSelection(e, currentXY.getB(), hotbar.getSizeY());
            } else {
                nextX = inventoryoverhaul$getNextScrollWheelSelection(e, currentXY.getA(), hotbar.getSizeX());
            }

            inventory.swapPaint(hotbar.getSlotIndex(nextX, nextY));
        }
    }

    @Redirect(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;swapPaint(D)V"))
    private void onScroll(Inventory instance, double d) {
    }
}
