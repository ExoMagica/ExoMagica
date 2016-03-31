package exomagica.api.ritual;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRitual {

    /**
     * Checks if this core has a valid pattern
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return Whether this is a valid ritual
     */
    boolean checkPattern(IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Returns the items of this ritual
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return A list with the items
     */
    List<ItemStack> getItems(IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Fired when the ritual starts to happen
     * @param recipe The recipe
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return A new RitualRecipeContainer.
     */
    RitualRecipeContainer startRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Fired when the ritual finishes
     * @param container The recipe container
     * @return Whether this ritual was successfully finished
     */
    boolean finishRitual(RitualRecipeContainer container);

    /**
     * Fired every tick while the ritual is running
     * @param container The recipe container
     */
    void tickRitual(RitualRecipeContainer container);

}
