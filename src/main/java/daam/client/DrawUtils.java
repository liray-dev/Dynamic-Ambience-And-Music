package daam.client;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class DrawUtils {

    public static void grid(AxisAlignedBB axisAlignedBB, float red, float green, float blue, float alpha) {
        drawBoundingBox(axisAlignedBB.minX - 0.01, axisAlignedBB.minY - 0.01, axisAlignedBB.minZ - 0.01, axisAlignedBB.maxX - 0.01, axisAlignedBB.maxY - 0.01, axisAlignedBB.maxZ - 0.01, red, green, blue, alpha);
    }

    public static void drawBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2, float red, float green, float blue, float alpha) {
        Tessellator instance = Tessellator.getInstance();
        BufferBuilder buffer = instance.getBuffer();

        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        instance.draw();


        buffer.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        instance.draw();


        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();

        buffer.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
        buffer.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
        instance.draw();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        double x, y, z;
        double offsetSize = 1.0;


        z = z2;
        y = y1;
        int msize = 257;
        if ((y2 - y / offsetSize) < msize) {
            for (double yoff = 0; yoff + y <= y2; yoff += offsetSize) {
                buffer.pos(x1, y + yoff, z).color(red, green, blue, alpha).endVertex();
                buffer.pos(x2, y + yoff, z).color(red, green, blue, alpha).endVertex();
            }
        }


        z = z1;
        if ((y2 - y / offsetSize) < msize) {
            for (double yoff = 0; yoff + y <= y2; yoff += offsetSize) {
                buffer.pos(x1, y + yoff, z).color(red, green, blue, alpha).endVertex();
                buffer.pos(x2, y + yoff, z).color(red, green, blue, alpha).endVertex();
            }
        }


        x = x1;
        if ((y2 - y / offsetSize) < msize) {
            for (double yoff = 0; yoff + y <= y2; yoff += offsetSize) {
                buffer.pos(x, y + yoff, z1).color(red, green, blue, alpha).endVertex();
                buffer.pos(x, y + yoff, z2).color(red, green, blue, alpha).endVertex();
            }
        }

        x = x2;
        if ((y2 - y / offsetSize) < msize) {
            for (double yoff = 0; yoff + y <= y2; yoff += offsetSize) {
                buffer.pos(x, y + yoff, z1).color(red, green, blue, alpha).endVertex();
                buffer.pos(x, y + yoff, z2).color(red, green, blue, alpha).endVertex();
            }
        }


        x = x1;
        z = z1;
        if ((x2 - x / offsetSize) < msize) {
            for (double xoff = 0; xoff + x <= x2; xoff += offsetSize) {
                buffer.pos(x + xoff, y1, z).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + xoff, y2, z).color(red, green, blue, alpha).endVertex();
            }
        }

        z = z2;
        if ((x2 - x / offsetSize) < msize) {
            for (double xoff = 0; xoff + x <= x2; xoff += offsetSize) {
                buffer.pos(x + xoff, y1, z).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + xoff, y2, z).color(red, green, blue, alpha).endVertex();
            }
        }

        y = y2;
        if ((x2 - x / offsetSize) < msize) {
            for (double xoff = 0; xoff + x <= x2; xoff += offsetSize) {
                buffer.pos(x + xoff, y, z1).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + xoff, y, z2).color(red, green, blue, alpha).endVertex();
            }
        }

        y = y1;
        if ((x2 - x / offsetSize) < msize) {
            for (double xoff = 0; xoff + x <= x2; xoff += offsetSize) {
                buffer.pos(x + xoff, y, z1).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + xoff, y, z2).color(red, green, blue, alpha).endVertex();
            }
        }


        z = z1;
        y = y1;
        if ((z2 - z / offsetSize) < msize) {
            for (double zoff = 0; zoff + z <= z2; zoff += offsetSize) {
                buffer.pos(x1, y, z + zoff).color(red, green, blue, alpha).endVertex();
                buffer.pos(x2, y, z + zoff).color(red, green, blue, alpha).endVertex();
            }
        }

        y = y2;
        if ((z2 - z / offsetSize) < msize) {
            for (double zoff = 0; zoff + z <= z2; zoff += offsetSize) {
                buffer.pos(x1, y, z + zoff).color(red, green, blue, alpha).endVertex();
                buffer.pos(x2, y, z + zoff).color(red, green, blue, alpha).endVertex();
            }
        }

        x = x2;
        if ((z2 - z / offsetSize) < msize) {
            for (double zoff = 0; zoff + z <= z2; zoff += offsetSize) {
                buffer.pos(x, y1, z + zoff).color(red, green, blue, alpha).endVertex();
                buffer.pos(x, y2, z + zoff).color(red, green, blue, alpha).endVertex();
            }
        }

        x = x1;
        if ((z2 - z / offsetSize) < msize) {
            for (double zoff = 0; zoff + z <= z2; zoff += offsetSize) {
                buffer.pos(x, y1, z + zoff).color(red, green, blue, alpha).endVertex();
                buffer.pos(x, y2, z + zoff).color(red, green, blue, alpha).endVertex();
            }
        }

        instance.draw();

    }

}
