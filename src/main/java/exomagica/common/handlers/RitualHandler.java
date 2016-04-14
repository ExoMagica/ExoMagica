package exomagica.common.handlers;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.HashBiMap;
import exomagica.api.IWand;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.entities.EntityRitual;
import exomagica.common.utils.ItemUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RitualHandler {

    public final static HashBiMap<String, IRitual> NAMED_RITUALS = HashBiMap.create();
    public final static HashMap<IRitual, HashBiMap<String, IRitualRecipe>> RITUALS_RECIPES = new HashMap<IRitual, HashBiMap<String, IRitualRecipe>>();

    public static void registerRitual(IRitual ritual, String name) {
        if(Strings.isNullOrEmpty(name)) {
            throw new IllegalArgumentException("Attempted to register a ritual with no name: " + ritual);
        }
        if(ritual == null) {
            throw new NullPointerException("The ritual cannot be null");
        }

        ModContainer mod = Loader.instance().activeModContainer();
        if(mod == null) {
            name = "minecraft:" + name;
        } else {
            name = mod.getModId() + ":" + name;
        }
        HashBiMap<String, IRitualRecipe> recipes = HashBiMap.create();
        NAMED_RITUALS.put(name, ritual);
        RITUALS_RECIPES.put(ritual, recipes);
    }

    public static void registerRecipe(IRitual ritual, IRitualRecipe recipe) {
        if(recipe == null) {
            throw new NullPointerException("The recipe cannot be null");
        }
        if(ritual == null) {
            throw new NullPointerException("The ritual cannot be null");
        }
        if(!RITUALS_RECIPES.containsKey(ritual)) {
            throw new IllegalArgumentException("The ritual is not registered");
        }

        RITUALS_RECIPES.get(ritual).put(genRitualRecipeName(ritual, recipe), recipe);
    }

    public static void registerRecipe(String ritualName, IRitualRecipe recipe) {
        IRitual ritual = getRitual(ritualName);
        registerRecipe(ritual, recipe);
    }

    public static IRitual getRitual(String ritual) {
        if(Strings.isNullOrEmpty(ritual)) {
            throw new IllegalArgumentException("Attempted to find a ritual with no name: " + ritual);
        }

        if(ritual.indexOf(":") == -1) {
            ModContainer mod = Loader.instance().activeModContainer();
            if(mod == null) {
                ritual = "minecraft:" + ritual;
            } else {
                ritual = mod.getModId() + ":" + ritual;
            }
        }

        return NAMED_RITUALS.get(ritual);
    }

    @SubscribeEvent
    public void interact(RightClickBlock event) {
        if(event.getSide() != Side.SERVER) return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();

        IBlockState state = world.getBlockState(pos);
        if(state == null) return;
        if(!(state.getBlock() instanceof IRitualCore)) return;
        IRitualCore core = (IRitualCore)state.getBlock();
        pos = core.getRitualCorePosition(world, pos, state);
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
        event.setUseBlock(Result.DENY);

        List<EntityRitual> rituals = world.getEntitiesWithinAABB(EntityRitual.class, new AxisAlignedBB(pos));
        for(int i = 0; i < rituals.size(); i++) {
            RitualRecipeContainer c = rituals.get(i).getContainer();
            if(c != null && c.world == world && c.pos.equals(pos)) return; // There is already a ritual running here
        }

        IRitual ritual = findRitual(core, world, pos);
        if(ritual == null) return;

        Map<String, List<IInventory>> inventories = ritual.getInventories(core, world, pos);
        if(inventories.isEmpty()) return;
        HashBiMap<String, IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        IRitualRecipe recipe = findRitualRecipe(ritual, recipes.values(), inventories);
        if(recipe == null) return;

        int ticks = recipe.getDuration(ritual);
        if(ticks == -1) ticks = ritual.getDuration(recipe, core, world, pos);

        RitualRecipeContainer container = ritual.createContainer(recipe, core, world, pos, ticks, inventories, Side.SERVER);

        if(container != null) {
            world.spawnEntityInWorld(new EntityRitual(world, container));
        }
    }

    public static IRitualCore getCore(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(state == null) return null;
        if(!(state.getBlock() instanceof IRitualCore)) return null;
        IRitualCore core = (IRitualCore)state.getBlock();
        pos = core.getRitualCorePosition(world, pos, state);
        if(!core.isRitualCore(world, pos, state)) return null;
        return core;
    }

    public static IRitual findRitual(IBlockAccess world, BlockPos pos) {
        IRitualCore core = getCore(world, pos);
        if(core == null) return null;
        return findRitual(core, world, pos);
    }

    public static IRitual findRitual(IRitualCore core, IBlockAccess world, BlockPos pos) {
        for(IRitual r : RITUALS_RECIPES.keySet()) {
            if(r.checkPattern(core, world, pos)) {
                return r;
            }
        }
        return null;
    }

    public static IRitualRecipe findRitualRecipe(IRitual ritual, IBlockAccess world, BlockPos pos) {
        IRitualCore core = getCore(world, pos);
        if(core == null) return null;
        return findRitualRecipe(ritual, core, world, pos);
    }

    public static IRitualRecipe findRitualRecipe(IRitual ritual, IRitualCore core, IBlockAccess world, BlockPos pos) {
        Map<String, List<IInventory>> inventories = ritual.getInventories(core, world, pos);
        if(inventories.isEmpty()) return null;
        HashBiMap<String, IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        return findRitualRecipe(ritual, recipes.values(), inventories);
    }

    public static IRitualRecipe findRitualRecipe(IRitual ritual, Collection<IRitualRecipe> recipes, Map<String, List<IInventory>> inventories) {
        for(IRitualRecipe recipe : recipes) {

            if(checkRecipe(ritual, recipe, inventories)) {
                return recipe;
            }

        }
        return null;
    }

    public static String genRitualRecipeName(IRitual ritual, IRitualRecipe recipe) {
        String name = recipe.getIdentifier(ritual);
        if(name != null) return name;

        List<String> names = new ArrayList<String>();
        Map<String, List<Object>> requiredItems = recipe.getRequiredItems(ritual);
        for(String key : requiredItems.keySet()) {
            List<Object> items = requiredItems.get(key);
            if(items == null) continue;
            names.add(key + ":" + ItemUtils.toString(items));
        }
        Collections.sort(names);
        return Joiner.on(',').join(names);
    }

    public static String getRitualRecipeName(IRitual ritual, IRitualRecipe recipe) {
        HashBiMap<String, IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        if(recipes != null) return recipes.inverse().get(recipe);
        return genRitualRecipeName(ritual, recipe);
    }

    public static IRitualRecipe findRitualRecipe(IRitual ritual, String recipe) {
        HashBiMap<String, IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        if(recipes == null) return null;
        return recipes.get(recipe);
    }

    public static boolean checkRecipe(IRitual ritual, IRitualRecipe recipe, Map<String, List<IInventory>> inventories) {
        Map<String, List<Object>> requiredItems = recipe.getRequiredItems(ritual);

        for(String type : requiredItems.keySet()) {
            List<IInventory> invs = inventories.get(type);
            if(invs == null) invs = Collections.emptyList();
            List<Object> items = requiredItems.get(type);
            if(items == null) {
                items = Collections.emptyList();
            } else {
                items = new ArrayList<Object>(items);
            }
            if(invs.isEmpty() && !items.isEmpty()) return false;

            for(IInventory inv : invs) {
                for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
                    ItemStack slotStack = inv.getStackInSlot(slot);

                    Object match = null;
                    for(Object stack : items) {
                        if(ItemUtils.itemMatches(stack, slotStack)) {
                            match = stack;
                            break;
                        }
                    }

                    if(match != null) items.remove(match);

                }
            }

            if(!items.isEmpty()) return false;
        }

        return true;
    }

    public static void craft(RitualRecipeContainer container) {
        Map<String, List<ItemStack>> results = container.recipe.getResults(container.ritual);
        Map<String, List<IInventory>> inventories = container.inventories;

        for(String type : inventories.keySet()) {
            List<IInventory> invs = inventories.get(type);
            if(invs == null) invs = Collections.emptyList();
            List<ItemStack> resultItems = results.get(type);
            if(resultItems == null) {
                resultItems = Collections.emptyList();
            } else {
                resultItems = new ArrayList<ItemStack>(resultItems);
            }
            Iterator<ItemStack> stacks = resultItems.iterator();

            ItemStack stack = null;

            invsLoop: for(IInventory inv : invs) {
                inv.clear();
                for(int slot = 0; slot < inv.getSizeInventory(); slot++) {
                    if(!stacks.hasNext() && stack == null) continue invsLoop;
                    if(stack == null) stack = stacks.next().copy();
                    if(inv.isItemValidForSlot(slot, stack)) {
                        inv.setInventorySlotContents(slot, stack);
                        stacks.remove();
                    }
                }
            }

        }
    }

}
