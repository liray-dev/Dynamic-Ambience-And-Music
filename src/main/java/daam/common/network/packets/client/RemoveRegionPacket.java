package daam.common.network.packets.client;

import daam.DAAM;
import daam.client.RegionHandler;
import daam.common.network.packets.SimpleNBTPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class RemoveRegionPacket extends SimpleNBTPacket {

    public RemoveRegionPacket() {
    }

    public RemoveRegionPacket(Region region) {
        this.compound = region.serializeNBT();
    }

    @Override
    public void client(Minecraft mc, EntityPlayer player) {
        Region updatedRegion = new Region();
        updatedRegion.deserializeNBT(compound);
        RegionHandler.regions.entrySet().removeIf(entry -> entry.getValue().equals(updatedRegion));
    }

    @Override
    public void server(EntityPlayerMP player) {
        Region updatedRegion = new Region();
        updatedRegion.deserializeNBT(compound);

        DAAMWorldSavedData savedData = DAAMWorldSavedData.get(player.world);
        savedData.regions.entrySet().removeIf(entry -> entry.getValue().equals(updatedRegion));

        DAAM.NETWORK.all(new RemoveRegionPacket(updatedRegion));
    }
}
