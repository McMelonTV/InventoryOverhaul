package ing.boykiss.inventoryoverhaul.mixin.client;

import ing.boykiss.inventoryoverhaul.client.gui.screen.InventorySettingsScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ControlsScreen.class)
public abstract class MixinControlsScreen extends OptionsSubScreen {
    public MixinControlsScreen(Screen screen, Options options, Component component) {
        super(screen, options, component);
    }

    @Inject(method = "addOptions", at = @At(value = "TAIL"))
    private void addOptions(CallbackInfo ci) {
        this.list.addSmall(List.of(Button.builder(Component.translatable("options.inventory_settings"), button -> minecraft.setScreen(new InventorySettingsScreen(this))).build()));
    }
}