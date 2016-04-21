package exomagica.api.ritual;

import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;

public interface IRitualRecipe<T extends IRitual> {

    /**
     * Returns the unique identifier of this recipe for this ritual.
     * It needs to be the same in the client and the server for synchronization
     * @param ritual The ritual
     * @return The identifier or NULL to use the generated identifier.
     */
    String getIdentifier(T ritual);

    /**
     * Specify the items required for this recipe. The items must be a ItemStack, Item, String, Fluid or FluidStack
     * @param ritual The ritual
     * @return A map with the required items for each inventory type
     */
    Map<String, List<Object>> getRequiredItems(T ritual);

    /**
     * Specify the result items of this recipe.
     * @param ritual The ritual
     * @return The result for each inventory type
     */
    Map<String, List<ItemStack>> getResults(T ritual);

    /**
     * Returns the duration of this recipe
     * @param ritual The ritual
     * @return The number of ticks. Return -1 to use the default value for this ritual
     */
    int getDuration(T ritual);

    /**
     * Fired when the recipe finishes
     * @param ritual The ritual
     * @return Whether this recipe was successfully finished
     */
    boolean finishRecipe(T ritual);

    /**
     * Fired when the recipe is cancelled
     * @param ritual The ritual
     */
    void cancelRecipe(T ritual);

}
