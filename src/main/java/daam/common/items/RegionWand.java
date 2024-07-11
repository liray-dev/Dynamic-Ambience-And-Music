package daam.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.client.DrawUtils;
import daam.client.screens.GuiRegionCreator;
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

public class RegionWand extends Item {

    public RegionWand() {
        setRegistryName(DAAM.MODID, "region_wand");
        setTranslationKey("region_wand");
        setCreativeTab(DAAM.TAB);
        setMaxStackSize(1);
        setMaxDamage(-1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote && playerIn.isSneaking()) {
            DrawUtils.open(new GuiRegionCreator());
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "SHIFT + RIGHT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "OPEN REGION CREATOR");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "IF YOU HAVE SELECTED TWO POINTS");
        tooltip.add(ChatFormatting.BLACK + "" + ChatFormatting.BOLD + "--------------");
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "LEFT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "CREATES A LEFT POINT");
        tooltip.add(ChatFormatting.BLACK + "" + ChatFormatting.BOLD + "--------------");
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "RIGHT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "CREATES A RIGHT POINT");
    }

}
