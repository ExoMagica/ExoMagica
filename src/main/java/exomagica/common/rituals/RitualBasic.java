package exomagica.common.rituals;

import exomagica.ExoContent;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.client.particles.ColorfulFX;
import exomagica.common.blocks.BlockChalk;
import exomagica.common.blocks.BlockChalk.ChalkType;
import exomagica.common.tiles.TileAltar;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import scala.actors.threadpool.Arrays;

// "BASIC"????? Yep, we are not creative enough for these names :D
// TODO check if we will keep this name
public class RitualBasic implements IRitual {

    @Override
    public boolean checkPattern(IRitualCore core, IBlockAccess world, BlockPos pos) {
        if(core != ExoContent.ALTAR) return false;

        if(checkPattern(ChalkType.REGULAR, world, pos)) {
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getCoreItem(IRitualCore core, IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() == ExoContent.ALTAR) {
            TileAltar altar = (TileAltar)world.getTileEntity(pos);
            return altar.getStackInSlot(0);
        }
        return null;
    }

    @Override
    public List<ItemStack> getItems(IRitualCore core, IBlockAccess world, BlockPos pos) {
        List<ItemStack> items = new ArrayList<ItemStack>();

        for(EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos pos2 = pos.offset(facing, 3);
            IBlockState state = world.getBlockState(pos2);
            if(state.getBlock() != ExoContent.ALTAR) continue;
            TileAltar altar = (TileAltar)world.getTileEntity(pos2);
            items.add(altar.getStackInSlot(0));
        }

        return items;
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
        if(state.getBlock() != ExoContent.CHALK) return false;
        if(state.getValue(BlockChalk.TYPE) != type) return false;
        return true;
    }

    @Override
    public RitualRecipeContainer startRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos, Side side) {
        System.out.println("START RITUAL: " + side);
        int ticks = recipe.startRecipe(this);
        return new RitualRecipeContainer(this, recipe, core, world, pos, ticks < 0 ? 100 : ticks);
    }

    @Override
    public boolean finishRitual(RitualRecipeContainer container, Side side) {
        System.out.println("FINISH RITUAL: " + side);
        return container.recipe.finishRecipe(this);
    }

    @Override
    public void tickRitual(RitualRecipeContainer c, Side side) {
        if(side == Side.CLIENT) {
            ColorfulFX fx = new ColorfulFX((World)c.world, c.pos.getX() + 0.5, c.pos.getY() + 1.75, c.pos.getZ() + 0.5, true);
            fx.randomizeSpeed();
            fx.setMaxAge(c.ticksLeft);
            fx.multiplyVelocity(c.ticksLeft / 500F);
            Minecraft.getMinecraft().effectRenderer.addEffect(fx);
        }
    }

    public static class RitualBasicRecipe implements IRitualRecipe<RitualBasic> {

        private final List<ItemStack> requiredItems;
        private final ItemStack coreItem;
        private final ItemStack result;

        public RitualBasicRecipe(ItemStack result, ItemStack coreItem, List<ItemStack> requiredItems) {
            this.requiredItems = requiredItems;
            this.coreItem = coreItem;
            this.result = result;
        }

        public RitualBasicRecipe(ItemStack result, ItemStack coreItem, ItemStack ... requiredItems) {
            this.requiredItems = Arrays.asList(requiredItems);
            this.coreItem = coreItem;
            this.result = result;
        }

        @Override
        public ItemStack getCoreItem(RitualBasic ritual) {
            return coreItem;
        }

        @Override
        public List<ItemStack> getRequiredItems(RitualBasic ritual) {
            return requiredItems;
        }

        @Override
        public ItemStack getResult(RitualBasic ritual) {
            return result;
        }

        @Override
        public int startRecipe(RitualBasic ritual) {
            return -1;
        }

        @Override
        public boolean finishRecipe(RitualBasic ritual) {
            return true;
        }

    }

}
