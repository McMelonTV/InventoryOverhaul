package ing.boykiss.inventoryoverhaul.gamerule;

import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import ing.boykiss.inventoryoverhaul.mixin.InvokerIntegerValue;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;

public class HotbarSizeGameRules {
    public static final GameRules.Key<GameRules.IntegerValue> HOTBAR_SIZE_X = GameRules.register("hotbarSizeX", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(3, 1, 6, HotbarSizeGameRules::resizeHotbarX));
    public static final GameRules.Key<GameRules.IntegerValue> HOTBAR_SIZE_Y = GameRules.register("hotbarSizeY", GameRules.Category.PLAYER, InvokerIntegerValue.invokeCreate(3, 1, 6, HotbarSizeGameRules::resizeHotbarY));

    public static void init() {
    }

    private static void resizeHotbarX(MinecraftServer server, GameRules.IntegerValue value) {
        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$getHotbar().setSizeX(value.get())
                )
        );
    }

    private static void resizeHotbarY(MinecraftServer server, GameRules.IntegerValue value) {
        server.getAllLevels().forEach(
                serverLevel -> serverLevel.players().forEach(
                        serverPlayer -> ((IMixinInventory) serverPlayer.getInventory()).inventoryoverhaul$getHotbar().setSizeY(value.get())
                )
        );
    }
}
