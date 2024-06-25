package daam.common.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class ItemRegister {

    public static ArrayList<Item> items = new ArrayList<>();

    public static Item regionEditor = new RegionEditor();
    public static Item regionWand = new RegionWand();
    public static Item lightStick = new LightStick();
    public static Item soundStick = new SoundStick();
    public static Item volumeEditor = new VolumeEditor();

    static {
        items.add(regionEditor);
        items.add(regionWand);
        items.add(lightStick);
        items.add(soundStick);
        items.add(volumeEditor);
    }

    public static void register() {
        for (Item item : items) {
            apply(item);
        }
    }

    private static void apply(Item item) {
        ForgeRegistries.ITEMS.register(item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender() {
        for (Item item : items) {
            applyRender(item);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void applyRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
