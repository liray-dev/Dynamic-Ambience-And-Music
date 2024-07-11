package daam.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber(Side.CLIENT)
public class KeyHandler {

    private static final String CATEGORY = "key.categories.daam";

    public static final KeyBinding musicBinding = new KeyBinding("key.daam.music_mute", Keyboard.KEY_LBRACKET, CATEGORY);
    public static final KeyBinding ambientBinding = new KeyBinding("key.daam.ambient_mute", Keyboard.KEY_RBRACKET, CATEGORY);

    static {
        ClientRegistry.registerKeyBinding(musicBinding);
        ClientRegistry.registerKeyBinding(ambientBinding);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (musicBinding.isPressed()) {
            RegionSoundHandler.musicMute = !RegionSoundHandler.musicMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
        }
        if (ambientBinding.isPressed()) {
            RegionSoundHandler.ambientMute = !RegionSoundHandler.ambientMute;
            RegionSoundHandler handler = RegionHandler.soundHandler;
            if (handler.currentRegion != null) {
                handler.switchRegion(handler.currentRegion);
            }
        }
    }
}
