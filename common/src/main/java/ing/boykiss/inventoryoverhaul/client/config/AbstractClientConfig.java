package ing.boykiss.inventoryoverhaul.client.config;

import com.google.gson.Gson;
import ing.boykiss.inventoryoverhaul.InventoryOverhaul;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class AbstractClientConfig {
    private static final String CONFIG_FILE_NAME = "inventory_overhaul-client.json";
    private static ClientConfig instance = null;
    private static File configFile = null;

    private static ClientConfig load() {
        try (FileReader fileReader = new FileReader(configFile)) {
            ClientConfig config = new Gson().fromJson(fileReader, ClientConfig.class);
            if (config == null) config = new ClientConfig();
            return config;
        } catch (IOException e) {
            InventoryOverhaul.LOGGER.error(e.getMessage());
            throw new RuntimeException("Could not load ClientConfig");
        }
    }

    public static void init() {
        if (instance != null) throw new IllegalStateException("ClientConfig already initialized");

        File file = Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve(CONFIG_FILE_NAME).toFile();

        if (!file.getParentFile().exists()) {
            boolean dirSuccess = file.getParentFile().mkdirs();
            if (!dirSuccess) throw new RuntimeException("Failed while creating ClientConfig file");
        }

        if (!file.exists()) {
            try {
                boolean fileSuccess = file.createNewFile();
                if (!fileSuccess) throw new RuntimeException("Failed to create ClientConfig file");
            } catch (IOException e) {
                InventoryOverhaul.LOGGER.error(e.getMessage());
                throw new RuntimeException("Failed to create ClientConfig file");
            }
        }

        configFile = file;
        instance = load();

        instance.save();
    }

    public static ClientConfig getInstance() {
        if (instance == null) throw new IllegalStateException("ClientConfig not initialized");

        return instance;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(configFile)) {
            String configJson = new Gson().toJson(this);
            fileWriter.write(configJson);
        } catch (IOException e) {
            InventoryOverhaul.LOGGER.error(e.getMessage());
            throw new RuntimeException("Could not save ClientConfig");
        }
    }
}
