package exomagica.common.items;

import exomagica.ExoContent;
import exomagica.ExoMagica;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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

}
