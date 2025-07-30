package ing.boykiss.inventoryoverhaul.mixin.client;

import ing.boykiss.inventoryoverhaul.client.gui.widget.HotbarViewWidget;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {
    @Shadow
    @Final
    private static ResourceLocation HOTBAR_OFFHAND_LEFT_SPRITE;
    @Shadow
    @Final
    private static ResourceLocation HOTBAR_OFFHAND_RIGHT_SPRITE;
    @Shadow
    @Final
    private static ResourceLocation HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE;
    @Shadow
    @Final
    private static ResourceLocation HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE;
    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    private HotbarViewWidget inventoryoverhaul$hotbarViewWidget;

    @Shadow
    @Nullable
    protected abstract Player getCameraPlayer();

    @Shadow
    protected abstract void renderSlot(GuiGraphics guiGraphics, int i, int j, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int k);

    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    private void renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        Player player = getCameraPlayer();
        if (player == null) return;

        if (inventoryoverhaul$hotbarViewWidget == null)
            inventoryoverhaul$hotbarViewWidget = new HotbarViewWidget();

        // start vanilla
        ItemStack itemStack = player.getOffhandItem();
        HumanoidArm humanoidArm = player.getMainArm().getOpposite();
        int a = guiGraphics.guiWidth() / 2;
        if (!itemStack.isEmpty()) {
            if (humanoidArm == HumanoidArm.LEFT) {
                guiGraphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE, a - 91 - 29, guiGraphics.guiHeight() - 23, 29, 24);
            } else {
                guiGraphics.blitSprite(HOTBAR_OFFHAND_RIGHT_SPRITE, a + 91, guiGraphics.guiHeight() - 23, 29, 24);
            }
        }
        // end vanilla

        inventoryoverhaul$hotbarViewWidget.render(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false), player);

        // start vanilla
        if (!itemStack.isEmpty()) {
            int d = guiGraphics.guiHeight() - 16 - 3;
            if (humanoidArm == HumanoidArm.LEFT) {
                this.renderSlot(guiGraphics, a - 91 - 26, d, deltaTracker, player, itemStack, 1);
            } else {
                this.renderSlot(guiGraphics, a + 91 + 10, d, deltaTracker, player, itemStack, 1);
            }
        }

        if (minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
            float f = player.getAttackStrengthScale(0.0F);
            if (f < 1.0F) {
                int n = guiGraphics.guiHeight() - 20;
                int o = a + 91 + 6;
                if (humanoidArm == HumanoidArm.RIGHT) {
                    o = a - 91 - 22;
                }

                int p = (int) (f * 19.0F);
                guiGraphics.blitSprite(HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, o, n, 18, 18);
                guiGraphics.blitSprite(HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
            }
        }
        // end vanilla

        ci.cancel();
    }

    @Inject(method = "renderSelectedItemName", at = @At("HEAD"))
    public void renderSelectedItemName(GuiGraphics guiGraphics, CallbackInfo ci) {
    }
}
