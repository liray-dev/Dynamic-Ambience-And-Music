package daam.common.sounds;

import daam.client.RegionSoundHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import java.util.function.Consumer;

public class DynamicSound extends PositionedSound implements ITickableSound {

    public final boolean flag;

    @Setter
    @Getter
    private boolean stop = false;

    private boolean finallyStop = false;

    public DynamicSound(String soundIn, boolean flag) {
        super(new ResourceLocation(soundIn), SoundCategory.MASTER);
        this.volume = 0.01f;
        this.flag = flag;
    }

    @Override
    public boolean canRepeat() {
        return true;
    }

    @Override
    public boolean isDonePlaying() {
        return finallyStop;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @Override
    public void update() {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player != null) {
            this.xPosF = (float) mc.player.posX;
            this.yPosF = (float) mc.player.posY;
            this.zPosF = (float) mc.player.posZ;
        }

        float limit = 1F;
        if (flag) {
            limit = RegionSoundHandler.musicVolume;
        } else {
            limit = RegionSoundHandler.ambientVolume;
        }

        if (stop) {
            if (volume - 0.05f <= 0) {
                this.finallyStop = true;
            }
            this.volume -= 0.05f;
        } else {
            if (volume + 0.05f >= limit) {
                this.volume = limit;
            }
            this.volume += 0.05f;
        }
    }

}
