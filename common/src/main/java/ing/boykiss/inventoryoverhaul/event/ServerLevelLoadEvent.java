package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.common.LifecycleEvent;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;

public class ServerLevelLoadEvent {
    public static void init() {
        LifecycleEvent.SERVER_LEVEL_LOAD.register(serverLevel -> {
            MinecraftServer server = serverLevel.getServer();
            GameRules gameRules = serverLevel.getGameRules();

            GameRules.IntegerValue hotbarSizeX = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_X);
            GameRules.IntegerValue hotbarSizeY = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_Y);

            if (hotbarSizeX.get() * hotbarSizeY.get() > 36) {
                InventoryOverhaul.LOGGER.error("Hotbar total size is set to more than inventory size, resetting to defaults");
                hotbarSizeY.set(1, server);

                hotbarSizeX.set(9, server);
                hotbarSizeY.set(1, server);
            }
        });
    }
}
