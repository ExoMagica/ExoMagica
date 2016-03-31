package exomagica.api.ritual;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RitualRecipeContainer {

    public final IRitual ritual;
    public final IRitualRecipe recipe;
    public final IRitualCore core;
    public final IBlockAccess world;
    public final BlockPos pos;
    public int ticksLeft;

    public RitualRecipeContainer(IRitual ritual, IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos, int ticks) {
        this.ritual = ritual;
        this.recipe = recipe;
        this.core = core;
        this.world = world;
        this.pos = pos;
        this.ticksLeft = ticks;
    }

}
