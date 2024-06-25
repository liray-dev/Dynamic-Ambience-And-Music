package daam.client.screens;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.awt.*;
import java.util.function.Consumer;

@Getter
public class GuiButtonDAAM extends GuiButton {

    private Consumer<GuiButtonDAAM> callback;
    private boolean firstHovered = false;

    private Color background;

    private ResourceLocation icon;

    private SoundEvent sound;

    private SoundEvent soundHovered;

    public GuiButtonDAAM(int id, int x, int y, String text) {
        super(id, x, y, text);
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        this.height = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
        this.enabled = true;
        this.visible = true;
        this.id = id;
        this.x = x;
        this.y = y;
        this.displayString = text;
    }

    public GuiButtonDAAM(int x, int y, String text, Consumer<GuiButtonDAAM> callback) {
        this(-1, x, y, text);
        this.callback = callback;
    }

    public void setText(String text) {
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
        this.displayString = text;
    }

    public GuiButtonDAAM call() {
        if (this.callback != null) {
            callback.accept(this);
            if (soundHovered != null) {
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(soundHovered, 1.0F));
            }
        }
        return this;
    }

    public GuiButtonDAAM w(int w) {
        this.width = w;
        return this;
    }

    public GuiButtonDAAM h(int h) {
        this.height = h;
        return this;
    }

    public GuiButtonDAAM wh(int w, int h) {
        this.width = w;
        this.height = h;
        return this;
    }

    public GuiButtonDAAM setBackground(Color color) {
        this.background = color;
        return this;
    }

    public GuiButtonDAAM setIcon(ResourceLocation icon) {
        this.icon = icon;
        return this;
    }

    public GuiButtonDAAM setSound(SoundEvent sound) {
        this.sound = sound;
        return this;
    }

    public GuiButtonDAAM setSoundHovered(SoundEvent soundHovered) {
        this.soundHovered = soundHovered;
        return this;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        boolean prevHover = this.hovered;
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        this.firstHovered = !prevHover && hovered;
        int i = this.getHoverState(this.hovered);
        this.mouseDragged(mc, mouseX, mouseY);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (background != null) {
            this.drawGradientRect(
                    this.x - 3,
                    this.y - 3,
                    this.x + this.width + 3,
                    this.y + this.height + 3,
                    background.getRGB(),
                    background.darker().getRGB()
            );
        }


        this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, -1);

        if (hovered && icon != null) {
            mc.getTextureManager().bindTexture(icon);
            int x = this.x - 20;
            int y = this.y - 4;
            int width = 16;
            int height = 16;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x, y + height, this.zLevel).tex(0, 1).endVertex();
            bufferbuilder.pos(x + width, y + height, this.zLevel).tex(1, 1).endVertex();
            bufferbuilder.pos(x + width, y, this.zLevel).tex(1, 0).endVertex();
            bufferbuilder.pos(x, y, this.zLevel).tex(0, 0).endVertex();
            tessellator.draw();
        }
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
        if (firstHovered) {
            if (soundHovered != null) {
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(soundHovered, 1.0F));
            }
        }
    }

    @Override
    public void playPressSound(SoundHandler soundHandlerIn) {
        if (sound != null) {
            soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
        }
    }
}
