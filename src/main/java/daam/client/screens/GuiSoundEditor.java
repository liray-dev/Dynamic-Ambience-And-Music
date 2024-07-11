package daam.client.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.client.DrawUtils;
import daam.common.network.packets.client.UpdateSoundBlockPacket;
import daam.common.tile.SoundBlockTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.awt.*;
import java.io.IOException;

public class GuiSoundEditor extends GuiScreen {

    protected final SoundBlockTileEntity soundBlockTile;
    private GuiTextField soundPath;
    private GuiTextField volume;
    private GuiTextField delay;

    public GuiSoundEditor(SoundBlockTileEntity soundBlockTile) {
        this.soundBlockTile = soundBlockTile;
    }

    @Override
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;

        soundPath = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 60, 200, 20);
        soundPath.setText(soundBlockTile.soundPath);
        soundPath.setMaxStringLength(256);
        volume = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 20, 200, 20);
        volume.setText(String.valueOf(soundBlockTile.volume));
        delay = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY + 20, 200, 20);
        delay.setText(String.valueOf(soundBlockTile.delay));

        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Done") / 2, centerY + 45, ChatFormatting.BOLD + "Done", (c) -> {
            try {
                soundBlockTile.soundPath = soundPath.getText();
                soundBlockTile.volume = Float.parseFloat(volume.getText());
                soundBlockTile.delay = Integer.parseInt(delay.getText());
                DAAM.NETWORK.server(new UpdateSoundBlockPacket(soundBlockTile));
                DrawUtils.open(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click"))).setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof GuiButtonDAAM) {
            ((GuiButtonDAAM) button).call();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.soundPath.updateCursorCounter();
        this.volume.updateCursorCounter();
        this.delay.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.soundPath.textboxKeyTyped(typedChar, keyCode);
        this.volume.textboxKeyTyped(typedChar, keyCode);
        this.delay.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.soundPath.mouseClicked(mouseX, mouseY, mouseButton);
        this.volume.mouseClicked(mouseX, mouseY, mouseButton);
        this.delay.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        Color purple = new Color(0x8000FF);

        GuiScreen.drawRect(centerX - 153, centerY - 93, centerX + 153, centerY + 93, purple.getRGB());
        GuiScreen.drawRect(centerX - 150, centerY - 90, centerX + 150, centerY + 90, Color.BLACK.getRGB());

        String soundPath = ChatFormatting.BOLD + "Sound Path (minecraft:sound_test)";
        fontRenderer.drawStringWithShadow(soundPath, centerX - fontRenderer.getStringWidth(soundPath) / 2f, centerY - 70, purple.getRGB());
        this.soundPath.drawTextBox();
        String volume = ChatFormatting.BOLD + "Volume (1.0 = 16 block)";
        fontRenderer.drawStringWithShadow(volume, centerX - fontRenderer.getStringWidth(volume) / 2f, centerY - 30, purple.getRGB());
        this.volume.drawTextBox();
        String delay = ChatFormatting.BOLD + "Delay (20 tick = 1 second)";
        fontRenderer.drawStringWithShadow(delay, centerX - fontRenderer.getStringWidth(delay) / 2f, centerY + 10, purple.getRGB());
        this.delay.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
