package daam.common.world;

import daam.DAAM;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Mod.EventBusSubscriber
public class DAAMWorldSavedData extends WorldSavedData {

    private static final String DATA_NAME = DAAM.MODID + "_data";

    public ConcurrentHashMap<RegionChunks, Region> regions = new ConcurrentHashMap<>();

    public DAAMWorldSavedData() {
        super(DATA_NAME);
    }

    public DAAMWorldSavedData(String name) {
        super(name);
    }

    /**
     * Use only on SERVER SIDE !!!
     *
     * @param world
     * @return DecorationWorldSavedData
     */
    public static DAAMWorldSavedData get(World world) {
        DAAMWorldSavedData instance = (DAAMWorldSavedData) world.loadData(DAAMWorldSavedData.class, DATA_NAME);
        if (instance == null) {
            instance = new DAAMWorldSavedData();
            world.setData(DATA_NAME, instance);
        }
        return instance;
    }

    @SubscribeEvent
    public static void onWorldLoadEvent(WorldEvent.Load event) {
        if (FMLCommonHandler.instance().getSide().isServer() && event.getWorld().provider.getDimension() == 0) {
            DAAMWorldSavedData.get(event.getWorld());
        }
    }

    @SubscribeEvent
    public void worldSave(WorldEvent.Save event) {
        if (FMLCommonHandler.instance().getSide().isServer() && event.getWorld().provider.getDimension() == 0) {
            DAAMWorldSavedData.get(event.getWorld()).markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        regions.clear();
        for (String regionChunksString : compound.getKeySet()) {
            NBTTagCompound regionNBT = compound.getCompoundTag(regionChunksString);

            Region region = new Region();
            region.deserializeNBT(regionNBT);

            RegionChunks regionChunks = new RegionChunks(region.getUUID());
            regionChunks.fromString(regionChunksString);

            regions.put(regionChunks, region);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        for (Map.Entry<RegionChunks, Region> region : regions.entrySet()) {
            compound.setTag(region.getKey().toString(), region.getValue().serializeNBT());
        }
        return compound;
    }


    public static HashMap<RegionChunks, Region> readNBT(NBTTagCompound compound) {
        HashMap<RegionChunks, Region> regions = new HashMap<>();
        for (String regionChunksString : compound.getKeySet()) {
            NBTTagCompound regionNBT = compound.getCompoundTag(regionChunksString);

            Region region = new Region();
            region.deserializeNBT(regionNBT);

            RegionChunks regionChunks = new RegionChunks(region.getUUID());
            regionChunks.fromString(regionChunksString);

            regions.put(regionChunks, region);
        }
        return regions;
    }


    public static NBTTagCompound writeNBT(HashMap<RegionChunks, Region> regions) {
        NBTTagCompound compound = new NBTTagCompound();
        for (Map.Entry<RegionChunks, Region> region : regions.entrySet()) {
            compound.setTag(region.getKey().toString(), region.getValue().serializeNBT());
        }
        return compound;
    }

}
