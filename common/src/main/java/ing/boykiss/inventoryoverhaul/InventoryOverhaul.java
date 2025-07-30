package ing.boykiss.inventoryoverhaul;

import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.keybind.ModifierKeybind;
import ing.boykiss.inventoryoverhaul.event.ClientStartedEvent;
import ing.boykiss.inventoryoverhaul.event.PlayerJoinEvent;
import ing.boykiss.inventoryoverhaul.event.ServerStartingEvent;
import ing.boykiss.inventoryoverhaul.gamerule.HotbarSizeGameRules;
import ing.boykiss.inventoryoverhaul.gamerule.InventorySizeGameRules;
import ing.boykiss.inventoryoverhaul.network.ModNetwork;
import ing.boykiss.inventoryoverhaul.util.annotations.AnnotationProcessor;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InventoryOverhaul {
    public static final String MOD_ID = "inventoryoverhaul";
    public static final Logger LOGGER = LoggerFactory.getLogger(InventoryOverhaul.class);

    public static @Nullable MinecraftServer server;

    public static void init() {
        InventorySizeGameRules.init();
        HotbarSizeGameRules.init();
        ModNetwork.init();
        PlayerJoinEvent.init();

        EnvExecutor.runInEnv(Env.SERVER, () -> InventoryOverhaul.Server::initServer);
        EnvExecutor.runInEnv(Env.CLIENT, () -> InventoryOverhaul.Client::initClient);
    }

    public static class Server {
        public static void initServer() {
            ServerStartingEvent.init();
            ModNetwork.initServer();
        }
    }

    public static class Client {
        public static void initClient() {
            AnnotationProcessor.validateRequireFieldAnnotations(ClientConfig.class);
            ClientStartedEvent.init();
            ModifierKeybind.init();
            ModNetwork.initClient();
        }
    }
}
