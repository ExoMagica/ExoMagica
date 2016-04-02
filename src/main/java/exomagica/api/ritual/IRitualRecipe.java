package exomagica.api.ritual;

import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;

public interface IRitualRecipe<T extends IRitual> {

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
     * Fired when the recipe start to happen
     * @param ritual The ritual
     * @return The number of ticks that this recipe will take. Return -1 to use the default value for this ritual
     */
    int startRecipe(T ritual);

    /**
     * Fired when the recipe finishes
     * @param ritual The ritual
     * @return Whether this recipe was successfully finished
     */
    boolean finishRecipe(T ritual);

}
