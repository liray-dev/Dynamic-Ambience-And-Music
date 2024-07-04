package daam.common.world;

import lombok.Data;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.INBTSerializable;

@Data
public class Region implements INBTSerializable<NBTTagCompound> {

    private String UUID;

    private AxisAlignedBB AABB;

    private String MUSIC_PATH_DAY;
    private String AMBIENT_PATH_DAY;

    private String MUSIC_PATH_NIGHT;
    private String AMBIENT_PATH_NIGHT;

    private boolean TIME_FACTOR;

    public Region() {
        this.setUUID(String.valueOf(java.util.UUID.randomUUID()));
        this.setAABB(new AxisAlignedBB(0, 0, 0, 0, 0, 0));

        this.setMUSIC_PATH_DAY("");
        this.setMUSIC_PATH_NIGHT("");

        this.setAMBIENT_PATH_DAY("");
        this.setAMBIENT_PATH_NIGHT("");

        this.setTIME_FACTOR(true);
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
            compound.setString("mPath", getMUSIC_PATH_DAY());
            compound.setString("mPath2", getMUSIC_PATH_NIGHT());
        }
        {
            compound.setString("aPath", getAMBIENT_PATH_DAY());
            compound.setString("aPath2", getAMBIENT_PATH_NIGHT());
        }
        {
            compound.setBoolean("timeF", isTIME_FACTOR());
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
            setMUSIC_PATH_DAY(compound.getString("mPath"));
            setMUSIC_PATH_NIGHT(compound.getString("mPath2"));
        }
        {
            setAMBIENT_PATH_DAY(compound.getString("aPath"));
            setAMBIENT_PATH_NIGHT(compound.getString("aPath2"));
        }
        {
            setTIME_FACTOR(compound.getBoolean("timeF"));
        }
    }

    public boolean equals(Region another) {
        return this.getUUID().equals(another.getUUID());
    }

}
