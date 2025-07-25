package ing.boykiss.inventoryoverhaul.network;

import dev.architectury.networking.NetworkManager;

public class ModNetwork {
    public static void init() {
    }

    public static void initServer() {
        NetworkManager.registerS2CPayloadType(S2CInventorySizeUpdatePacket.TYPE, S2CInventorySizeUpdatePacket.CODEC);
        NetworkManager.registerS2CPayloadType(S2CHotbarSizeUpdatePacket.TYPE, S2CHotbarSizeUpdatePacket.CODEC);
    }

    public static void initClient() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2CInventorySizeUpdatePacket.TYPE, S2CInventorySizeUpdatePacket.CODEC, S2CInventorySizeUpdatePacket::handle);
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, S2CHotbarSizeUpdatePacket.TYPE, S2CHotbarSizeUpdatePacket.CODEC, S2CHotbarSizeUpdatePacket::handle);
    }
}
