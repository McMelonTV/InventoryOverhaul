package ing.boykiss.inventoryoverhaul.inventory;

import dev.architectury.networking.NetworkManager;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.network.S2CHotbarSizeUpdatePacket;
import lombok.Getter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;

public class Hotbar {
    private final Player player;
    @Getter
    private int sizeX;
    @Getter
    private int sizeY;

    public Hotbar(Player player) {
        this.player = player;
    }

    public int getTotalSize() {
        return sizeX * sizeY;
    }

    public void setSizeX(int x) {
        setSize(x, sizeY, true);
    }

    public void setSizeY(int y) {
        setSize(sizeX, y, true);
    }

    public void setSize(int x, int y, boolean sync) {
        sizeX = x;
        sizeY = y;
        if (sync) trySync();
    }

    public void trySync() {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkManager.sendToPlayer(serverPlayer, new S2CHotbarSizeUpdatePacket(sizeX, sizeY));
        }
    }

    // slots are ordered by rows
    // ex. (0-indexed):
    // 0 1 2
    // 3 4 5
    // 6 7 8
    public int getSlotIndex(int x, int y) {
        return y * sizeX + x;
    }

    public Tuple<Integer, Integer> getSlotXY(int i) {
        int y = i / sizeX;
        int x = i % sizeX;
        return new Tuple<>(x, y);
    }

    // for column scrolling
    // ex. (in->out)
    // 0->0 3->1 6->2
    // 1->3 4->4 7->5
    // 2->6 5->7 8->8
    public int getSlotIndexColumn(int i) {
        int x = i % sizeX;
        int y = i / sizeX;
        int out = x * sizeY + y;
        InventoryOverhaul.LOGGER.info("in: {}, x: {}, y: {}, out: {}", i, x, y, out);
        return out;
    }
}
