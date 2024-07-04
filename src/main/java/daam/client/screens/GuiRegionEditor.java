package daam.client.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.Resources;
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
    private GuiTextField MUSIC_FIELD_DAY;
    private GuiTextField AMBIENT_FIELD_DAY;

    private GuiTextField MUSIC_FIELD_NIGHT;
    private GuiTextField AMBIENT_FIELD_NIGHT;

    private boolean timeFactor;

    public GuiRegionEditor(Region region) {
        this.region = new Region();
        this.region.deserializeNBT(region.serializeNBT());
    }

    @Override
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;
        timeFactor = region.isTIME_FACTOR();
        {
            MUSIC_FIELD_DAY = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 90, 200, 20);
            MUSIC_FIELD_DAY.setText(region.getMUSIC_PATH_DAY());
            MUSIC_FIELD_DAY.setMaxStringLength(256);
            this.addButton(new GuiButtonDAAM(centerX + 105, centerY - 85, ChatFormatting.BOLD + "?", (c) -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (music) -> {
                    region.setMUSIC_PATH_DAY(DAAM.MODID + ":" + music);
                    MUSIC_FIELD_DAY.setText(region.getMUSIC_PATH_DAY());
                }));
            }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                    .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
            ///////////////////////////////////////////////////////////
            AMBIENT_FIELD_DAY = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 55, 200, 20);
            AMBIENT_FIELD_DAY.setText(region.getAMBIENT_PATH_DAY());
            AMBIENT_FIELD_DAY.setMaxStringLength(256);
            this.addButton(new GuiButtonDAAM(centerX + 105, centerY - 50, ChatFormatting.BOLD + "?", (c) -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (ambient) -> {
                    region.setAMBIENT_PATH_DAY(DAAM.MODID + ":" + ambient);
                    AMBIENT_FIELD_DAY.setText(region.getAMBIENT_PATH_DAY());
                }));
            }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                    .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
        }

        {
            MUSIC_FIELD_NIGHT = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY - 20, 200, 20);
            MUSIC_FIELD_NIGHT.setText(region.getMUSIC_PATH_NIGHT());
            MUSIC_FIELD_NIGHT.setMaxStringLength(256);
            MUSIC_FIELD_NIGHT.setEnabled(timeFactor);
            this.addButton(new GuiButtonDAAM(centerX + 105, centerY - 15, ChatFormatting.BOLD + "?", (c) -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (music) -> {
                    region.setMUSIC_PATH_NIGHT(DAAM.MODID + ":" + music);
                    MUSIC_FIELD_NIGHT.setText(region.getMUSIC_PATH_NIGHT());
                }));
            }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                    .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
            ///////////////////////////////////////////////////////////
            AMBIENT_FIELD_NIGHT = new GuiTextField(0, mc.fontRenderer, centerX - 100, centerY + 15, 200, 20);
            AMBIENT_FIELD_NIGHT.setText(region.getAMBIENT_PATH_NIGHT());
            AMBIENT_FIELD_NIGHT.setMaxStringLength(256);
            AMBIENT_FIELD_NIGHT.setEnabled(timeFactor);
            this.addButton(new GuiButtonDAAM(centerX + 105, centerY + 20, ChatFormatting.BOLD + "?", (c) -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiSelectSound(this, (ambient) -> {
                    region.setAMBIENT_PATH_NIGHT(DAAM.MODID + ":" + ambient);
                    AMBIENT_FIELD_NIGHT.setText(region.getAMBIENT_PATH_NIGHT());
                }));
            }).setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                    .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
        }


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

        this.addButton(new GuiButtonDAAM(centerX - 130, centerY - 30, "    ", (c) -> {
            timeFactor = !timeFactor;
            c.setIcon(timeFactor ? Resources.TIME_FACTOR_ACTIVE : Resources.TIME_FACTOR_DISABLED);
            MUSIC_FIELD_NIGHT.setEnabled(timeFactor);
            AMBIENT_FIELD_NIGHT.setEnabled(timeFactor);
        }).setIcon(timeFactor ? Resources.TIME_FACTOR_ACTIVE : Resources.TIME_FACTOR_DISABLED)
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
        this.MUSIC_FIELD_DAY.updateCursorCounter();
        this.AMBIENT_FIELD_DAY.updateCursorCounter();
        this.MUSIC_FIELD_NIGHT.updateCursorCounter();
        this.AMBIENT_FIELD_NIGHT.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.MUSIC_FIELD_DAY.textboxKeyTyped(typedChar, keyCode);
        this.MUSIC_FIELD_NIGHT.textboxKeyTyped(typedChar, keyCode);
        this.AMBIENT_FIELD_DAY.textboxKeyTyped(typedChar, keyCode);
        this.AMBIENT_FIELD_NIGHT.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.MUSIC_FIELD_DAY.mouseClicked(mouseX, mouseY, mouseButton);
        this.MUSIC_FIELD_NIGHT.mouseClicked(mouseX, mouseY, mouseButton);
        this.AMBIENT_FIELD_DAY.mouseClicked(mouseX, mouseY, mouseButton);
        this.AMBIENT_FIELD_NIGHT.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        Color purple = new Color(0x8000FF);
        Color purpleD = new Color(0x33310065, true);

        GuiScreen.drawRect(centerX - 153, centerY - 113, centerX + 153, centerY + 113, purple.getRGB());
        GuiScreen.drawRect(centerX - 150, centerY - 110, centerX + 150, centerY + 110, Color.BLACK.getRGB());


        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "MUSIC PATH " + (timeFactor ? ChatFormatting.RESET + "(DAY)" : ""),
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "MUSIC PATH " + (timeFactor ? ChatFormatting.RESET + "(DAY)" : "")) / 2f, centerY - 100, purple.getRGB());
        this.MUSIC_FIELD_DAY.drawTextBox();

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "AMBIENT PATH " + (timeFactor ? ChatFormatting.RESET + "(DAY)" : ""),
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "AMBIENT PATH " + (timeFactor ? ChatFormatting.RESET + "(DAY)" : "")) / 2f, centerY - 65, purple.getRGB());
        this.AMBIENT_FIELD_DAY.drawTextBox();

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "MUSIC PATH " + (timeFactor ? ChatFormatting.RESET + "(NIGHT)" : ""),
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "MUSIC PATH " + (timeFactor ? ChatFormatting.RESET + "(NIGHT)" : "")) / 2f, centerY - 30, timeFactor ? purple.getRGB() : purpleD.getRGB());
        this.MUSIC_FIELD_NIGHT.drawTextBox();

        fontRenderer.drawStringWithShadow(ChatFormatting.BOLD + "AMBIENT PATH " + (timeFactor ? ChatFormatting.RESET + "(NIGHT)" : ""),
                centerX - mc.fontRenderer.getStringWidth(ChatFormatting.BOLD + "AMBIENT PATH " + (timeFactor ? ChatFormatting.RESET + "(NIGHT)" : "")) / 2f, centerY + 5, timeFactor ? purple.getRGB() : purpleD.getRGB());
        this.AMBIENT_FIELD_NIGHT.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void remove() {
        DAAM.NETWORK.server(new RemoveRegionPacket(region));
    }

    private void save() {
        Region updatedRegion = new Region();
        updatedRegion.setAABB(region.getAABB());
        updatedRegion.setUUID(region.getUUID());
        updatedRegion.setMUSIC_PATH_DAY(MUSIC_FIELD_DAY.getText());
        updatedRegion.setMUSIC_PATH_NIGHT(MUSIC_FIELD_NIGHT.getText());
        updatedRegion.setAMBIENT_PATH_DAY(AMBIENT_FIELD_DAY.getText());
        updatedRegion.setAMBIENT_PATH_NIGHT(AMBIENT_FIELD_NIGHT.getText());
        updatedRegion.setTIME_FACTOR(timeFactor);
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
