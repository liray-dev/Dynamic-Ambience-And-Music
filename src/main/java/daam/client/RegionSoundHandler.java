package daam.client;

import daam.common.sounds.DynamicSound;
import daam.common.world.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.TextComponentString;

import java.util.function.Consumer;

public class RegionSoundHandler {

    public SoundHandler sound;

    public Region currentRegion;
    public DynamicSound dynamicMusic;
    public DynamicSound dynamicAmbient;

    public static float musicVolume = 1F;
    public static float ambientVolume = 1F;

    public static boolean musicMute = false;
    public static boolean ambientMute = false;

    public void tick(Region updatedRegion) {
        sound = Minecraft.getMinecraft().getSoundHandler();

        if (currentRegion == null) {
            this.currentRegion = updatedRegion;
            switchRegion(updatedRegion);
        } else {
            boolean flag = currentRegion.equals(updatedRegion);

            if (!flag) {
                switchRegion(updatedRegion);
                currentRegion = updatedRegion;
            }
        }

        if (dynamicMusic != null) {
            boolean flag = sound.isSoundPlaying(dynamicMusic);
            boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicMusic.getSoundLocation());
            if (!flag && !flag2 && !musicMute) {
                if (!sound.sndManager.playingSounds.containsValue(dynamicMusic)) {
                    sound.playSound(dynamicMusic);
                    // TODO
                }
            }
        }

        if (dynamicAmbient != null) {
            boolean flag = sound.isSoundPlaying(dynamicAmbient);
            boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicAmbient.getSoundLocation());
            if (!flag && !flag2 && !ambientMute) {
                if (!sound.sndManager.playingSounds.containsValue(dynamicAmbient)) {
                    sound.playSound(dynamicAmbient);
                    // TODO
                }
            }
        }
    }

    public void stop() {
        currentRegion = null;
        if (dynamicMusic != null) {
            dynamicMusic.setStop(true);
            dynamicMusic = null;
        }
        if (dynamicAmbient != null) {
            dynamicAmbient.setStop(true);
            dynamicAmbient = null;
        }
    }

    public void switchRegion(Region updatedRegion) {
        DynamicSound updatedMusic = new DynamicSound(updatedRegion.getMUSIC_PATH(), true);
        DynamicSound updatedAmbient = new DynamicSound(updatedRegion.getAMBIENT_PATH(), false);

        if (musicMute) {
            if (dynamicMusic != null) {
                dynamicMusic.setStop(true);
            }
            dynamicMusic = null;
        } else {
            if ((dynamicMusic == null || !dynamicMusic.getSoundLocation().equals(updatedMusic.getSoundLocation()))) {
                if (dynamicMusic != null) {
                    dynamicMusic.setStop(true);
                }
                sound.playSound(updatedMusic);
                dynamicMusic = updatedMusic;
            }
        }

        if (ambientMute) {
            if (dynamicAmbient != null) {
                dynamicAmbient.setStop(true);
            }
            dynamicAmbient = null;
        } else {
            if (dynamicAmbient == null || !dynamicAmbient.getSoundLocation().equals(updatedAmbient.getSoundLocation())) {
                if (dynamicAmbient != null) {
                    dynamicAmbient.setStop(true);
                }
                sound.playSound(updatedAmbient);
                dynamicAmbient = updatedAmbient;
            }
        }

    }

}
