package exomagica;

import exomagica.common.items.ItemSpellTest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ExoContent {

    public static final CreativeTabs TAB = new CreativeTabs(ExoMagica.MODID) {
        @Override
        public Item getTabIconItem() {
            return TEST;
        }
    };

    public static final ItemSpellTest TEST = new ItemSpellTest();

}
