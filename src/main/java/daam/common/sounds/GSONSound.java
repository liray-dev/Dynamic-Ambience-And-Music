package daam.common.sounds;

import daam.DAAM;

import java.util.ArrayList;

public class GSONSound {

    public ArrayList<SoundProperty> sounds = new ArrayList<>();

    public GSONSound(String loadedSound) {
        sounds.add(new SoundProperty(DAAM.MODID + ":" + loadedSound));
    }

    public static class SoundProperty {

        public String name;
        public boolean stream = true;

        public SoundProperty(String name) {
            this.name = name;
        }

    }

}
