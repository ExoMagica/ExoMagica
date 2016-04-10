package exomagica.common.handlers;

import com.google.common.base.Strings;
import exomagica.ExoMagica;
import exomagica.api.IWand;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.packets.RitualPacket;
import exomagica.common.utils.ItemUtils;
import java.util.ArrayList;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RitualHandler {

    public final static HashMap<String, IRitual> NAMED_RITUALS = new HashMap<String, IRitual>();
    public final static HashMap<IRitual, List<IRitualRecipe>> RITUALS_RECIPES = new HashMap<IRitual, List<IRitualRecipe>>();

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
        NAMED_RITUALS.put(name, ritual);
        RITUALS_RECIPES.put(ritual, new ArrayList<IRitualRecipe>());
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

        RITUALS_RECIPES.get(ritual).add(recipe);
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

    public final static List<RitualRecipeContainer> SERVER_ACTIVE_RITUALS = new ArrayList<RitualRecipeContainer>();

    @SideOnly(Side.CLIENT)
    public final static List<RitualRecipeContainer> CLIENT_ACTIVE_RITUALS = new ArrayList<RitualRecipeContainer>();

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        for(int i = 0; i < SERVER_ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer c = SERVER_ACTIVE_RITUALS.get(i);
            if(c.ticksLeft-- <= 0) {
                SERVER_ACTIVE_RITUALS.remove(c);
                if(checkRecipe(c.ritual, c.recipe, c.inventories) &&
                        c.ritual.checkPattern(c.core, c.world, c.pos)) {
                    boolean canCraft = c.ritual.finishRitual(c, Side.SERVER);
                    if(canCraft) craft(c);
                }
            } else {
                if(c.ticksLeft % 20 == 0 && !checkRecipe(c.ritual, c.recipe, c.inventories)) {
                    SERVER_ACTIVE_RITUALS.remove(c);
                    continue;
                }
                c.ritual.tickRitual(c, Side.SERVER);
            }
        }
    }

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
        for(int i = 0; i < CLIENT_ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer c = CLIENT_ACTIVE_RITUALS.get(i);
            if(c.ticksLeft-- <= 0) {
                CLIENT_ACTIVE_RITUALS.remove(c);
                if(checkRecipe(c.ritual, c.recipe, c.inventories) &&
                        c.ritual.checkPattern(c.core, c.world, c.pos)) {
                    boolean canCraft = c.ritual.finishRitual(c, Side.CLIENT);
                    if(canCraft) craft(c); // Executed in client-side for instant update
                }
            } else {
                if(c.ticksLeft % 20 == 0 && !checkRecipe(c.ritual, c.recipe, c.inventories)) {
                    CLIENT_ACTIVE_RITUALS.remove(c);
                    continue;
                }
                c.ritual.tickRitual(c, Side.CLIENT);
            }
        }
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

        for(int i = 0; i < SERVER_ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer c = SERVER_ACTIVE_RITUALS.get(i);
            if(c != null && c.world == world && c.pos.equals(pos)) return; // There is already a ritual running here
        }

        IRitual ritual = findRitual(core, world, pos);
        if(ritual == null) return;

        Map<String, List<IInventory>> inventories = ritual.getInventories(core, world, pos);
        if(inventories.isEmpty()) return;
        List<IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        IRitualRecipe recipe = findRitualRecipe(ritual, recipes, inventories);
        if(recipe == null) return;

        int ticks = recipe.getDuration(ritual);
        if(ticks == -1) ticks = ritual.getDuration(recipe, core, world, pos);

        RitualRecipeContainer container = ritual.createContainer(recipe, core, world, pos, ticks, inventories, Side.SERVER);

        if(container != null) {
            SERVER_ACTIVE_RITUALS.add(container);
            // TODO better packet system
            ExoMagica.NETWORK.sendToAllAround(new RitualPacket(pos), new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64));
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
        List<IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        return findRitualRecipe(ritual, recipes, inventories);
    }

    public static IRitualRecipe findRitualRecipe(IRitual ritual, List<IRitualRecipe> recipes, Map<String, List<IInventory>> inventories) {
        for(IRitualRecipe recipe : recipes) {

            if(checkRecipe(ritual, recipe, inventories)) {
                return recipe;
            }

        }
        return null;
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
