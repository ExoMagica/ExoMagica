package exomagica.common.items;

import exomagica.ExoContent;
import exomagica.api.spells.IItemSpell;
import exomagica.api.spells.SpellType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpellTest extends Item implements IItemSpell {

    public ItemSpellTest() {
        setCreativeTab(ExoContent.TAB);
    }

    @Override
    public void cast(EntityPlayer player, ItemStack stack) {
        player.addVelocity(0, 1, 0);
    }

    @Override
    public SpellType getType() {
        return SpellType.HOLD;
    }
}
