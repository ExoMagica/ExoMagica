package exomagica.common.items;

import exomagica.ExoContent;
import net.minecraft.item.Item;

public class ItemScroll extends Item {

    public ItemScroll() {
        this.setRegistryName("scroll");
        this.setUnlocalizedName("scroll");
        this.setCreativeTab(ExoContent.TAB);
    }

}
