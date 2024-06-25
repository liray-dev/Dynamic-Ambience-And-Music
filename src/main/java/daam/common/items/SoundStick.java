package daam.common.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import daam.DAAM;
import daam.common.blocks.SoundBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SoundStick extends Item {

    public SoundStick() {
        setRegistryName(DAAM.MODID, "sound_stick");
        setTranslationKey("sound_stick");
        setCreativeTab(DAAM.TAB);
        setMaxStackSize(1);
        setMaxDamage(-1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote && playerIn.isSneaking()) {
            SoundBlock.setHidden(!SoundBlock.isHidden());
            Minecraft.getMinecraft().renderGlobal.loadRenderers();
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "SHIFT + RIGHT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "HANDLE SOUND VISIBILITY");
        tooltip.add(ChatFormatting.BLACK + "" + ChatFormatting.BOLD + "--------------");
        tooltip.add(ChatFormatting.GREEN + "" + ChatFormatting.BOLD + "RIGHT CLICK");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "OPEN SOUND BLOCK EDITOR");
        tooltip.add(ChatFormatting.DARK_GREEN + "" + ChatFormatting.BOLD + "IF YOU INTERACT WITH THE BLOCK");
    }

}
