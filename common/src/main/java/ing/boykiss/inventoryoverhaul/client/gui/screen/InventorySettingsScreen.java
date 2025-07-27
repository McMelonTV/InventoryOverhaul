package ing.boykiss.inventoryoverhaul.client.gui.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class InventorySettingsScreen extends Screen {
    private static final Component TITLE = Component.translatable("options.inventory_settings.title");
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    protected final Screen lastScreen;

    public InventorySettingsScreen(Screen lastScreen) {
        super(TITLE);
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.addTitle();
        this.addContents();
        this.addFooter();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void addTitle() {
        this.layout.addTitleHeader(this.title, this.font);
    }

    protected void addContents() {
        this.layout.addToContents(Button.builder(Component.literal("test"), (button) -> {

        }).build());
    }

    protected void addFooter() {
        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.lastScreen);
    }
}
