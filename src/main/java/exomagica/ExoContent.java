package exomagica;

import exomagica.common.blocks.BlockAltar;
import exomagica.common.blocks.BlockChalk;
import exomagica.common.items.ItemScroll;
import exomagica.common.items.ItemSpellTest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ExoContent {

    public static final CreativeTabs TAB = new CreativeTabs(ExoMagica.MODID) {
        @Override
        public Item getTabIconItem() {
            return SCROLL;
        }
    };

    public static final ItemSpellTest TEST = new ItemSpellTest();

    public static final ItemScroll SCROLL = new ItemScroll();

    public static final BlockChalk CHALK = new BlockChalk();
    public static final BlockAltar ALTAR = new BlockAltar();

}
