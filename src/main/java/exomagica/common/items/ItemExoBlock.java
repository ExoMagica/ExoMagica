package exomagica.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemExoBlock extends ItemBlock {

    public ItemExoBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    public int getMetadata(int damage) {
        return damage;
    }

}
