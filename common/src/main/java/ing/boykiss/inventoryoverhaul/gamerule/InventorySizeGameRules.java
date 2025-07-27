package ing.boykiss.inventoryoverhaul.gamerule;

import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.mixin.InvokerIntegerValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;

public class InventorySizeGameRules {
    public static final GameRules.Key<GameRules.IntegerValue> INVENTORY_SIZE_X = GameRules.register("inventorySizeX", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(9, 1, 12, FeatureFlagSet.of(), InventorySizeGameRules::resizeInventoryX));
    public static final GameRules.Key<GameRules.IntegerValue> INVENTORY_SIZE_Y = GameRules.register("inventorySizeY", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(3, 1, 12, FeatureFlagSet.of(), InventorySizeGameRules::resizeInventoryY));

    public static void init() {
    }

    private static void resizeInventoryX(MinecraftServer server, GameRules.IntegerValue value) {
        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$setSizeX(value.get())
                )
        );
    }

    private static void resizeInventoryY(MinecraftServer server, GameRules.IntegerValue value) {
        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$setSizeY(value.get())
                )
        );
    }
}
