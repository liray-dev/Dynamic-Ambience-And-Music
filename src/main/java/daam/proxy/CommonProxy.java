package daam.proxy;

import daam.DAAM;
import daam.common.blocks.BlockRegister;
import daam.common.items.ItemRegister;
import daam.common.tile.SoundBlockTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        ItemRegister.register();
        BlockRegister.register();
        GameRegistry.registerTileEntity(SoundBlockTileEntity.class, new ResourceLocation(DAAM.MODID, "sound_tile"));
    }

    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void server() {

    }

}