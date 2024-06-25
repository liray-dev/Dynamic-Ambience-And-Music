package daam.common.network.packets.client;

import daam.common.network.packets.SimpleNBTPacket;
import daam.common.tile.SoundBlockTileEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class UpdateSoundBlockPacket extends SimpleNBTPacket {

    public UpdateSoundBlockPacket() {
    }

    public UpdateSoundBlockPacket(SoundBlockTileEntity tileEntity) {
        this.compound = tileEntity.serializeNBT();
    }

    @Override
    public void server(EntityPlayerMP player) {
        SoundBlockTileEntity serialized = new SoundBlockTileEntity();
        serialized.deserializeNBT(compound);

        TileEntity tileEntity = player.world.getTileEntity(serialized.getPos());
        if (tileEntity instanceof SoundBlockTileEntity) {
            tileEntity.deserializeNBT(compound);
            ((SoundBlockTileEntity) tileEntity).syncToClient();
        }
    }
}
