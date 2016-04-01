package exomagica.api.ritual;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

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
     * The item that is usually in the center of the ritual
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return The ItemStack. Can be null if there is no center
     */
    ItemStack getCoreItem(IRitualCore core, IBlockAccess world, BlockPos pos);

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
     * @param side The side
     * @return A new RitualRecipeContainer.
     */
    RitualRecipeContainer startRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos, Side side);

    /**
     * Fired when the ritual finishes
     * @param container The recipe container
     * @param side The side
     * @return Whether this ritual was successfully finished
     */
    boolean finishRitual(RitualRecipeContainer container, Side side);

    /**
     * Fired every tick while the ritual is running
     * @param container The recipe container
     * @param side The side
     */
    void tickRitual(RitualRecipeContainer container, Side side);

}
