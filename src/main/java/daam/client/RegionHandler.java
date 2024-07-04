package daam.client;

import daam.DAAM;
import daam.common.network.packets.client.RequestRegionFromChunkPacket;
import daam.common.world.Region;
import daam.common.world.RegionChunks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(Side.CLIENT)
public class RegionHandler {

    public static ArrayList<String> loadedSounds = new ArrayList<>();

    public static boolean hidden = true;

    public static ConcurrentHashMap<RegionChunks, Region> regions = new ConcurrentHashMap<>();

    public static Region currentRegion;

    public static RegionSoundHandler soundHandler = new RegionSoundHandler();

    public static void loadSounds() {
        loadedSounds.clear();
        File soundsDir = DAAM.SOUNDS_DIR;
        for (File file : soundsDir.listFiles()) {
            loadedSounds.add(file.getName().replace(".ogg", ""));
        }
    }

    @SubscribeEvent
    public static void tick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        EntityPlayerSP player = mc.player;
        AxisAlignedBB playerAABB = player.getEntityBoundingBox();

        boolean flag = false;
        ArrayList<Region> intersectRegions = new ArrayList<>();
        for (Region region : new ArrayList<>(regions.values())) {
            AxisAlignedBB regionAABB = region.getAABB();
            if (inside(playerAABB, regionAABB)) {
                flag = true;
                intersectRegions.add(region);
            }
        }

        if (flag) {
            Region smallestRegion = null;
            double smallestVolume = Double.MAX_VALUE;

            for (Region region : intersectRegions) {
                double volume = getVolume(region.getAABB());
                if (volume < smallestVolume) {
                    smallestVolume = volume;
                    smallestRegion = region;
                }
            }

            currentRegion = smallestRegion;
            soundHandler.tick(currentRegion);
        } else {
            currentRegion = null;
            soundHandler.stopAll();
        }
    }

    @SubscribeEvent
    public static void render(RenderWorldLastEvent event) {
        if (hidden) return;

        Vector3d offset = getCameraOffset(event.getPartialTicks());
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GL11.glLineWidth(3f);
        GlStateManager.translate(-offset.x, -offset.y, -offset.z);

        for (Region region : new ArrayList<>(regions.values())) {
            GlStateManager.pushMatrix();
            DrawUtils.grid(region.getAABB(), 0.5f, 0.0f, 1.0f, 0.5f);
            GlStateManager.popMatrix();
        }

        if (currentRegion != null) {
            GlStateManager.disableCull();
            DrawUtils.grid(currentRegion.getAABB(), 0.0f, 0.5f, 0.0f, 1f);
            GlStateManager.enableCull();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void onWorldLoadEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.player.world.isRemote) {
            regions.clear();
        }
    }

    @SubscribeEvent
    public static void onWorldLoadEvent(ChunkEvent.Load event) {
        if (event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
            DAAM.NETWORK.server(new RequestRegionFromChunkPacket(event.getChunk()));
        }
    }

    @SubscribeEvent
    public static void onWorldLoadEvent(ChunkEvent.Unload event) {
        if (event.getWorld().isRemote && event.getWorld().provider.getDimension() == 0) {
            clean();
        }
    }

    public static void clean() {
        for (RegionChunks regionChunks : regions.keySet()) {
            boolean flag = regionChunks.allChunksUnload(Minecraft.getMinecraft().world);
            if (flag) {
                regions.remove(regionChunks);
            }
        }
    }

    private static double getVolume(AxisAlignedBB aabb) {
        double length = aabb.maxX - aabb.minX;
        double width = aabb.maxZ - aabb.minZ;
        double height = aabb.maxY - aabb.minY;
        return length * width * height;
    }

    public static boolean inside(AxisAlignedBB playerAABB, AxisAlignedBB regionAABB) {
        return (regionAABB.minX <= playerAABB.minX && regionAABB.maxX >= playerAABB.maxX &&
                regionAABB.minY <= playerAABB.minY && regionAABB.maxY >= playerAABB.maxY &&
                regionAABB.minZ <= playerAABB.minZ && regionAABB.maxZ >= playerAABB.maxZ) || playerAABB.intersects(regionAABB);
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

}
