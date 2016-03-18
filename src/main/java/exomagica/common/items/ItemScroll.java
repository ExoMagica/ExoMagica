package exomagica.common.items;

import exomagica.ExoContent;
import exomagica.ExoMagica;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends Item {

    @SideOnly(Side.CLIENT)
    public final ModelResourceLocation MODEL = new ModelResourceLocation(ExoMagica.MODID + ":scroll", "inventory");

    public ItemScroll() {
        this.setRegistryName("scroll");
        this.setUnlocalizedName("scroll");
        this.setCreativeTab(ExoContent.TAB);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
        return MODEL;
    }
}
