package ing.boykiss.inventoryoverhaul.gamerule;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.mixin.InvokerIntegerValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public class HotbarSizeGameRules {
    public static void init() {
    }

    private static void resizeHotbarX(MinecraftServer server, GameRules.IntegerValue hotbarSizeX) {
        GameRules gameRules = server.getGameRules();

        GameRules.IntegerValue hotbarSizeY = gameRules.getRule(HOTBAR_SIZE_Y);

        if (hotbarSizeX.get() * hotbarSizeY.get() > 36) {
            InventoryOverhaul.LOGGER.error("Tried to set hotbar size to more than inventory size, resetting to defaults");
            hotbarSizeX.set(1, server);

            hotbarSizeY.set(1, server);
            hotbarSizeX.set(9, server);
        }

        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$getHotbar().setSize(hotbarSizeX.get(), hotbarSizeY.get(), true)
                )
        );
    }

    private static void resizeHotbarY(MinecraftServer server, GameRules.IntegerValue hotbarSizeY) {
        GameRules gameRules = server.getGameRules();

        GameRules.IntegerValue hotbarSizeX = gameRules.getRule(HOTBAR_SIZE_X);

        if (hotbarSizeX.get() * hotbarSizeY.get() > 36) {
            InventoryOverhaul.LOGGER.error("Tried to set hotbar size to more than inventory size, resetting to defaults");
            hotbarSizeY.set(1, server);

            hotbarSizeX.set(9, server);
            hotbarSizeY.set(1, server);
        }

        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$getHotbar().setSize(hotbarSizeX.get(), hotbarSizeY.get(), true)
                )
        );
    }

    public static final GameRules.Key<GameRules.IntegerValue> HOTBAR_SIZE_X = GameRules.register("hotbarSizeX", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(9, 1, 9, HotbarSizeGameRules::resizeHotbarX));


    public static final GameRules.Key<GameRules.IntegerValue> HOTBAR_SIZE_Y = GameRules.register("hotbarSizeY", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(1, 1, 9, HotbarSizeGameRules::resizeHotbarY));


}
