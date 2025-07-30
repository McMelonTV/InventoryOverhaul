package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.common.PlayerEvent;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameRules;

public class PlayerJoinEvent {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            // setting the size again might not be needed as we are already doing that in the Inventory init method, but it's potentially a good failsafe
            MinecraftServer server = serverPlayer.getServer();
            if (server == null) {
                InventoryOverhaul.LOGGER.error("server is null in player join event");
                return;
            }

            GameRules gameRules = serverPlayer.getServer().getGameRules();

            GameRules.IntegerValue hotbarSizeX = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_X);
            GameRules.IntegerValue hotbarSizeY = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_Y);

            if (hotbarSizeX.get() * hotbarSizeY.get() > 36) {
                InventoryOverhaul.LOGGER.error("PLAYERJOIN Hotbar total size is set to more than inventory size, resetting to defaults");
                hotbarSizeY.set(1, server);

                hotbarSizeX.set(9, server);
                hotbarSizeY.set(1, server);
            }

            Inventory inventory = serverPlayer.getInventory();
            IMixinInventory iMixinInventory = (IMixinInventory) inventory;
            iMixinInventory.inventoryoverhaul$trySync();
            iMixinInventory.inventoryoverhaul$getHotbar().setSize(hotbarSizeX.get(), hotbarSizeY.get(), true);
        });
    }
}
