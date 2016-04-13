package exomagica.common.entities;

import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.handlers.RitualHandler;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.Map;

public class EntityRitual extends Entity {

    public static final DataParameter<BlockPos> RITUAL = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<String> RECIPE = EntityDataManager.createKey(EntityRitual.class, DataSerializers.STRING);
    public static final DataParameter<Integer> TICKS = EntityDataManager.createKey(EntityRitual.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> CANCELLED = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BOOLEAN);

    private RitualRecipeContainer container;
    private boolean cancelled = false;

    public EntityRitual(World world) {
        super(world);
        this.container = null;
        this.dataWatcher.register(RITUAL, null);
        this.dataWatcher.register(RECIPE, null);
        this.dataWatcher.register(TICKS, 0);
        this.dataWatcher.register(CANCELLED, false);
        this.width = 0;
        this.height = 0;
    }

    public EntityRitual(World world, RitualRecipeContainer container) {
        super(world);
        this.container = container;
        this.dataWatcher.register(RITUAL, container.pos);
        this.dataWatcher.register(RECIPE, RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
        this.dataWatcher.register(TICKS, container.ticksLeft);
        this.dataWatcher.register(CANCELLED, false);
        this.width = 0;
        this.height = 0;
        this.setPosition(container.pos.getX(), container.pos.getY(), container.pos.getZ());
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

        boolean wasDirty = dataWatcher.isDirty();
        this.dataWatcher.set(TICKS, container.ticksLeft);
        // Prevent the server from sending a metadata packet each regular tick
        if(!wasDirty) this.dataWatcher.setClean();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if(key == CANCELLED) {
            cancelled = dataWatcher.get(CANCELLED).booleanValue();
        } else if(container == null) {

            if(dataWatcher.get(RITUAL) != null && dataWatcher.get(RECIPE) != null) {
                loadFromData(dataWatcher.get(RITUAL), dataWatcher.get(RECIPE), dataWatcher.get(TICKS), true);
            }

        } else if(key == TICKS) {
            container.ticksLeft = dataWatcher.get(TICKS).intValue();
        }
    }

    @Override
    protected void entityInit() {

    }

    protected void loadFromData(BlockPos pos, String recipeId, int ticksLeft, boolean setDW) {
        isDead = true;

        IRitualCore core = RitualHandler.getCore(worldObj, pos);

        if(core == null) return;

        IRitual ritual = RitualHandler.findRitual(core, worldObj, pos);

        if(ritual == null) return;

        IRitualRecipe recipe = RitualHandler.findRitualRecipe(ritual, recipeId);

        if(recipe == null) return;

        Map<String, List<IInventory>> inventories = ritual.getInventories(core, worldObj, pos);
        Side side = worldObj.isRemote ? Side.CLIENT : Side.SERVER;

        container = ritual.createContainer(recipe, core, worldObj, pos, ticksLeft, inventories, side);

        if(setDW) {
            this.dataWatcher.set(RITUAL, container.pos);
            this.dataWatcher.set(RECIPE, RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
            this.dataWatcher.set(TICKS, container.ticksLeft);
            this.dataWatcher.set(CANCELLED, false);
        }

        isDead = false; // If everything was loaded correctly, keep it spawned
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        BlockPos pos = new BlockPos(nbt.getInteger("RitualX"), nbt.getInteger("RitualY"), nbt.getInteger("RitualZ"));
        loadFromData(pos, nbt.getString("Recipe"), nbt.getInteger("TicksLeft"), true);

        this.setPosition(container.pos.getX(), container.pos.getY(), container.pos.getZ());
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        container.writeToNBT(nbt);
        nbt.setInteger("RitualX", container.pos.getX());
        nbt.setInteger("RitualY", container.pos.getY());
        nbt.setInteger("RitualZ", container.pos.getZ());
        nbt.setInteger("TicksLeft", container.ticksLeft);
        nbt.setString("Recipe", RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
    }
}
