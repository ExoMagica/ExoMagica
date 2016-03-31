package exomagica.common.handlers;

import exomagica.api.IWand;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.rituals.RitualBasic;
import exomagica.common.rituals.RitualBasic.RitualBasicRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class RitualHandler {

    public final HashMap<IRitual, List<IRitualRecipe>> RITUALS = new HashMap<IRitual, List<IRitualRecipe>>();

    public final List<RitualRecipeContainer> ACTIVE_RITUALS = new ArrayList<RitualRecipeContainer>();

    public RitualHandler() {
        List<IRitualRecipe> recipes = new ArrayList<IRitualRecipe>();
        recipes.add(new RitualBasicRecipe(new ItemStack(Items.arrow), new ItemStack(Items.diamond),
                    new ItemStack(Items.lava_bucket), new ItemStack(Items.flint_and_steel),
                    new ItemStack(Blocks.torch), new ItemStack(Items.coal)));
        RITUALS.put(new RitualBasic(), recipes);
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        for(int i = 0; i < ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer container = ACTIVE_RITUALS.get(i);
            if(container.ticksLeft-- <= 0) {
                container.ritual.finishRitual(container);
                ACTIVE_RITUALS.remove(container);
            } else {
                container.ritual.tickRitual(container);
            }
        }
    }

    @SubscribeEvent
    public void interact(RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        if(state == null) return;
        if(!(state.getBlock() instanceof IRitualCore)) return;
        IRitualCore core = (IRitualCore)state.getBlock();
        if(!core.isRitualCore(world, pos, state)) return;

        ItemStack stack = event.getItemStack();
        if(stack == null) return;
        Item item = stack.getItem();
        if(item == Items.stick) {

        } else if(item instanceof IWand) {

        } else {
            return;
        }

        event.setCanceled(true);
        System.out.println("USING WAND");

        IRitual ritual = null;
        IRitualRecipe recipe = null;
        for(IRitual r : RITUALS.keySet()) {
            if(r.checkPattern(core, world, pos)) {

                System.out.println("RITUAL FOUND");

                List<ItemStack> items = r.getItems(core, world, pos);
                if(items.isEmpty()) continue;
                List<IRitualRecipe> recipes = RITUALS.get(r);
                recipe = findRecipe(r, recipes, items);
                ritual = r;
                if(recipe != null) {
                    System.out.println("RECIPE FOUND");
                    break;
                }

            }
        }

        if(recipe == null) return;

        System.out.println("STARTING...");

        RitualRecipeContainer container = ritual.startRitual(recipe, core, world, pos);

        if(container != null) ACTIVE_RITUALS.add(container);
    }

    private IRitualRecipe findRecipe(IRitual ritual, List<IRitualRecipe> recipes, List<ItemStack> items) {
        recipeLoop: for(IRitualRecipe recipe : recipes) {
            List<ItemStack> recipeItems = recipe.getRequiredItems(ritual);
            if(recipeItems.size() > items.size()) continue;

            recipeItems = new ArrayList<ItemStack>(recipeItems);
            for(ItemStack stack : items) {

                boolean b = false;
                for(ItemStack ri : recipeItems) {
                    if(stack.isItemEqual(ri)) b = true;
                }
                if(!b) continue recipeLoop;
                recipeItems.remove(stack);

            }

            System.out.println("RECIPE: " + recipeItems.size());
            if(recipeItems.isEmpty()) return recipe;
        }

        return null;
    }

}
