package daam.client.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.client.DrawUtils;
import daam.client.RegionCreatorHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.awt.*;

public class GuiRegionCreator extends GuiScreen {

    @Override
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;
        this.addButton(new GuiButtonDAAM(0, centerX - 60, centerY + 25, ChatFormatting.BOLD + "Save")
                .setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
        this.addButton(new GuiButtonDAAM(1, centerX + 20, centerY + 25, ChatFormatting.BOLD + "Reset")
                .setSound(new SoundEvent(new ResourceLocation("minecraft:block.comparator.click")))
                .setSoundHovered(new SoundEvent(new ResourceLocation("minecraft:block.iron_trapdoor.close"))));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            RegionCreatorHandler.create();
        } else {
            RegionCreatorHandler.reset();
        }
        DrawUtils.open(null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        Color purple = new Color(0x8000FF);

        GuiScreen.drawRect(centerX - 103, centerY - 43, centerX + 103, centerY + 43, purple.getRGB());
        GuiScreen.drawRect(centerX - 100, centerY - 40, centerX + 100, centerY + 40, Color.BLACK.getRGB());

        if (RegionCreatorHandler.LEFT != null) {
            String text = ChatFormatting.BOLD + String.format("Left{x=%s,y=%s,z=%s}",
                    RegionCreatorHandler.LEFT.getX(), RegionCreatorHandler.LEFT.getY(), RegionCreatorHandler.LEFT.getZ());
            fontRenderer.drawStringWithShadow(text, centerX - fontRenderer.getStringWidth(text) / 2f, centerY - 20, purple.getRGB());
        } else {
            String text = ChatFormatting.BOLD + "Left not highlighted";
            fontRenderer.drawStringWithShadow(text, centerX - fontRenderer.getStringWidth(text) / 2f, centerY - 20, Color.RED.brighter().getRGB());
        }
        if (RegionCreatorHandler.RIGHT != null) {
            String text = ChatFormatting.BOLD + String.format("Right{x=%s,y=%s,z=%s}",
                    RegionCreatorHandler.RIGHT.getX(), RegionCreatorHandler.RIGHT.getY(), RegionCreatorHandler.RIGHT.getZ());
            fontRenderer.drawStringWithShadow(text, centerX - fontRenderer.getStringWidth(text) / 2f, centerY, purple.getRGB());
        } else {
            String text = ChatFormatting.BOLD + "Right not highlighted";
            fontRenderer.drawStringWithShadow(text, centerX - fontRenderer.getStringWidth(text) / 2f, centerY, Color.RED.brighter().getRGB());
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
