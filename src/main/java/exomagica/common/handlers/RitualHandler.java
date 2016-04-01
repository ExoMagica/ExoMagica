package exomagica.common.handlers;

import com.google.common.base.Strings;
import exomagica.ExoMagica;
import exomagica.api.IWand;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.packets.RitualPacket;
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
import net.minecraftforge.oredict.OreDictionary;

public class RitualHandler {

    private final static HashMap<String, IRitual> NAMED_RITUALS = new HashMap<String, IRitual>();
    private final static HashMap<IRitual, List<IRitualRecipe>> RITUALS_RECIPES = new HashMap<IRitual, List<IRitualRecipe>>();

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

    public RitualHandler() {
        registerRitual(new RitualBasic(), "basic");
        registerRecipe("basic", new RitualBasicRecipe(new ItemStack(Items.arrow), new ItemStack(Items.diamond),
                new ItemStack(Items.lava_bucket), new ItemStack(Items.flint_and_steel),
                new ItemStack(Blocks.torch), new ItemStack(Items.coal)));
    }

    @SubscribeEvent
    public void tick(ServerTickEvent event) {
        for(int i = 0; i < SERVER_ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer container = SERVER_ACTIVE_RITUALS.get(i);
            if(container.ticksLeft-- <= 0) {
                boolean canCraft = container.ritual.finishRitual(container, Side.SERVER);
                SERVER_ACTIVE_RITUALS.remove(container);
                if(canCraft) {
                    // TODO craft
                    //container.recipe.getResult(container.ritual);
                }
            } else {
                container.ritual.tickRitual(container, Side.SERVER);
            }
        }
    }

    @SubscribeEvent
    public void tick(ClientTickEvent event) {
        for(int i = 0; i < CLIENT_ACTIVE_RITUALS.size(); i++) {
            RitualRecipeContainer container = CLIENT_ACTIVE_RITUALS.get(i);
            if(container.ticksLeft-- <= 0) {
                container.ritual.finishRitual(container, Side.CLIENT);
                CLIENT_ACTIVE_RITUALS.remove(container);
            } else {
                container.ritual.tickRitual(container, Side.CLIENT);
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

        IRitual ritual = findRitual(core, world, pos);
        if(ritual == null) return;

        IRitualRecipe recipe = findRitualRecipe(ritual, core, world, pos);
        if(recipe == null) return;

        RitualRecipeContainer container = ritual.startRitual(recipe, core, world, pos, Side.SERVER);

        if(container != null) {
            SERVER_ACTIVE_RITUALS.add(container);
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
        List<ItemStack> items = ritual.getItems(core, world, pos);
        if(items.isEmpty()) return null;
        ItemStack coreItem = ritual.getCoreItem(core, world, pos);
        List<IRitualRecipe> recipes = RITUALS_RECIPES.get(ritual);
        return findRitualRecipe(ritual, recipes, coreItem, items);
    }

    private static IRitualRecipe findRitualRecipe(IRitual ritual, List<IRitualRecipe> recipes, ItemStack coreItem, List<ItemStack> items) {
        recipeLoop: for(IRitualRecipe recipe : recipes) {

            ItemStack recipeCore = recipe.getCoreItem(ritual);
            if(!OreDictionary.itemMatches(recipeCore, coreItem, false)) continue;

            List<ItemStack> recipeItems = recipe.getRequiredItems(ritual);
            if(recipeItems.size() > items.size()) continue;

            recipeItems = new ArrayList<ItemStack>(recipeItems);
            for(ItemStack stack : items) {

                ItemStack recipeItem = null;
                for(ItemStack ri : recipeItems) {
                    if(OreDictionary.itemMatches(ri, stack, false)) {
                        recipeItem = ri;
                    }
                }
                if(recipeItem == null) {
                    continue recipeLoop;
                } else {
                    recipeItems.remove(recipeItem);
                }

            }

            if(recipeItems.isEmpty()) return recipe;
        }

        return null;
    }

}
