package ing.boykiss.inventoryoverhaul.client.config;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class ClientConfig extends AbstractClientConfig {
    @Getter
    @Setter
    @SerializedName("something")
    private String something = "";
}
