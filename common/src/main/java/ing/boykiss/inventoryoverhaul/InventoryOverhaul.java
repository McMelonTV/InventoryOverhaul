package ing.boykiss.inventoryoverhaul;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import ing.boykiss.inventoryoverhaul.client.config.AbstractClientConfig;
import ing.boykiss.inventoryoverhaul.client.keybind.ModifierKeybind;
import ing.boykiss.inventoryoverhaul.event.PlayerJoinEvent;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.gamerule.InventorySizeGameRules;
import ing.boykiss.inventoryoverhaul.network.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InventoryOverhaul {
    public static final String MOD_ID = "inventoryoverhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger(InventoryOverhaul.class);

    public static void init() {
        InventorySizeGameRules.init();
        HotbarSizeGameRules.init();
        ModNetwork.init();
        PlayerJoinEvent.init();

        EnvExecutor.runInEnv(Env.SERVER, () -> InventoryOverhaul.Server::initServer);
        EnvExecutor.runInEnv(Env.CLIENT, () -> InventoryOverhaul.Client::initClient);
    }

    @Environment(EnvType.SERVER)
    public static class Server {
        @Environment(EnvType.SERVER)
        public static void initServer() {
            ModNetwork.initServer();
        }
    }

    @Environment(EnvType.CLIENT)
    public static class Client {
        @Environment(EnvType.CLIENT)
        public static void initClient() {
            AbstractClientConfig.init();
            ModifierKeybind.init();
            ModNetwork.initClient();
        }
    }
}
