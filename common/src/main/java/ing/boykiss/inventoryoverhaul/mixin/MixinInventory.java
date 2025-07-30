package ing.boykiss.inventoryoverhaul.mixin;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.EnvExecutor;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import ing.boykiss.inventoryoverhaul.network.S2CInventorySizeUpdatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class MixinInventory implements IMixinInventory {
    @Shadow
    @Final
    public Player player;
    @Unique
    public Hotbar inventoryoverhaul$hotbar;
    @Shadow
    public int selected;
    @Unique
    private int inventoryoverhaul$sizeX;
    @Unique
    private int inventoryoverhaul$sizeY;

    @Inject(method = "isHotbarSlot", at = @At("HEAD"), cancellable = true)
    private static void isHotbarSlot(int i, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(i >= 0 && i < Inventory.getSelectionSize());
    }

    @Inject(method = "getSelectionSize", at = @At("HEAD"), cancellable = true)
    private static void getSelectionSize(CallbackInfoReturnable<Integer> cir) {
        int hotbarSize = EnvExecutor.getEnvSpecific(() -> () -> {
            Player player = Minecraft.getInstance().player;

            // hacky failsafe before player data is fully loaded, should always be higher than the highest possible hotbar total size
            if (player == null) return 999;

            return ((IMixinInventory) player.getInventory()).inventoryoverhaul$getHotbar().getTotalSize();
        }, () -> () -> {
            GameRules gameRules = InventoryOverhaul.server.getGameRules();
            int hotbarSizeX = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_X);
            int hotbarSizeY = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_Y);

            return hotbarSizeX * hotbarSizeY;
        });
        cir.setReturnValue(hotbarSize);
    }

    @Shadow
    public static int getSelectionSize() {
        return 0;
    }

    @Shadow
    public abstract void swapPaint(double d);

    @Inject(method = "swapPaint", at = @At("HEAD"), cancellable = true)
    public void swapPaint(double d, CallbackInfo ci) {
        if (!Inventory.isHotbarSlot((int) d)) {
            swapPaint(getSelectionSize() - 1);
        } else {
            this.selected = (int) d;
        }
        ci.cancel();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Player player, CallbackInfo ci) {
        this.inventoryoverhaul$hotbar = new Hotbar(player);

        if (!(player instanceof ServerPlayer serverPlayer)) return;
        if (serverPlayer.getServer() == null) return;
        GameRules gameRules = serverPlayer.getServer().getGameRules();

//            int inventorySizeX = gameRules.getInt(InventorySizeGameRules.INVENTORY_SIZE_X);
//            int inventorySizeY = gameRules.getInt(InventorySizeGameRules.INVENTORY_SIZE_Y);
        int hotbarSizeX = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_X);
        int hotbarSizeY = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_Y);

        // we need to sync on the PlayerJoin event to prevent trying to send data before a connection is created
//            inventoryoverhaul$setSize(inventorySizeX, inventorySizeY, false);
        inventoryoverhaul$hotbar.setSize(hotbarSizeX, hotbarSizeY, false);

    }

    @Override
    @Unique
    public int inventoryoverhaul$getSizeX() {
        return inventoryoverhaul$sizeX;
    }

    @Override
    @Unique
    public void inventoryoverhaul$setSizeX(int x) {
        inventoryoverhaul$setSize(x, inventoryoverhaul$sizeY, true);
    }

    @Override
    @Unique
    public int inventoryoverhaul$getSizeY() {
        return inventoryoverhaul$sizeY;
    }

    @Override
    @Unique
    public void inventoryoverhaul$setSizeY(int y) {
        inventoryoverhaul$setSize(inventoryoverhaul$sizeX, y, true);
    }

    @Override
    @Unique
    public void inventoryoverhaul$setSize(int x, int y, boolean sync) {
        inventoryoverhaul$sizeX = x;
        inventoryoverhaul$sizeY = y;
        if (sync) inventoryoverhaul$trySync();
    }

    @Override
    @Unique
    public void inventoryoverhaul$trySync() {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, new S2CInventorySizeUpdatePacket(inventoryoverhaul$sizeX, inventoryoverhaul$sizeY));
        }
    }

    @Override
    @Unique
    public Hotbar inventoryoverhaul$getHotbar() {
        return inventoryoverhaul$hotbar;
    }
}

