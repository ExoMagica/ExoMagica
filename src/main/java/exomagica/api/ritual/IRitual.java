package exomagica.api.ritual;

import java.util.List;
import java.util.Map;
import net.minecraft.inventory.IInventory;
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
     * Returns the items of this ritual
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @return A map of types and inventories
     */
    Map<String, List<IInventory>> getInventories(IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Returns the duration of this ritual
     * @param recipe The recipe
     * @param core The core
     * @param world The world
     * @param pos The position
     * @return The number of ticks that this ritual will take
     */
    int getDuration(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos);

    /**
     * Fired when the ritual starts to happen
     * @param recipe The recipe
     * @param core The core of the ritual
     * @param world The world
     * @param pos The position
     * @param ticks The number of ticks
     * @param inventories The inventories of this ritual
     * @param side The side
     * @return A new RitualRecipeContainer.
     */
    RitualRecipeContainer createContainer(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos,
                                          int ticks, Map<String, List<IInventory>> inventories, Side side);

    /**
     * Fired when the ritual finishes
     * @param container The recipe container
     * @param side The side
     * @return Whether this ritual was successfully finished
     */
    boolean finishRitual(RitualRecipeContainer container, Side side);

    /**
     * Fired when the ritual is cancelled
     * @param container The recipe container
     * @param side The side
     */
    void cancelRitual(RitualRecipeContainer container, Side side);

    /**
     * Fired every tick while the ritual is running
     * @param container The recipe container
     * @param side The side
     */
    void tickRitual(RitualRecipeContainer container, Side side);

}
