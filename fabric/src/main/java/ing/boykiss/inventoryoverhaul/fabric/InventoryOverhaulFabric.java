package ing.boykiss.inventoryoverhaul.fabric;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import net.fabricmc.api.ModInitializer;

public final class InventoryOverhaulFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        InventoryOverhaul.init();
    }
}
