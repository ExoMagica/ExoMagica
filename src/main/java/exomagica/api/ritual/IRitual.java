package exomagica.api.ritual;

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
     * Fired when the ritual start to happen
     * @param recipe The recipe
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return The default number of ticks that this ritual will take.
     */
    int startRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Fired when the ritual finishes
     * @param recipe The recipe
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return Whether this ritual was successfully finished
     */
    boolean finishRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos);

}
