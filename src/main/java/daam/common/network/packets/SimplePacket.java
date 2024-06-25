package daam.common.network.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SimplePacket implements IMessage, IMessageHandler<SimplePacket, SimplePacket> {

    @Override
    public SimplePacket onMessage(SimplePacket sp, MessageContext ctx) {
        if (ctx.side.isServer())
            sp.server(ctx.getServerHandler().player);
        else
            mc().addScheduledTask(() -> sp.client(mc(), clientPlayer()));
        return null;
    }

    public void client(Minecraft mc, EntityPlayer player) {
    }

    public void server(EntityPlayerMP player) {
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer clientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @SideOnly(Side.CLIENT)
    private Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

}
