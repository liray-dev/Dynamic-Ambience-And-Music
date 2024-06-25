package daam.common.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public class RegionChunks {

    private static final String PATTERN = "%s,%s";
    private static final Gson GSON = new GsonBuilder().create();

    public String UUID;
    public ArrayList<String> chunks = new ArrayList<>();

    public RegionChunks(String uuid) {
        this.UUID = uuid;
    }

    public RegionChunks(String uuid, World world, AxisAlignedBB aabb) {
        this.UUID = uuid;
        ArrayList<Chunk> chunks = getChunksFromAABB(world, aabb);
        chunks.forEach(this::addChunk);
    }

    public void addChunk(Chunk chunk) {
        chunks.add(String.format(PATTERN, chunk.x, chunk.z));
    }

    public boolean equalsWithChunk(Chunk chunk) {
        String formatted = String.format(PATTERN, chunk.x, chunk.z);
        for (String string : chunks) {
            if (formatted.equals(string)) {
                return true;
            }
        }
        return false;
    }

    public void fromString(String chunks) {
        RegionChunks json = GSON.fromJson(chunks, RegionChunks.class);
        this.UUID = json.UUID;
        this.chunks = json.chunks;
    }

    public String toString() {
        return GSON.toJson(this);
    }

    public ArrayList<Chunk> getChunksFromAABB(World world, AxisAlignedBB aabb) {
        ArrayList<Chunk> chunks = new ArrayList<>();

        int minX = ((int) aabb.minX) >> 4;
        int maxX = ((int) aabb.maxX) >> 4;
        int minZ = ((int) aabb.minZ) >> 4;
        int maxZ = ((int) aabb.maxZ) >> 4;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(world.getChunk(x, z));
            }
        }

        return chunks;
    }

    public boolean allChunksUnload(World world) {
        for (String chunk : chunks) {
            String[] xz = chunk.split(",");
            int x = Integer.parseInt(xz[0]);
            int z = Integer.parseInt(xz[1]);
            boolean loaded = world.getChunkProvider().isChunkGeneratedAt(x, z);
            if (loaded) {
                return false;
            }
        }
        return true;
    }

}
