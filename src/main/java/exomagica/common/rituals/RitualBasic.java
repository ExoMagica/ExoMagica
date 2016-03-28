package exomagica.common.rituals;

import exomagica.ExoContent;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.common.blocks.BlockChalk;
import exomagica.common.blocks.BlockChalk.ChalkType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

// BASIC????? Yep, we are not creative enough :D
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
    public int startRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos) {
        return 50;
    }

    @Override
    public boolean finishRitual(IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos) {
        return true;
    }

}
