package daam.client.screens;

import com.google.common.collect.Lists;
import daam.client.RegionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class GuiSelectSound extends GuiScreen {

    public String selected;
    protected GuiScreen parentScreen;
    protected Consumer<String> consumer;
    private GuiSelectSound.List list;

    private GuiTextField filter;

    public GuiSelectSound(GuiScreen screen, Consumer<String> consumer) {
        this.parentScreen = screen;
        this.consumer = consumer;
    }

    public void initGui() {
        this.addButton(new GuiOptionButton(6, this.width / 2 - 75, this.height - 38, I18n.format("gui.done")));
        this.list = new GuiSelectSound.List(this.mc);
        this.list.registerScrollButtons(7, 8);
        this.filter = new GuiTextField(-1, mc.fontRenderer, this.width / 2 - 100, 5, 200, 20);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.filter.updateCursorCounter();
        this.list.filter = filter.getText();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.filter.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.filter.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.list.handleMouseInput();
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            switch (button.id) {
                case 5:
                    break;
                case 6:
                    if (selected != null) {
                        consumer.accept(selected);
                    }
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                default:
                    this.list.actionPerformed(button);
            }
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.list.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "(Select sound and click done!)", this.width / 2, this.height - 56, 8421504);
        this.filter.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    class List extends GuiSlot {

        private final java.util.List<String> all = Lists.newArrayList();
        private final java.util.List<String> filtered = Lists.newArrayList();

        public String filter = "";

        public List(Minecraft mcIn) {
            super(mcIn, GuiSelectSound.this.width, GuiSelectSound.this.height, 32, GuiSelectSound.this.height - 65 + 4, 18);
            this.all.addAll(RegionHandler.loadedSounds);
            this.filtered.addAll(RegionHandler.loadedSounds);
        }

        protected int getSize() {
            return this.filtered.size();
        }

        protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            GuiSelectSound.this.selected = this.filtered.get(slotIndex);
        }

        protected boolean isSelected(int slotIndex) {
            return (this.filtered.get(slotIndex)).equals(GuiSelectSound.this.selected);
        }

        protected int getContentHeight() {
            return this.getSize() * 18;
        }

        protected void drawBackground() {
            GuiSelectSound.this.drawDefaultBackground();
        }

        protected void tick() {
            this.filtered.clear();
            this.filtered.addAll(this.all.stream()
                    .filter(s -> s.contains(filter))
                    .collect(Collectors.toList()));
        }

        protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
            String string = this.filtered.get(slotIndex);
            if (string.contains(filter)) {
                GuiSelectSound.this.drawCenteredString(GuiSelectSound.this.fontRenderer, string, this.width / 2, yPos + 1, 16777215);
            }
        }
    }
}
