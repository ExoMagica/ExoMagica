package exomagica.common.rituals;

import exomagica.ExoContent;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.client.particles.ColorfulFX;
import exomagica.client.particles.ItemCubeFX;
import exomagica.common.blocks.BlockChalk;
import exomagica.common.blocks.BlockChalk.ChalkType;
import exomagica.common.tiles.TileAltar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// "BASIC"????? Yep, we are not creative enough for these names :D
// WHY DON'T YOU SUGGEST SOME?
// TODO check if we will keep this name
public class RitualBasic implements IRitual {

    @Override
    public boolean checkPattern(IRitualCore core, IBlockAccess world, BlockPos pos) {
        return core == ExoContent.ALTAR && checkPattern(ChalkType.REGULAR, world, pos);
    }

    @Override
    public Map<String, List<IInventory>> getInventories(IRitualCore core, IBlockAccess world, BlockPos pos) {
        HashMap<String, List<IInventory>> map = new HashMap<String, List<IInventory>>();
        List<IInventory> elements = new ArrayList<IInventory>();
        List<IInventory> coreInvs = new ArrayList<IInventory>();

        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == ExoContent.ALTAR) {
            coreInvs.add((TileAltar)world.getTileEntity(pos));
        }

        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos pos2 = pos.offset(facing, 3);
            state = world.getBlockState(pos2);
            if(state.getBlock() != ExoContent.ALTAR) continue;
            elements.add((TileAltar)world.getTileEntity(pos2));
        }

        map.put("core", coreInvs);
        map.put("element", elements);

        return map;
    }

    private boolean checkPattern(ChalkType type, IBlockAccess world, BlockPos pos) {
        IBlockState state;

        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            state = world.getBlockState(pos.offset(facing));
            if(!checkChalk(type, state)) return false;

            BlockPos pos2 = pos.offset(facing, 3);
            state = world.getBlockState(pos2);
            if(state.getBlock() != ExoContent.ALTAR) return false;

            EnumFacing opposite = facing.getOpposite();
            for(EnumFacing facing2 : EnumFacing.HORIZONTALS) {
                if(facing2 == facing || facing2 == opposite) continue;

                BlockPos pos3 = pos2.offset(facing2);
                state = world.getBlockState(pos3);
                if(!checkChalk(type, state)) return false;
                state = world.getBlockState(pos3.offset(opposite).offset(facing2));
                if(!checkChalk(type, state)) return false;
            }
        }
        return true;
    }

    private boolean checkChalk(ChalkType type, IBlockState state) {
        return state.getBlock() == ExoContent.CHALK && state.getValue(BlockChalk.TYPE) == type;
    }

    @Override
    public int getDuration(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos) {
        return 250;
    }

    @Override
    public RitualRecipeContainer createContainer(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos,
                                             int ticksLeft, Map<String, List<IInventory>> inventories, Side side) {
        return new RitualRecipeContainer(this, recipe, core, world, pos, inventories, ticksLeft);
    }

    @Override
    public boolean finishRitual(RitualRecipeContainer c, Side side) {
        boolean finish = c.recipe.finishRecipe(this);
        if(side == Side.CLIENT) {
            if(finish) {
                EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;

                for(int i = 0; i < 25; i++) {
                    ColorfulFX fx = new ColorfulFX((World)c.world, c.pos.getX() + 0.5, c.pos.getY() + 1.75, c.pos.getZ() + 0.5, true);
                    fx.randomizeSpeed();
                    fx.multiplyVelocity(0.1F);
                    fx.setMaxAge(80);
                    renderer.addEffect(fx);
                }

            }
        }
        return finish;
    }

    @Override
    public void tickRitual(RitualRecipeContainer c, Side side) {
        if(side == Side.CLIENT && c.ticksLeft > 15 || c.ticksLeft == 5) {
            EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;
            boolean isBig = c.ticksLeft == 5;
            for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                BlockPos pos = c.pos.offset(facing, 3);

                TileAltar altar = (TileAltar)c.world.getTileEntity(pos);
                ItemStack stack = altar.getStackInSlot(0);
                if(stack == null) continue;
                ItemCubeFX fx = new ItemCubeFX((World)c.world, pos.getX() + 0.5, pos.getY() + 1.75, pos.getZ() + 0.5, stack);
                fx.multipleParticleScaleBy((float)(0.5 * Math.random()) + (isBig ? 2.5F : 0.1F));
                fx.setAlphaEffect(false);
                fx.setScaleEffect(false);
                fx.enableFinalCoords(c.pos.getX() + 0.5F, c.pos.getY() + 1.75F, c.pos.getZ() + 0.5F, isBig ? 0.25F : 0.01F, 500);
                fx.setNoClip(true);
                renderer.addEffect(fx);
            }
        }
    }

    @Override
    public void cancelRitual(RitualRecipeContainer container, Side side) {

    }

    public static class RitualBasicRecipe implements IRitualRecipe<RitualBasic> {

        private final Map<String, List<Object>> requiredItems;
        private final Map<String, List<ItemStack>> results;

        public RitualBasicRecipe(ItemStack result, Object coreItem, List<Object> requiredItems) {
            this.requiredItems = new HashMap<String, List<Object>>();

            List<Object> coreItems = new ArrayList<Object>();
            coreItems.add(coreItem);

            this.requiredItems.put("core", coreItems);
            this.requiredItems.put("element", requiredItems);

            this.results = new HashMap<String, List<ItemStack>>();

            List<ItemStack> coreResults = new ArrayList<ItemStack>();
            coreResults.add(result);
            this.results.put("core", coreResults);
        }

        public RitualBasicRecipe(ItemStack result, Object coreItem, Object ... requiredItems) {
            this(result, coreItem, Arrays.asList(requiredItems));
        }

        @Override
        public String getIdentifier(RitualBasic ritual) {
            return null;
        }

        @Override
        public Map<String, List<Object>> getRequiredItems(RitualBasic ritual) {
            return requiredItems;
        }

        @Override
        public Map<String, List<ItemStack>> getResults(RitualBasic ritual) {
            return results;
        }

        @Override
        public int getDuration(RitualBasic ritual) {
            return -1;
        }

        @Override
        public boolean finishRecipe(RitualBasic ritual) {
            return true;
        }

        @Override
        public void cancelRecipe(RitualBasic ritual) {

        }

    }

}
