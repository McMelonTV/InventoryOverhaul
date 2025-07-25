package ing.boykiss.inventoryoverhaul.mixin.client;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Unique
    private static final ResourceLocation HOTBAR_SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "hotbar_slot");
    @Unique
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "hotbar_selection");

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k);

    @Inject(method = "renderItemHotbar", at = @At("HEAD"))
    private void renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Player player = getCameraPlayer();
        if (player == null) return;

        IMixinInventory iMixinInventory = (IMixinInventory) player.getInventory();
        Hotbar hotbar = iMixinInventory.inventoryoverhaul$getHotbar();

        int m = 0;
        int l = 1;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, -90.0F);

        for (int y = 0; y < hotbar.getSizeY(); y++) {
            for (int x = 0; x < hotbar.getSizeX(); x++) {
                int posX = 10 + x * 20;
                int posY = 10 + y * 20;

                guiGraphics.blitSprite(RenderType::guiTextured, HOTBAR_SLOT_SPRITE, posX, posY, 20, 20);

                renderSlot(guiGraphics, posX + 2, posY + 2, deltaTracker, player, player.getInventory().getItem(m++), l++);
            }
        }

        int hotbarX = hotbar.getSizeX();
        int hotbarY = hotbar.getSizeY();

        // top and bottom
        guiGraphics.fill(9, 9, 10 + hotbarX * 20, 10, 0xFF000000);
        guiGraphics.fill(9, 9 + hotbarY * 20, 10 + hotbarX * 20, 10 + hotbarY * 20, 0xFF000000);
        // left and right
        guiGraphics.fill(9, 9, 10, 10 + hotbarY * 20, 0xFF000000);
        guiGraphics.fill(9 + hotbarX * 20, 9, 10 + hotbarX * 20, 10 + hotbarY * 20, 0xFF000000);

        int selectedSlot = player.getInventory().getSelectedSlot();
        int selectedX = selectedSlot % hotbar.getSizeX();
        int selectedY = selectedSlot / hotbar.getSizeX();

        int selectedPosX = 10 + selectedX * 20 - 1;
        int selectedPosY = 10 + selectedY * 20 - 1;

        guiGraphics.blitSprite(
                RenderType::guiTextured, HOTBAR_SELECTION_SPRITE, selectedPosX, selectedPosY, 22, 22
        );

        boolean firstX = selectedX == 0;
        boolean lastX = selectedX == hotbar.getSizeX() - 1;
        boolean firstY = selectedY == 0;
        boolean lastY = selectedY == hotbar.getSizeY() - 1;

        if (firstX) {
            // left
            guiGraphics.fill(selectedPosX - 1, selectedPosY - 1, selectedPosX, selectedPosY + (lastY ? 22 : 23), 0xFF000000);
        }
        if (lastX) {
            // right
            guiGraphics.fill(selectedPosX - 1 + 22, selectedPosY - 1, selectedPosX + 22, selectedPosY + (lastY ? 22 : 23), 0xFF000000);
        }

        if (firstY) {
            // top
            guiGraphics.fill(selectedPosX - 1, selectedPosY - 1, selectedPosX + (lastX ? 22 : 23), selectedPosY, 0xFF000000);
        }
        if (lastY) {
            // bottom
            guiGraphics.fill(selectedPosX - 1, selectedPosY - 1 + 22, selectedPosX + (lastX ? 22 : 23), selectedPosY + 22, 0xFF000000);
        }

        guiGraphics.pose().popPose();
    }
}
