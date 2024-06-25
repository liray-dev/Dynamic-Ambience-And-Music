package daam.common.network.packets.client;

import daam.client.RegionHandler;
import daam.common.network.packets.SimpleNBTPacket;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class SyncRegionPacket extends SimpleNBTPacket {

    public SyncRegionPacket() {
    }

    public SyncRegionPacket(Region region) {
        this.compound = region.serializeNBT();
    }

    @Override
    public void client(Minecraft mc, EntityPlayer player) {
        Region updatedRegion = new Region();
        updatedRegion.deserializeNBT(compound);
        boolean flag = true;
        for (Region region : RegionHandler.regions.values()) {
            if (region.equals(updatedRegion)) {
                region.deserializeNBT(updatedRegion.serializeNBT());
                flag = false;
            }
        }
        if (flag) {
            RegionHandler.regions.put(new RegionChunks(mc.world, updatedRegion.getAABB()), updatedRegion);
        }
        RegionHandler.soundHandler.stop();
    }

}
