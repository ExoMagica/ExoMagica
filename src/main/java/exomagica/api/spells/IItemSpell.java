package exomagica.api.spells;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Implement this on items
 */
public interface IItemSpell extends ISpell {

    void cast(EntityPlayer player, ItemStack stack);

}
