package ing.boykiss.inventoryoverhaul.mixin;

import dev.architectury.networking.NetworkManager;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.gamerule.InventorySizeGameRules;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.inventory.Hotbar;
import ing.boykiss.inventoryoverhaul.network.S2CInventorySizeUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityEquipment;
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

import java.io.IOException;

@Mixin(Inventory.class)
public class MixinInventory implements IMixinInventory {
    @Shadow
    @Final
    public Player player;
    @Unique
    public Hotbar inventoryoverhaul$hotbar;
    @Unique
    private int inventoryoverhaul$sizeX;
    @Unique
    private int inventoryoverhaul$sizeY;

    @Inject(method = "isHotbarSlot", at = @At("HEAD"), cancellable = true)
    private static void isHotbarSlot(int i, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(i >= 0 && i < 16);
//        try {
//            throw new RuntimeException();
//        } catch (RuntimeException e) {
//            InventoryOverhaul.LOGGER.error("isHotbarSlot called: ");
//            e.printStackTrace();
//        }
    }

    @Inject(method = "getSelectionSize", at = @At("HEAD"), cancellable = true)
    private static void getSelectionSize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(16);
//        try {
//            throw new RuntimeException();
//        } catch (RuntimeException e) {
//            InventoryOverhaul.LOGGER.error("getSelectionSize called: ");
//            e.printStackTrace();
//        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(Player player, EntityEquipment entityEquipment, CallbackInfo ci) {
        this.inventoryoverhaul$hotbar = new Hotbar(player); // TODO: load actual hotbar data

        if (player instanceof ServerPlayer serverPlayer) {
            try (ServerLevel serverLevel = serverPlayer.serverLevel()) {
                GameRules gameRules = serverLevel.getGameRules();

                int inventorySizeX = gameRules.getInt(InventorySizeGameRules.INVENTORY_SIZE_X);
                int inventorySizeY = gameRules.getInt(InventorySizeGameRules.INVENTORY_SIZE_Y);
                int hotbarSizeX = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_X);
                int hotbarSizeY = gameRules.getInt(HotbarSizeGameRules.HOTBAR_SIZE_Y);

                // we need to sync on the PlayerJoin event to prevent trying to send data before a connection is created
                inventoryoverhaul$setSize(inventorySizeX, inventorySizeY, false);
                inventoryoverhaul$hotbar.setSize(hotbarSizeX, hotbarSizeY, false);
            } catch (IOException e) {
                InventoryOverhaul.LOGGER.error(e.getMessage());
            }
        }
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

