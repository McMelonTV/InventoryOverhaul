package ing.boykiss.inventoryoverhaul.neoforge;

import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import net.neoforged.fml.common.Mod;

@Mod(InventoryOverhaul.MOD_ID)
public final class InventoryOverhaulNeoForge {
    public InventoryOverhaulNeoForge() {
        // Run our common setup.
        InventoryOverhaul.init();
    }
}
