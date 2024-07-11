package daam.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.client.DrawUtils;
import daam.client.screens.GuiVolumeEditor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class VolumeEditor extends Item {

    public VolumeEditor() {
        setRegistryName(DAAM.MODID, "volume_editor");
        setTranslationKey("volume_editor");
        setCreativeTab(DAAM.TAB);
        setMaxStackSize(1);
        setMaxDamage(-1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote) {
            DrawUtils.open(new GuiVolumeEditor());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "RIGHT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "OPEN VOLUME EDITOR");
    }
}
