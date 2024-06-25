package daam.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class SoundBlockTileEntity extends TileEntity implements ITickable {

    public String soundPath;
    public float volume;
    public int delay;

    private int currentTick;

    public SoundBlockTileEntity() {
        this.soundPath = "";
        this.volume = 1F;
        this.delay = 20;
        this.currentTick = 0;
    }

    @Override
    public void update() {
        if (soundPath.isEmpty()) return;

        currentTick++;

        World world = getWorld();

        if (world.isRemote) {
            if (currentTick >= delay) {
                currentTick = 0;
                world.playSound(getPos().getX(), getPos().getY(), getPos().getZ(), new SoundEvent(new ResourceLocation(soundPath)), SoundCategory.BLOCKS, volume, 1f, true);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setString("soundPath", soundPath);
        compound.setFloat("volume", volume);
        compound.setInteger("delay", delay);
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.soundPath = compound.getString("soundPath");
        this.volume = compound.getFloat("volume");
        this.delay = compound.getInteger("delay");
        super.readFromNBT(compound);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 3, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    public void syncToClient() {
        if (!world.isRemote) {
            if (world instanceof WorldServer) {
                WorldServer server = (WorldServer) world;
                PlayerChunkMapEntry entry = server.getPlayerChunkMap().getEntry(pos.getX() >> 4, pos.getZ() >> 4);
                if (entry != null) {
                    SPacketUpdateTileEntity packet = getUpdatePacket();
                    if (packet != null) {
                        entry.sendPacket(packet);
                    }
                }
            }
        }
        this.markDirty();
    }

}
