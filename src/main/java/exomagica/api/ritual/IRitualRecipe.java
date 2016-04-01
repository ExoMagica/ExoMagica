package exomagica.api.ritual;

import java.util.List;
import net.minecraft.item.ItemStack;

public interface IRitualRecipe<T extends IRitual> {

    /**
     * The item that is usually in the center of the ritual
     * @return The ItemStack. Can be null if there is no center or no item is needed in the center
     */
    ItemStack getCoreItem(T ritual);

    /**
     * Specify the items required for this recipe
     * @param ritual The ritual
     * @return A list with the required ItemStacks for this recipe
     */
    List<ItemStack> getRequiredItems(T ritual);

    /**
     * Specify the result item of this recipe
     * @param ritual The ritual
     * @return The result of this recipe
     */
    ItemStack getResult(T ritual);

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
