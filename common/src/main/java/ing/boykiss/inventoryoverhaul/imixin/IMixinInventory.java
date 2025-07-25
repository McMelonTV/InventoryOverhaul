package ing.boykiss.inventoryoverhaul.imixin;

import ing.boykiss.inventoryoverhaul.inventory.Hotbar;

public interface IMixinInventory {
    Hotbar inventoryoverhaul$getHotbar();

    int inventoryoverhaul$getSizeX();

    void inventoryoverhaul$setSizeX(int x);

    int inventoryoverhaul$getSizeY();

    void inventoryoverhaul$setSizeY(int y);

    void inventoryoverhaul$setSize(int x, int y, boolean sync);

    void inventoryoverhaul$trySync();
}
