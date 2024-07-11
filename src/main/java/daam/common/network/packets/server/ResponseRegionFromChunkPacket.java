package daam.common.network.packets.server;

import daam.client.RegionHandler;
import daam.common.network.packets.SimpleNBTPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class ResponseRegionFromChunkPacket extends SimpleNBTPacket {

    public ResponseRegionFromChunkPacket() {

    }

    public ResponseRegionFromChunkPacket(NBTTagCompound compound) {
        this.compound = compound;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void client(Minecraft mc, EntityPlayer player) {
        HashMap<RegionChunks, Region> map = DAAMWorldSavedData.readNBT(compound);
        for (Map.Entry<RegionChunks, Region> entry : map.entrySet()) {
            Region region = entry.getValue();

            boolean flag = true;

            for (Region value : RegionHandler.regions.values()) {
                if (value.equals(region)) {
                    flag = false;
                }
            }

            if (flag) {
                RegionHandler.regions.put(entry.getKey(), entry.getValue());
            }
        }
    }

}
