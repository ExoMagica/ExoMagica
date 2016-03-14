package exomagica.api.spells;

import net.minecraft.entity.player.EntityPlayer;

/**
 * @author Guilherme Chaguri
 */
public interface IHandSpell extends ISpell {

    void cast(EntityPlayer player);

}
