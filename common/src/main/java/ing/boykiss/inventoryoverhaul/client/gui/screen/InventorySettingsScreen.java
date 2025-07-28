package ing.boykiss.inventoryoverhaul.client.gui.screen;

import ing.boykiss.inventoryoverhaul.client.config.ClientConfig;
import ing.boykiss.inventoryoverhaul.client.gui.widget.config.ConfigWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;

public class InventorySettingsScreen extends Screen {
    private static final Component TITLE = Component.translatable("options.inventory_settings.title");
    public final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    public final GridLayout buttonGrid = layout.addToContents(new GridLayout());
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
        buttonGrid.rowSpacing(8);

        List<? extends ConfigWidget<?>> widgets = ConfigWidget.createAll(ClientConfig.getInstance());
        List<Tuple<GridLayout, Integer>> rows = arrangeWidgetsInRows(widgets);

        int maxRowElementCount = rows.stream()
                .mapToInt(Tuple::getB)
                .max()
                .orElse(0);

        int maxSpacing = (maxRowElementCount - 1) * 8;

        // we want to make sure the overall grid is aligned and all elements have at least 8px of spacing
        for (int i = 0; i < rows.size(); i++) {
            GridLayout row = rows.get(i).getA();
            int rowElementCount = rows.get(i).getB();
            int rowSpacing = maxSpacing / (rowElementCount <= 1 ? 1 : (rowElementCount - 1)); // FIXME: no spacing when only one element
            row.columnSpacing(rowSpacing);
            buttonGrid.addChild(row, i, 0);
        }
    }

    private List<Tuple<GridLayout, Integer>> arrangeWidgetsInRows(List<? extends ConfigWidget<?>> widgets) {
        List<Tuple<GridLayout, Integer>> rows = new ArrayList<>();

        GridLayout currentRow = new GridLayout();
        int currentRowIndex = 0;
        int currentRowWidth = 0;
        int currentRowElementIndex = 0;
        for (ConfigWidget<?> widget : widgets) {
            int widgetSize = widget.getSize().getSize();

            if (currentRowWidth != 0 && (currentRowWidth + widgetSize) > 300) {
                rows.add(new Tuple<>(currentRow, currentRowElementIndex));
                currentRow = new GridLayout();
                currentRowIndex++;
                currentRowWidth = 0;
                currentRowElementIndex = 0;
            }

            currentRow.addChild(widget.getWidget(), currentRowIndex, currentRowElementIndex);
            currentRowWidth += widgetSize;
            currentRowElementIndex++;
        }

        if (currentRowIndex != 0) {
            rows.add(new Tuple<>(currentRow, currentRowElementIndex));
        }

        return rows;
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
