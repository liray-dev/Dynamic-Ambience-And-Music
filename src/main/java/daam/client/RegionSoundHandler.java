package daam.client;

import daam.common.sounds.DynamicSound;
import daam.common.world.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;

public class RegionSoundHandler {

    public static float musicVolume = 1F;
    public static float ambientVolume = 1F;
    public static boolean musicMute = false;
    public static boolean ambientMute = false;
    public SoundHandler sound;
    public Region currentRegion;

    private boolean isDay;

    public DynamicSound dynamicMusicDay;
    public DynamicSound dynamicAmbientDay;

    public DynamicSound dynamicMusicNight;
    public DynamicSound dynamicAmbientNight;

    public void tick(Region updatedRegion) {
        long time = Minecraft.getMinecraft().world.getWorldTime() % 24000;
        boolean isDay = !(time >= 13000 && time <= 23000);
        if (this.isDay != isDay) {
            if (!updatedRegion.isTIME_FACTOR()) {
                if (dynamicMusicDay == null && dynamicAmbientDay == null) {
                    stopAll();
                }
            } else {
                stopAll();
            }
        }
        this.isDay = isDay;

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

        if (isDay || !updatedRegion.isTIME_FACTOR()) {
            if (dynamicMusicDay != null) {
                boolean flag = sound.isSoundPlaying(dynamicMusicDay);
                boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicMusicDay.getSoundLocation());
                if (!flag && !flag2 && !musicMute) {
                    if (!sound.sndManager.playingSounds.containsValue(dynamicMusicDay)) {
                        sound.playSound(dynamicMusicDay);
                    }
                }
            }

            if (dynamicAmbientDay != null) {
                boolean flag = sound.isSoundPlaying(dynamicAmbientDay);
                boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicAmbientDay.getSoundLocation());
                if (!flag && !flag2 && !ambientMute) {
                    if (!sound.sndManager.playingSounds.containsValue(dynamicAmbientDay)) {
                        sound.playSound(dynamicAmbientDay);
                    }
                }
            }
        } else {
            if (dynamicMusicNight != null) {
                boolean flag = sound.isSoundPlaying(dynamicMusicNight);
                boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicMusicNight.getSoundLocation());
                if (!flag && !flag2 && !musicMute) {
                    if (!sound.sndManager.playingSounds.containsValue(dynamicMusicNight)) {
                        sound.playSound(dynamicMusicNight);
                    }
                }
            }

            if (dynamicAmbientNight != null) {
                boolean flag = sound.isSoundPlaying(dynamicAmbientNight);
                boolean flag2 = SoundManager.UNABLE_TO_PLAY.contains(dynamicAmbientNight.getSoundLocation());
                if (!flag && !flag2 && !ambientMute) {
                    if (!sound.sndManager.playingSounds.containsValue(dynamicAmbientNight)) {
                        sound.playSound(dynamicAmbientNight);
                    }
                }
            }
        }

    }

    public void stopAll() {
        currentRegion = null;
        if (dynamicMusicDay != null) {
            dynamicMusicDay.setStop(true);
            dynamicMusicDay = null;
        }
        if (dynamicAmbientDay != null) {
            dynamicAmbientDay.setStop(true);
            dynamicAmbientDay = null;
        }
        if (dynamicMusicNight != null) {
            dynamicMusicNight.setStop(true);
            dynamicMusicNight = null;
        }
        if (dynamicAmbientNight != null) {
            dynamicAmbientNight.setStop(true);
            dynamicAmbientNight = null;
        }
    }

    public void switchRegion(Region updatedRegion) {
        if (isDay || !updatedRegion.isTIME_FACTOR()) {
            DynamicSound updatedMusic = new DynamicSound(updatedRegion.getMUSIC_PATH_DAY(), true);
            DynamicSound updatedAmbient = new DynamicSound(updatedRegion.getAMBIENT_PATH_DAY(), false);

            if (musicMute) {
                if (dynamicMusicDay != null) {
                    dynamicMusicDay.setStop(true);
                }
                dynamicMusicDay = null;
            } else {
                if ((dynamicMusicDay == null || !dynamicMusicDay.getSoundLocation().equals(updatedMusic.getSoundLocation()))) {
                    if (dynamicMusicDay != null) {
                        dynamicMusicDay.setStop(true);
                    }
                    sound.playSound(updatedMusic);
                    dynamicMusicDay = updatedMusic;
                }
            }

            if (ambientMute) {
                if (dynamicAmbientDay != null) {
                    dynamicAmbientDay.setStop(true);
                }
                dynamicAmbientDay = null;
            } else {
                if (dynamicAmbientDay == null || !dynamicAmbientDay.getSoundLocation().equals(updatedAmbient.getSoundLocation())) {
                    if (dynamicAmbientDay != null) {
                        dynamicAmbientDay.setStop(true);
                    }
                    sound.playSound(updatedAmbient);
                    dynamicAmbientDay = updatedAmbient;
                }
            }
        } else {
            DynamicSound updatedMusic = new DynamicSound(updatedRegion.getMUSIC_PATH_NIGHT(), true);
            DynamicSound updatedAmbient = new DynamicSound(updatedRegion.getAMBIENT_PATH_NIGHT(), false);

            if (musicMute) {
                if (dynamicMusicNight != null) {
                    dynamicMusicNight.setStop(true);
                }
                dynamicMusicNight = null;
            } else {
                if ((dynamicMusicNight == null || !dynamicMusicNight.getSoundLocation().equals(updatedMusic.getSoundLocation()))) {
                    if (dynamicMusicNight != null) {
                        dynamicMusicNight.setStop(true);
                    }
                    sound.playSound(updatedMusic);
                    dynamicMusicNight = updatedMusic;
                }
            }

            if (ambientMute) {
                if (dynamicAmbientNight != null) {
                    dynamicAmbientNight.setStop(true);
                }
                dynamicAmbientNight = null;
            } else {
                if (dynamicAmbientNight == null || !dynamicAmbientNight.getSoundLocation().equals(updatedAmbient.getSoundLocation())) {
                    if (dynamicAmbientNight != null) {
                        dynamicAmbientNight.setStop(true);
                    }
                    sound.playSound(updatedAmbient);
                    dynamicAmbientNight = updatedAmbient;
                }
            }
        }

    }

}
