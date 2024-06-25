package daam.client.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daam.DAAM;
import daam.client.RegionHandler;
import daam.common.sounds.GSONSound;
import net.minecraft.client.audio.SoundList;
import net.minecraft.client.audio.SoundListSerializer;
import net.minecraft.util.text.ITextComponent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SoundLoader {

    private static final Gson GSON = (new GsonBuilder()).registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer()).registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();

    private static final ParameterizedType TYPE = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[]{String.class, SoundList.class};
        }

        public Type getRawType() {
            return Map.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    public static HashMap<String, GSONSound> toMap() {
        HashMap<String, GSONSound> map = new HashMap<>();
        for (String loadedSound : RegionHandler.loadedSounds) {
            map.put(loadedSound, new GSONSound(loadedSound));
        }
        return map;
    }

    public static void add(String sound) {
        HashMap<String, GSONSound> sounds = toMap();
        sounds.put(sound, new GSONSound(sound));
        File soundJson = DAAM.SOUND_JSON;
        try (Writer writer = new FileWriter(soundJson)) {
            GSON.toJson(sounds, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RegionHandler.loadedSounds.add(sound);
        RegionHandler.loadSounds();
    }

}
