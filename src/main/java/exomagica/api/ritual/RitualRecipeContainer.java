package exomagica.api.ritual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RitualRecipeContainer {

    public final IRitual ritual;
    public final IRitualRecipe recipe;
    public final IRitualCore core;
    public final IBlockAccess world;
    public final BlockPos pos;

    public final Map<String, List<IInventory>> inventories;

    public final int totalTicks;
    public int ticksLeft;

    /**
     * Number of ticks left, sound
     */
    public Map<Integer, SoundEvent> timedSounds;
    public SoundEvent startSound, endSound, cancelSound, loopSound;

    public RitualRecipeContainer(IRitual ritual, IRitualRecipe recipe, IRitualCore core, IBlockAccess world, BlockPos pos,
                                 Map<String, List<IInventory>> inventories, int ticks) {
        this.ritual = ritual;
        this.recipe = recipe;
        this.core = core;
        this.world = world;
        this.pos = pos;

        this.inventories = inventories;
        this.timedSounds = new HashMap<Integer, SoundEvent>();

        this.totalTicks = ticks;
        this.ticksLeft = ticks;
    }

    public void writeToNBT(NBTTagCompound nbt) {

    }

    public void readFromNBT(NBTTagCompound nbt) {

    }

}
