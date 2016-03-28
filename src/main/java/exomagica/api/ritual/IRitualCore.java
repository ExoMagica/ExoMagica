package exomagica.api.ritual;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IRitualCore {

    /**
     * Check if is a ritual core in these conditions
     * @param world The world
     * @param pos The position
     * @param state The state
     * @return Whether this block is a core
     */
    boolean isRitualCore(IBlockAccess world, BlockPos pos, IBlockState state);

}
