package daam.common.world;

import lombok.Data;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.INBTSerializable;

@Data
public class Region implements INBTSerializable<NBTTagCompound> {

    private String UUID;

    private AxisAlignedBB AABB;

    private String MUSIC_PATH;
    private String AMBIENT_PATH;

    public Region() {
        this.setUUID(String.valueOf(java.util.UUID.randomUUID()));
        this.setAABB(new AxisAlignedBB(0, 0, 0, 0, 0, 0));
        this.setMUSIC_PATH("");
        this.setAMBIENT_PATH("");
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        {
            compound.setString("uuid", getUUID());
        }
        {
            AxisAlignedBB aabb = getAABB();
            compound.setDouble("miX", aabb.minX);
            compound.setDouble("miY", aabb.minY);
            compound.setDouble("miZ", aabb.minZ);
            compound.setDouble("maX", aabb.maxX);
            compound.setDouble("maY", aabb.maxY);
            compound.setDouble("maZ", aabb.maxZ);
        }
        {
            compound.setString("mPath", getMUSIC_PATH());
        }
        {
            compound.setString("aPath", getAMBIENT_PATH());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound compound) {
        {
            setUUID(compound.getString("uuid"));
        }
        {
            double minX = compound.getDouble("miX");
            double minY = compound.getDouble("miY");
            double minZ = compound.getDouble("miZ");
            double maxX = compound.getDouble("maX");
            double maxY = compound.getDouble("maY");
            double maxZ = compound.getDouble("maZ");
            setAABB(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
        }
        {
            setMUSIC_PATH(compound.getString("mPath"));
        }
        {
            setAMBIENT_PATH(compound.getString("aPath"));
        }
    }

    public boolean equals(Region another) {
        return this.getUUID().equals(another.getUUID());
    }

}
