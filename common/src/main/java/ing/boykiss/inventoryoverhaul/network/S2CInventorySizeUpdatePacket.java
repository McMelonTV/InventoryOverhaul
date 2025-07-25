package ing.boykiss.inventoryoverhaul.network;

import dev.architectury.networking.NetworkManager;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import ing.boykiss.inventoryoverhaul.imixin.IMixinInventory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public record S2CInventorySizeUpdatePacket(int x, int y) implements CustomPacketPayload {
    public static final Type<S2CInventorySizeUpdatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "inventory_size_update_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CInventorySizeUpdatePacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CInventorySizeUpdatePacket payload) {
            buf.writeInt(payload.x());
            buf.writeInt(payload.y());
        }

        @Override
        public @NotNull S2CInventorySizeUpdatePacket decode(RegistryFriendlyByteBuf buf) {
            int x = buf.readInt();
            int y = buf.readInt();
            return new S2CInventorySizeUpdatePacket(x, y);
        }
    };

    public static void handle(S2CInventorySizeUpdatePacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Inventory inventory = player.getInventory();
        ((IMixinInventory) inventory).inventoryoverhaul$setSize(packet.x(), packet.y(), false);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
