package daam.client.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.client.loader.DynamicAmbienceAndMusicLoader;
import daam.common.network.packets.client.RemoveRegionPacket;
import daam.common.network.packets.client.UpdateRegionPacket;
import daam.common.world.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class GuiRegionEditor extends GuiScreen {

    private final Region region;
    private GuiTextField MUSIC_FIELD;
    private GuiTextField AMBIENT_FIELD;

    public GuiRegionEditor(Region region) {
        this.region = new Region();
        this.region.deserializeNBT(region.serializeNBT());
    }

    @Override
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;

        MUSIC_FIELD = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 20, 200, 20);
        MUSIC_FIELD.setText(region.getMUSIC_PATH());
        MUSIC_FIELD.setMaxStringLength(256);

        this.addButton(new GuiButtonDAAM(centerX + 105, centerY - 15, ChatFormatting.BOLD + "?", (c) -> {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (music) -> {
                region.setMUSIC_PATH(DAAM.MODID + ":" + music);
                MUSIC_FIELD.setText(region.getMUSIC_PATH());
            }));
        }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));

        AMBIENT_FIELD = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY + 20, 200, 20);
        AMBIENT_FIELD.setText(region.getAMBIENT_PATH());
        AMBIENT_FIELD.setMaxStringLength(256);

        this.addButton(new GuiButtonDAAM(centerX + 105, centerY + 25, ChatFormatting.BOLD + "?", (c) -> {
            Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (ambient) -> {
                region.setAMBIENT_PATH(DAAM.MODID + ":" + ambient);
                MUSIC_FIELD.setText(region.getAMBIENT_PATH());
            }));
        }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));

        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Save region") / 2, centerY + 45, ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "Save region", (c) -> {
            save();
            Minecraft.getMinecraft().displayGuiScreen(null);
        }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click"))).setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));

        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Remove Region") / 2, centerY + 60, ChatFormatting.RED + "" + ChatFormatting.BOLD + "Remove Region", (c) -> {
            remove();
            Minecraft.getMinecraft().displayGuiScreen(null);
        }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click"))).setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));

        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Load new sound") / 2, centerY + 75, ChatFormatting.DARK_PURPLE + "" + ChatFormatting.BOLD + "Load new sound", (c) -> open())
                .setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));

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
        this.MUSIC_FIELD.updateCursorCounter();
        this.AMBIENT_FIELD.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.MUSIC_FIELD.textboxKeyTyped(typedChar, keyCode);
        this.AMBIENT_FIELD.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.MUSIC_FIELD.mouseClicked(mouseX, mouseY, mouseButton);
        this.AMBIENT_FIELD.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        Color purple = new Color(0x8000FF);

        GuiScreen.drawRect(centerX - 153, centerY - 93, centerX + 153, centerY + 93, purple.getRGB());
        GuiScreen.drawRect(centerX - 150, centerY - 90, centerX + 150, centerY + 90, Color.BLACK.getRGB());

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "REGION UUID:",
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "REGION UUID:") / 2f, centerY - 80, purple.getRGB());
        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + region.getUUID(),
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + region.getUUID()) / 2f, centerY - 70, Color.WHITE.getRGB());

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "MUSIC PATH",
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "MUSIC PATH") / 2f, centerY - 30, purple.getRGB());
        this.MUSIC_FIELD.drawTextBox();

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "AMBIENT PATH",
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "AMBIENT PATH") / 2f, centerY + 10, purple.getRGB());
        this.AMBIENT_FIELD.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void remove() {
        DAAM.NETWORK.server(new RemoveRegionPacket(region));
    }

    private void save() {
        Region updatedRegion = new Region();
        updatedRegion.setAABB(region.getAABB());
        updatedRegion.setUUID(region.getUUID());
        updatedRegion.setMUSIC_PATH(MUSIC_FIELD.getText());
        updatedRegion.setAMBIENT_PATH(AMBIENT_FIELD.getText());
        DAAM.NETWORK.server(new UpdateRegionPacket(updatedRegion));
    }

    private void open() {
        CompletableFuture.runAsync(() -> {
            JFrame frame = new DynamicAmbienceAndMusicLoader();
            frame.pack();
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
