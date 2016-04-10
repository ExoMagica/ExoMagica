package exomagica.common.entities;

import com.sun.org.apache.regexp.internal.RE;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.handlers.RitualHandler;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class EntityRitual extends Entity {

    public static final DataParameter<BlockPos> CORE = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<String> RITUAL = EntityDataManager.createKey(EntityRitual.class, DataSerializers.STRING);
    public static final DataParameter<String> RECIPE = EntityDataManager.createKey(EntityRitual.class, DataSerializers.STRING);
    public static final DataParameter<Integer> TICKS = EntityDataManager.createKey(EntityRitual.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> CANCELLED = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BOOLEAN);

    private RitualRecipeContainer container;
    private boolean cancelled = false;

    public EntityRitual(World world, RitualRecipeContainer container) {
        super(world);
        this.container = container;
        this.dataWatcher.register(CORE, container.pos);
        this.dataWatcher.register(RITUAL, null); // TODO
        this.dataWatcher.register(RECIPE, null);
        this.dataWatcher.register(TICKS, container.ticksLeft);
        this.dataWatcher.register(CANCELLED, false);
    }

    @Override
    public void moveEntity(double x, double y, double z) {}
    @Override
    protected void doBlockCollisions() {}
    @Override
    public void addVelocity(double x, double y, double z) {}
    @Override
    public void applyEntityCollision(Entity entityIn) {}
    @Override
    public void onCollideWithPlayer(EntityPlayer entityIn) {}
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    @Override
    public boolean canBePushed() {
        return false;
    }
    @Override
    public boolean canTriggerWalking() {
        return false;
    }
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return null;
    }
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    @Override
    public void onUpdate() {
        Side side = worldObj.isRemote ? Side.CLIENT : Side.SERVER;

        if(cancelled) {
            container.recipe.cancelRecipe(container.ritual);
            container.ritual.cancelRitual(container, side);
            setDead();
            return;
        }

        if(container.ticksLeft % 20 == 0) {
            if(!RitualHandler.checkRecipe(container.ritual, container.recipe, container.inventories)) {
                cancelled = true;
                this.dataWatcher.set(CANCELLED, cancelled);
                return;
            }
        }

        if(container.ticksLeft-- <= 0) {
            if(RitualHandler.checkRecipe(container.ritual, container.recipe, container.inventories) &&
                    container.ritual.finishRitual(container, side)) {
                RitualHandler.craft(container);
                setDead();
            } else {
                cancelled = true;
                this.dataWatcher.set(CANCELLED, cancelled);
            }
            return;
        }

        container.ritual.tickRitual(container, side);
        this.dataWatcher.set(TICKS, container.ticksLeft);
        this.dataWatcher.setClean();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if(key == TICKS) {
            container.ticksLeft = dataWatcher.get(TICKS).intValue();
        } else if(key == CANCELLED) {
            cancelled = dataWatcher.get(CANCELLED).booleanValue();
        }
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        BlockPos pos = new BlockPos(nbt.getInteger("PosX"), nbt.getInteger("PosY"), nbt.getInteger("PosZ"));
        IRitual ritual = RitualHandler.findRitual(this.worldObj, pos);
        //TODO
        if(ritual != null) {
            //container = ritual.createContainer();
            container.readFromNBT(nbt);
        }
        cancelled = nbt.getBoolean("Cancelled");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        container.writeToNBT(nbt);
        //nbt.setString("Ritual", RitualHandler.getRitual()container.ritual);
    }
}
