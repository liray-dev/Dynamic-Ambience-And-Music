package daam.common.network.packets.client;

import daam.DAAM;
import daam.common.network.packets.SimpleNBTPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import net.minecraft.entity.player.EntityPlayerMP;

public class UpdateRegionPacket extends SimpleNBTPacket {

    public UpdateRegionPacket() {
    }

    public UpdateRegionPacket(Region region) {
        this.compound = region.serializeNBT();
    }

    @Override
    public void server(EntityPlayerMP player) {
        DAAMWorldSavedData savedData = DAAMWorldSavedData.get(player.world);
        Region updatedRegion = new Region();
        updatedRegion.deserializeNBT(compound);

        for (Region region : savedData.regions.values()) {
            if (region.equals(updatedRegion)) {
                region.deserializeNBT(updatedRegion.serializeNBT());
            }
        }
        savedData.markDirty();

        DAAM.NETWORK.all(new SyncRegionPacket(updatedRegion));
    }
}
