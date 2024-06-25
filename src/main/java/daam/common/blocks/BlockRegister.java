package daam.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SuppressWarnings("DataFlowIssue")
public class BlockRegister {

    public static ArrayList<Block> blocks = new ArrayList<>();

    static {
        for (int i = 0; i < 16; i++) {
            blocks.add(new LightBlock(i));
        }
        blocks.add(new SoundBlock());
    }

    public static void register() {
        for (Block block : blocks) {
            apply(block);
        }

    }

    private static void apply(Block block) {
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender() {
        for (Block block : blocks) {
            applyRender(block);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void applyRender(Block block) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
