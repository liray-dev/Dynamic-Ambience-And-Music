package daam.common.network.packets.client;

import daam.DAAM;
import daam.common.network.packets.SimplePacket;
import daam.common.network.packets.server.ResponseRegionFromChunkPacket;
import daam.common.world.DAAMWorldSavedData;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class RequestRegionFromChunkPacket extends SimplePacket {

    private int chunkX;
    private int chunkZ;

    public RequestRegionFromChunkPacket() {

    }

    public RequestRegionFromChunkPacket(Chunk chunk) {
        this.chunkX = chunk.x;
        this.chunkZ = chunk.z;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(chunkX);
        buf.writeInt(chunkZ);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        chunkX = buf.readInt();
        chunkZ = buf.readInt();
    }

    @Override
    public void server(EntityPlayerMP player) {
        Chunk chunk = player.world.getChunk(chunkX, chunkZ);
        DAAMWorldSavedData savedData = DAAMWorldSavedData.get(player.world);

        ConcurrentHashMap<RegionChunks, Region> regions = savedData.regions;

        HashMap<RegionChunks, Region> filtered = new HashMap<>();
        regions.entrySet().forEach((entry) -> {
            RegionChunks regionChunks = entry.getKey();
            boolean contains = regionChunks.equalsWithChunk(chunk);
            if (contains) {
                filtered.put(entry.getKey(), entry.getValue());
            }
        });

        if (filtered.isEmpty()) {
            return;
        }

        DAAM.NETWORK.client(new ResponseRegionFromChunkPacket(DAAMWorldSavedData.writeNBT(filtered)), player);
    }

}
