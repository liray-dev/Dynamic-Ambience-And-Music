package daam;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Parser {

    public static void main(String[] args) {
        CompoundTag oldData = null;
        try (FileInputStream fis = new FileInputStream(new File("C:\\Users\\david\\Desktop\\parser\\daam_data.dat"))) {
            NBTDeserializer deserializer = new NBTDeserializer();
            oldData = (CompoundTag) deserializer.fromStream(fis).getTag();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CompoundTag newData = new CompoundTag();
        CompoundTag newDataEntries = new CompoundTag();
        newData.put("data", newDataEntries);

        CompoundTag oldDataEntries = oldData.getCompoundTag("data");

        for (Map.Entry<String, Tag<?>> entry : oldDataEntries.entrySet()) {
            CompoundTag dataEntry = (CompoundTag) entry.getValue();
            String uuid = entry.getKey();
            double minX = dataEntry.getDouble("miX");
            double maxX = dataEntry.getDouble("maX");
            double minY = dataEntry.getDouble("miY");
            double maxY = dataEntry.getDouble("maY");
            double minZ = dataEntry.getDouble("miZ");
            double maxZ = dataEntry.getDouble("maZ");

            AxisAlignedBB aabb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);

            ArrayList<Chunk> chunks = getChunksFromAABB(aabb);

            StringBuilder chunkKey = new StringBuilder("[");
            for (Chunk chunk : chunks) {
                if (chunkKey.length() > 1) {
                    chunkKey.append(",");
                }
                chunkKey.append("\"").append(chunk.x).append(",").append(chunk.z).append("\"");
            }
            chunkKey.append("]");

            CompoundTag newDataEntry = new CompoundTag();
            newDataEntry.putDouble("maY", maxY);
            newDataEntry.putDouble("maX", maxX);
            newDataEntry.putDouble("maZ", maxZ);
            newDataEntry.putString("mPath", dataEntry.getString("mPath"));
            newDataEntry.putString("aPath", dataEntry.getString("aPath"));
            newDataEntry.putDouble("miY", minY);
            newDataEntry.putString("uuid", uuid);
            newDataEntry.putDouble("miX", minX);
            newDataEntry.putDouble("miZ", minZ);

            newDataEntries.put(chunkKey.toString(), newDataEntry);
        }

        try (FileOutputStream fos = new FileOutputStream(new File("C:\\Users\\david\\Desktop\\parser\\daam_data_new.dat"))) {
            NBTSerializer serializer = new NBTSerializer();
            serializer.toStream(new NamedTag("", newData), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Chunk> getChunksFromAABB(AxisAlignedBB aabb) {
        ArrayList<Chunk> chunks = new ArrayList<>();

        int minX = ((int) aabb.minX) >> 4;
        int maxX = ((int) aabb.maxX) >> 4;
        int minZ = ((int) aabb.minZ) >> 4;
        int maxZ = ((int) aabb.maxZ) >> 4;

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                chunks.add(new Chunk(x, z));
            }
        }

        return chunks;
    }

    static class Chunk {
        int x, z;

        Chunk(int x, int z) {
            this.x = x;
            this.z = z;
        }
    }

    static class AxisAlignedBB {
        double minX, minY, minZ;
        double maxX, maxY, maxZ;

        AxisAlignedBB(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
        }
    }

}
