package daam.client.screens;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.client.RegionHandler;
import daam.client.RegionSoundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.function.Consumer;

public class GuiVolumeEditor extends GuiScreen {


    public GuiVolumeEditor() {
    }

    @Override
    public void initGui() {
        int centerX = width / 2;
        int centerY = height / 2;

        this.addButton(new VolumeButton(centerX - 100, centerY - 30, 200, 20, "Music", RegionSoundHandler.musicVolume, (volumeButton -> RegionSoundHandler.musicVolume = volumeButton.volume)));
        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Music Mute: " + RegionSoundHandler.musicMute) / 2, centerY - 45, ChatFormatting.BOLD + "Music Mute: " + RegionSoundHandler.musicMute, (b) -> {
            RegionSoundHandler.musicMute = !RegionSoundHandler.musicMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
            b.setText(ChatFormatting.BOLD + "Music Mute: " + RegionSoundHandler.musicMute);
            b.x = centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Music Mute: " + RegionSoundHandler.musicMute) / 2;
        }).setBackground(new Color(0x8000FF)));
        this.addButton(new VolumeButton(centerX - 100, centerY + 30, 200, 20, "Ambient", RegionSoundHandler.ambientVolume, (volumeButton -> RegionSoundHandler.ambientVolume = volumeButton.volume)));
        this.addButton(new GuiButtonDAAM(centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Ambient Mute: " + RegionSoundHandler.ambientMute) / 2, centerY + 15, ChatFormatting.BOLD + "Ambient Mute: " + RegionSoundHandler.ambientMute, (b) -> {
            RegionSoundHandler.ambientMute = !RegionSoundHandler.ambientMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
            b.setText(ChatFormatting.BOLD + "Ambient Mute: " + RegionSoundHandler.ambientMute);
            b.x = centerX - fontRenderer.getStringWidth(ChatFormatting.BOLD + "Ambient Mute: " + RegionSoundHandler.ambientMute) / 2;
        }).setBackground(new Color(0x8000FF)));
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int centerX = width / 2;
        int centerY = height / 2;

        Color purple = new Color(0x8000FF);

        GuiScreen.drawRect(centerX - 153, centerY - 93, centerX + 153, centerY + 93, purple.getRGB());
        GuiScreen.drawRect(centerX - 150, centerY - 90, centerX + 150, centerY + 90, Color.BLACK.getRGB());

        String volumeEditor = ChatFormatting.BOLD + "Volume Editor";
        fontRenderer.drawStringWithShadow(volumeEditor, centerX - fontRenderer.getStringWidth(volumeEditor) / 2f, centerY - 80, purple.getRGB());

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    class VolumeButton extends GuiButton {
        private final String categoryName;
        private final Consumer<VolumeButton> consumer;
        public float volume;
        public boolean pressed;

        public VolumeButton(int x, int y, int width, int height, String name, float volume, Consumer<VolumeButton> consumer) {
            super(-1, x, y, width, height, "");
            this.categoryName = name;
            this.volume = volume;
            this.displayString = this.categoryName + ": " + getDisplayString();
            this.consumer = consumer;
        }

        protected String getDisplayString() {
            float f = volume;
            return f == 0.0F ? "1%" : (int) (f * 100.0F) + "%";
        }

        protected int getHoverState(boolean mouseOver) {
            return 0;
        }

        protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                if (this.pressed) {
                    this.volume = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
                    this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
                    this.displayString = this.categoryName + ": " + getDisplayString();
                    this.consumer.accept(this);
                }

                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.drawTexturedModalRect(this.x + (int) (this.volume * (float) (this.width - 8)), this.y, 0, 66, 4, 20);
                this.drawTexturedModalRect(this.x + (int) (this.volume * (float) (this.width - 8)) + 4, this.y, 196, 66, 4, 20);
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (super.mousePressed(mc, mouseX, mouseY)) {
                this.volume = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
                this.volume = MathHelper.clamp(this.volume, 0.0F, 1.0F);
                this.displayString = this.categoryName + ": " + getDisplayString();
                this.pressed = true;
                this.consumer.accept(this);
                return true;
            } else {
                return false;
            }
        }

        public void playPressSound(SoundHandler soundHandlerIn) {
        }

        public void mouseReleased(int mouseX, int mouseY) {
            if (this.pressed) {
                GuiVolumeEditor.this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }

            this.pressed = false;
        }
    }

}
