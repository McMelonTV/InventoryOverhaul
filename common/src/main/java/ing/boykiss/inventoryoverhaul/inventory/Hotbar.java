package ing.boykiss.inventoryoverhaul.inventory;

import dev.architectury.networking.NetworkManager;
import ing.boykiss.inventoryoverhaul.network.S2CHotbarSizeUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class Hotbar {
    private final Player player;
    private int sizeX;
    private int sizeY;

    public Hotbar(Player player) {
        this.player = player;
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int x) {
        setSize(x, sizeY, true);
    }

    public int getSizeY() {
        return sizeY;
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
}
