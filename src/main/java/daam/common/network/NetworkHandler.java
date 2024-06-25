package daam.common.network;

import daam.DAAM;
import daam.common.network.packets.SimplePacket;
import daam.common.network.packets.client.*;
import daam.common.network.packets.server.ResponseRegionFromChunkPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(DAAM.MODID);
    private short id;

    public void registry() {
        register(CreateRegionPacket.class);
        register(UpdateRegionPacket.class);
        register(RequestRegionFromChunkPacket.class);
        register(ResponseRegionFromChunkPacket.class);
        register(SyncRegionPacket.class);
        register(RemoveRegionPacket.class);
        register(UpdateSoundBlockPacket.class);
    }

    public void client(SimplePacket packet, EntityPlayerMP player) {
        NETWORK.sendTo(packet, player);
    }

    public void server(SimplePacket packet) {
        NETWORK.sendToServer(packet);
    }

    public void all(SimplePacket packet) {
        NETWORK.sendToAll(packet);
    }

    private void register(Class<? extends SimplePacket> packet) {
        try {
            NETWORK.registerMessage(packet.newInstance(), packet, id++, Side.CLIENT);
            NETWORK.registerMessage(packet.newInstance(), packet, id++, Side.SERVER);
        } catch (InstantiationException | IllegalAccessException e) {
            DAAM.logger.error(e.getMessage());
        }
    }

}
