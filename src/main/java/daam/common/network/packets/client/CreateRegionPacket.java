package daam.common.network.packets.client;

import daam.DAAM;
import daam.common.network.packets.SimpleNBTPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.entity.player.EntityPlayerMP;

public class CreateRegionPacket extends SimpleNBTPacket {

    public CreateRegionPacket() {
    }

    public CreateRegionPacket(Region region) {
        this.compound = region.serializeNBT();
    }

    @Override
    public void server(EntityPlayerMP player) {
        DAAMWorldSavedData savedData = DAAMWorldSavedData.get(player.world);
        Region region = new Region();
        region.deserializeNBT(compound);

        savedData.regions.put(new RegionChunks(region.getUUID(), player.world, region.getAABB()), region);
        savedData.markDirty();

        DAAM.NETWORK.all(new SyncRegionPacket(region));
    }
}
