package ing.boykiss.inventoryoverhaul.client.gui.widget;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class HotbarViewWidget implements Renderable {
    private static final ResourceLocation HOTBAR_SLOT_SPRITE = ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "hotbar_slot");
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "hotbar_selection");

    private final Player player;
    private final Hotbar hotbar;
    private final ClientConfig clientConfig;

    public HotbarViewWidget(@NotNull Player player) {
        this.player = player;
        this.hotbar = ((IMixinInventory) player.getInventory()).inventoryoverhaul$getHotbar();
        this.clientConfig = ClientConfig.getInstance();
    }

    public Vec2 size(boolean includeSelectionOutline) {
        int outlineTotal = 2 + (includeSelectionOutline ? 2 : 0); // both sides

        int slotsX = hotbar.getSizeX();
        int sizeSlotsX = slotsX * 20;
        int sizeX = sizeSlotsX + outlineTotal;

        int slotsY = hotbar.getSizeY();
        int sizeSlotsY = slotsY * 20;
        int sizeY = sizeSlotsY + outlineTotal;

        return new Vec2(sizeX, sizeY);
    }

    public Vec2 size() {
        return size(false);
    }


    /**
     * NOTE: the hotbar selection outline will go one pixel outside the current pose translation
     */
    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0);

        renderSlots(guiGraphics, partialTick);
        renderOutline(guiGraphics);
        renderSlotSelection(guiGraphics);
        renderSlotSelectionOutline(guiGraphics);

        guiGraphics.pose().popPose();
    }


    public void render(GuiGraphics guiGraphics, float partialTick) {
        guiGraphics.pose().pushPose();

        Vec2 hotbarPosition = calculatePosition(guiGraphics, clientConfig);
        guiGraphics.pose().translate(hotbarPosition.x, hotbarPosition.y, -90.0F);

        render(guiGraphics, 0, 0, partialTick);

        guiGraphics.pose().popPose();
    }

    private void renderSlots(GuiGraphics guiGraphics, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(1, 1, 0);

        for (int slotY = 0; slotY < hotbar.getSizeY(); slotY++) {
            for (int slotX = 0; slotX < hotbar.getSizeX(); slotX++) {
                int itemIndex = hotbar.getSlotIndex(slotX, slotY);

                int posX = slotX * 20;
                int posY = slotY * 20;

                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(posX, posY, 0);

                guiGraphics.blitSprite(RenderType::guiTextured, HOTBAR_SLOT_SPRITE, 0, 0, 20, 20);

                renderItemSlot(guiGraphics, 2, 2, partialTick, player.getInventory().getItem(itemIndex), itemIndex + 1);

                guiGraphics.pose().popPose();
            }
        }

        guiGraphics.pose().popPose();
    }

    private void renderOutline(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 0);

        int hotbarX = hotbar.getSizeX();
        int hotbarY = hotbar.getSizeY();

        // top, bottom, left, right
        guiGraphics.fill(0, 0, 1 + hotbarX * 20, 1, 0xFF000000);
        guiGraphics.fill(0, hotbarY * 20, 1 + hotbarX * 20, 1 + hotbarY * 20, 0xFF000000);
        guiGraphics.fill(0, 0, 1, 1 + hotbarY * 20, 0xFF000000);
        guiGraphics.fill(hotbarX * 20, 0, 1 + hotbarX * 20, 1 + hotbarY * 20, 0xFF000000);

        guiGraphics.pose().popPose();
    }

    private void renderSlotSelection(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 0);

        int selectedSlot = player.getInventory().getSelectedSlot();
        int selectedX = selectedSlot % hotbar.getSizeX();
        int selectedY = selectedSlot / hotbar.getSizeX();

        int selectedPosX = selectedX * 20;
        int selectedPosY = selectedY * 20;

        guiGraphics.blitSprite(
                RenderType::guiTextured, HOTBAR_SELECTION_SPRITE, selectedPosX, selectedPosY, 22, 22
        );

        guiGraphics.pose().popPose();
    }

    private void renderSlotSelectionOutline(GuiGraphics guiGraphics) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(-1, -1, 0);

        int selectedSlot = player.getInventory().getSelectedSlot();
        int selectedX = selectedSlot % hotbar.getSizeX();
        int selectedY = selectedSlot / hotbar.getSizeX();

        int selectedPosX = selectedX * 20;
        int selectedPosY = selectedY * 20;
        int endSelectedPosX = selectedPosX + 1;
        int endSelectedPosY = selectedPosY + 1;

        boolean firstX = selectedX == 0;
        boolean lastX = selectedX == hotbar.getSizeX() - 1;
        boolean firstY = selectedY == 0;
        boolean lastY = selectedY == hotbar.getSizeY() - 1;

        // top, bottom, left, right
        if (firstY) {
            guiGraphics.fill(selectedPosX, selectedPosY, endSelectedPosX + (lastX ? 22 : 23), endSelectedPosY, 0xFF000000);
        }
        if (lastY) {
            guiGraphics.fill(selectedPosX, selectedPosY + 22, endSelectedPosX + (lastX ? 22 : 23), endSelectedPosY + 22, 0xFF000000);
        }
        if (firstX) {
            guiGraphics.fill(selectedPosX, selectedPosY, endSelectedPosX, endSelectedPosY + (lastY ? 22 : 23), 0xFF000000);
        }
        if (lastX) {
            guiGraphics.fill(selectedPosX + 22, selectedPosY, endSelectedPosX + 22, endSelectedPosY + (lastY ? 22 : 23), 0xFF000000);
        }

        guiGraphics.pose().popPose();
    }

    // copied over from Gui class
    private void renderItemSlot(GuiGraphics guiGraphics, int x, int y, float partialTick, ItemStack itemStack, int seed) {
        if (!itemStack.isEmpty()) {
            float f = itemStack.getPopTime() - partialTick;
            if (f > 0.0F) {
                float g = 1.0F + f / 5.0F;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate((float) (x + 8), (float) (y + 12), 0.0F);
                guiGraphics.pose().scale(1.0F / g, (g + 1.0F) / 2.0F, 1.0F);
                guiGraphics.pose().translate((float) (-(x + 8)), (float) (-(y + 12)), 0.0F);
            }

            guiGraphics.renderItem(player, itemStack, x, y, seed);
            if (f > 0.0F) {
                guiGraphics.pose().popPose();
            }

            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, x, y);
        }
    }

    public Vec2 calculatePosition(GuiGraphics guiGraphics, ClientConfig clientConfig) {
        Vec2 hotbarSize = size();

        ClientConfig.HotbarAnchorX hotbarAnchorX = clientConfig.getHotbarAnchorX();
        int hotbarOffsetX = clientConfig.getHotbarOffsetX();
        int hotbarPaddingX = clientConfig.getHotbarPaddingX();

        ClientConfig.HotbarAnchorY hotbarAnchorY = clientConfig.getHotbarAnchorY();
        int hotbarOffsetY = clientConfig.getHotbarOffsetY();
        int hotbarPaddingY = clientConfig.getHotbarPaddingY();

        int guiWidth = guiGraphics.guiWidth();
        int guiHeight = guiGraphics.guiHeight();

        int safeWidth = guiWidth - (hotbarPaddingX * 2);
        int safeHeight = guiHeight - (hotbarPaddingY * 2);

        int xChunk = safeWidth / 4;
        int yChunk = safeHeight / 4;

        Map<ClientConfig.HotbarAnchorX, Integer> anchorXMap = Map.of(
                ClientConfig.HotbarAnchorX.LEFT, 0,
                ClientConfig.HotbarAnchorX.LEFT_CENTER, xChunk,
                ClientConfig.HotbarAnchorX.CENTER, xChunk * 2,
                ClientConfig.HotbarAnchorX.RIGHT_CENTER, xChunk * 3,
                ClientConfig.HotbarAnchorX.RIGHT, xChunk * 4
        );

        Map<ClientConfig.HotbarAnchorX, Integer> anchorXHotbarSizeOffsetMap = Map.of(
                ClientConfig.HotbarAnchorX.LEFT, 0,
                ClientConfig.HotbarAnchorX.LEFT_CENTER, (int) -(hotbarSize.x / 2),
                ClientConfig.HotbarAnchorX.CENTER, (int) -(hotbarSize.x / 2),
                ClientConfig.HotbarAnchorX.RIGHT_CENTER, (int) -(hotbarSize.x / 2),
                ClientConfig.HotbarAnchorX.RIGHT, (int) -hotbarSize.x
        );

        Map<ClientConfig.HotbarAnchorY, Integer> anchorYMap = Map.of(
                ClientConfig.HotbarAnchorY.TOP, 0,
                ClientConfig.HotbarAnchorY.TOP_CENTER, yChunk,
                ClientConfig.HotbarAnchorY.CENTER, yChunk * 2,
                ClientConfig.HotbarAnchorY.BOTTOM_CENTER, yChunk * 3,
                ClientConfig.HotbarAnchorY.BOTTOM, yChunk * 4
        );

        Map<ClientConfig.HotbarAnchorY, Integer> anchorYHotbarSizeOffsetMap = Map.of(
                ClientConfig.HotbarAnchorY.TOP, 0,
                ClientConfig.HotbarAnchorY.TOP_CENTER, (int) -(hotbarSize.y / 2),
                ClientConfig.HotbarAnchorY.CENTER, (int) -(hotbarSize.y / 2),
                ClientConfig.HotbarAnchorY.BOTTOM_CENTER, (int) -(hotbarSize.y / 2),
                ClientConfig.HotbarAnchorY.BOTTOM, (int) -hotbarSize.y
        );

        int anchorX = anchorXMap.get(hotbarAnchorX) + anchorXHotbarSizeOffsetMap.get(hotbarAnchorX);
        int anchorY = anchorYMap.get(hotbarAnchorY) + anchorYHotbarSizeOffsetMap.get(hotbarAnchorY);

        int posX = hotbarPaddingX + anchorX + hotbarOffsetX;
        int posY = hotbarPaddingY + anchorY + hotbarOffsetY;

        return new Vec2(posX, posY);
    }
}
