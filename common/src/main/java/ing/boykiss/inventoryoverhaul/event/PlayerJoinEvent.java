package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.common.PlayerEvent;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.GameRules;

public class PlayerJoinEvent {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            // setting the size again might not be needed as we are already doing that in the Inventory init method, but it's potentially a good failsafe

            GameRules gameRules = serverPlayer.server.getGameRules();

            GameRules.IntegerValue hotbarSizeX = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_X);
            GameRules.IntegerValue hotbarSizeY = gameRules.getRule(HotbarSizeGameRules.HOTBAR_SIZE_Y);

            Inventory inventory = serverPlayer.getInventory();
            IMixinInventory iMixinInventory = (IMixinInventory) inventory;
            iMixinInventory.inventoryoverhaul$trySync();
            iMixinInventory.inventoryoverhaul$getHotbar().setSize(hotbarSizeX.get(), hotbarSizeY.get(), true);
        });
    }
}
