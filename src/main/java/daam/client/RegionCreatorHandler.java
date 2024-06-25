package daam.client;

import daam.DAAM;
import daam.client.screens.GuiRegionCreator;
import daam.common.items.RegionWand;
import daam.common.network.packets.client.CreateRegionPacket;
import daam.common.world.Region;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RegionCreatorHandler {

    @Setter
    public static BlockPos LEFT;

    @Setter
    public static BlockPos RIGHT;

    public static Region currentRegion;

    @SubscribeEvent
    public static void left(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player.getHeldItemMainhand().getItem() instanceof RegionWand) {
                RayTraceResult traceResult = mc.objectMouseOver;
                if (traceResult != null) {
                    setLEFT(traceResult.getBlockPos());
                    update();
                }
            }
        } else {
            if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof RegionWand) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void right(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player.getHeldItemMainhand().getItem() instanceof RegionWand) {
                if (!mc.player.isSneaking() && !(mc.currentScreen instanceof GuiRegionCreator)) {
                    RayTraceResult traceResult = mc.objectMouseOver;
                    if (traceResult != null) {
                        setRIGHT(traceResult.getBlockPos());
                        update();
                    }
                }
            }
        } else {
            if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof RegionWand) {
                event.setCanceled(true);
            }
        }
    }

    private static void update() {
        if (LEFT != null && RIGHT != null) {
            currentRegion = new Region();
            currentRegion.setAABB(new AxisAlignedBB(LEFT).union(new AxisAlignedBB(RIGHT)));
        }
    }

    @SubscribeEvent
    public static void render(RenderWorldLastEvent event) {
        if (RegionHandler.hidden) return;

        Vector3d offset = getCameraOffset(event.getPartialTicks());
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GL11.glLineWidth(3f);
        GlStateManager.translate(-offset.x, -offset.y, -offset.z);

        if (LEFT != null) {
            RenderGlobal.renderFilledBox(new AxisAlignedBB(LEFT).expand(-0.01, -0.01, -0.01).expand(0.01, 0.01, 0.01), 0.0f, 0.8f, 1.0f, 0.5f);
        }

        if (RIGHT != null) {
            RenderGlobal.renderFilledBox(new AxisAlignedBB(RIGHT).expand(-0.01, -0.01, -0.01).expand(0.01, 0.01, 0.01), 0.0f, 0.8f, 1.0f, 0.5f);
        }

        if (currentRegion != null) {
            GlStateManager.disableCull();
            DrawUtils.grid(currentRegion.getAABB(), 0.0f, 0.8f, 1.0f, 0.5f);
            GlStateManager.enableCull();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }


    private static Vector3d getCameraOffset(float partialTicks) {
        try {
            Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            double xCoord = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks);
            double yCoord = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks);
            double zCoord = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
            return new Vector3d(xCoord, yCoord, zCoord);
        } catch (Exception e) {
            return new Vector3d();
        }
    }

    public static void create() {
        DAAM.NETWORK.server(new CreateRegionPacket(currentRegion));
        reset();
    }

    public static void reset() {
        RegionCreatorHandler.currentRegion = null;
        RegionCreatorHandler.setLEFT(null);
        RegionCreatorHandler.setRIGHT(null);
    }

}
