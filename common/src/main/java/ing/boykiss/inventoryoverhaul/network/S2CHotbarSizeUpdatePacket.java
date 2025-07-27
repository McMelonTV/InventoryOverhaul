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

public record S2CHotbarSizeUpdatePacket(int x, int y) implements CustomPacketPayload {
    public static final Type<S2CHotbarSizeUpdatePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(InventoryOverhaul.MOD_ID, "hotbar_size_update_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CHotbarSizeUpdatePacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CHotbarSizeUpdatePacket payload) {
            buf.writeInt(payload.x());
            buf.writeInt(payload.y());
        }

        @Override
        public @NotNull S2CHotbarSizeUpdatePacket decode(RegistryFriendlyByteBuf buf) {
            int x = buf.readInt();
            int y = buf.readInt();
            return new S2CHotbarSizeUpdatePacket(x, y);
        }
    };

    public static void handle(S2CHotbarSizeUpdatePacket packet, NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        Inventory inventory = player.getInventory();
        ((IMixinInventory) inventory).inventoryoverhaul$getHotbar().setSize(packet.x(), packet.y(), false);

        inventory.setSelectedSlot(inventory.getSelectedSlot()); // reset the slot to the last available if outside bounds after size update
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
