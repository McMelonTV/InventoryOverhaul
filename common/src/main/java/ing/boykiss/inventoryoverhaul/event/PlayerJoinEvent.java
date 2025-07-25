package ing.boykiss.inventoryoverhaul.event;

import dev.architectury.event.events.common.PlayerEvent;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import net.minecraft.world.entity.player.Inventory;

public class PlayerJoinEvent {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(serverPlayer -> {
            Inventory inventory = serverPlayer.getInventory();
            IMixinInventory iMixinInventory = (IMixinInventory) inventory;
            iMixinInventory.inventoryoverhaul$trySync();
            iMixinInventory.inventoryoverhaul$getHotbar().trySync();
        });
    }
}
