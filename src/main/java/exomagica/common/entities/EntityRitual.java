package exomagica.common.entities;

import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.client.sounds.SoundRitualLoop;
import exomagica.client.utils.ClientUtils;
import exomagica.common.handlers.RitualHandler;
import java.util.List;
import java.util.Map;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRitual extends Entity {

    public static final DataParameter<BlockPos> RITUAL = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BLOCK_POS);
    public static final DataParameter<String> RECIPE = EntityDataManager.createKey(EntityRitual.class, DataSerializers.STRING);
    public static final DataParameter<Integer> TICKS_LEFT = EntityDataManager.createKey(EntityRitual.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> TOTAL_TICKS = EntityDataManager.createKey(EntityRitual.class, DataSerializers.VARINT);
    public static final DataParameter<Boolean> SUCCESS = EntityDataManager.createKey(EntityRitual.class, DataSerializers.BOOLEAN);

    private NBTTagCompound toLoad = null;
    private RitualRecipeContainer container;
    private boolean success = false;

    @SideOnly(Side.CLIENT)
    private SoundRitualLoop loopSound = null;

    public EntityRitual(World world) {
        super(world);
        this.container = null;
        this.dataWatcher.register(RITUAL, new BlockPos(this));
        this.dataWatcher.register(RECIPE, "");
        this.dataWatcher.register(TICKS_LEFT, 0);
        this.dataWatcher.register(TOTAL_TICKS, 100);
        this.dataWatcher.register(SUCCESS, false);
    }

    public EntityRitual(World world, RitualRecipeContainer container) {
        super(world);
        this.container = container;
        this.dataWatcher.register(RITUAL, container.pos);
        this.dataWatcher.register(RECIPE, RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
        this.dataWatcher.register(TICKS_LEFT, container.ticksLeft);
        this.dataWatcher.register(TOTAL_TICKS, container.totalTicks);
        this.dataWatcher.register(SUCCESS, false);
        this.setPosition(container.pos.getX() + 0.5, container.pos.getY() + 0.5, container.pos.getZ() + 0.5);
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
        if(container == null) {
            if(toLoad != null) {

                // Load it after the world is ticking to prevent crashes
                BlockPos pos = new BlockPos(toLoad.getInteger("RitualX"), toLoad.getInteger("RitualY"), toLoad.getInteger("RitualZ"));
                loadFromData(pos, toLoad.getString("Recipe"), toLoad.getInteger("TotalTicks"), toLoad.getInteger("TicksLeft"), true);
                setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

                toLoad = null;
            }
            return;
        }

        Side side = worldObj.isRemote ? Side.CLIENT : Side.SERVER;

        if(success) {
            setDead();
            return;
        }

        if(container.ticksLeft % 20 == 0) {
            if(!RitualHandler.checkRecipe(container.ritual, container.recipe, container.inventories) ||
                    !container.ritual.checkPattern(container.core, container.world, container.pos)) {
                success = false;
                setDead();
                return;
            }
        }

        if(side == Side.CLIENT) {
            if(container.ticksLeft == container.totalTicks) {
                ClientUtils.playSound(container.startSound, SoundCategory.AMBIENT, container.pos);
            }

            if(loopSound == null) {
                loopSound = new SoundRitualLoop(this, container.loopSound);
                Minecraft.getMinecraft().getSoundHandler().playSound(loopSound);
            }

            SoundEvent sound = container.timedSounds.get(container.ticksLeft);
            ClientUtils.playSound(sound, SoundCategory.AMBIENT, container.pos);
        }

        if(container.ticksLeft-- <= 0 && side == Side.SERVER) {
            if(RitualHandler.checkRecipe(container.ritual, container.recipe, container.inventories) &&
                    container.ritual.checkPattern(container.core, container.world, container.pos)) {
                success = true;
                this.dataWatcher.set(SUCCESS, success);
            } else {
                success = false;
                setDead();
            }
            return;
        }

        container.ritual.tickRitual(container, side);

        boolean wasDirty = dataWatcher.isDirty();
        this.dataWatcher.set(TICKS_LEFT, container.ticksLeft);
        // Prevent the server from sending a metadata packet each regular tick
        // but at the same time, keep the data watcher with updated data
        if(!wasDirty) this.dataWatcher.setClean();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if(key == SUCCESS) {
            success = dataWatcher.get(SUCCESS).booleanValue();
        } else if(container == null) {

            if(dataWatcher.get(RITUAL) != null && dataWatcher.get(RECIPE) != null &&
                    dataWatcher.get(TOTAL_TICKS) != null) {
                loadFromData(dataWatcher.get(RITUAL), dataWatcher.get(RECIPE),
                        dataWatcher.get(TOTAL_TICKS), dataWatcher.get(TICKS_LEFT), true);
            }

        } else if(key == TICKS_LEFT) {
            container.ticksLeft = dataWatcher.get(TICKS_LEFT).intValue();
        }
    }

    @Override
    public void setDead() {
        super.setDead();
        if(container == null) return;

        Side side = worldObj.isRemote ? Side.CLIENT : Side.SERVER;
        if(success &&
                container.recipe.finishRecipe(container.ritual) &&
                container.ritual.finishRitual(container, side)) {
            RitualHandler.craft(container);

            if(side == Side.CLIENT) ClientUtils.playSound(container.endSound, SoundCategory.AMBIENT, container.pos);
        } else {
            container.recipe.cancelRecipe(container.ritual);
            container.ritual.cancelRitual(container, side);

            if(side == Side.CLIENT) ClientUtils.playSound(container.cancelSound, SoundCategory.AMBIENT, container.pos);
        }
    }

    @Override
    protected void entityInit() {
        this.width = 0;
        this.height = 0;
    }

    private void loadFromData(BlockPos pos, String recipeId, int maxTicks, int ticksLeft, boolean setDW) {
        isDead = true;

        if(pos == null) return;

        IRitualCore core = RitualHandler.getCore(worldObj, pos);

        if(core == null) return;

        IRitual ritual = RitualHandler.findRitual(core, worldObj, pos);

        if(ritual == null) return;

        IRitualRecipe recipe = RitualHandler.findRitualRecipe(ritual, recipeId);

        if(recipe == null) return;

        Map<String, List<IInventory>> inventories = ritual.getInventories(core, worldObj, pos);
        Side side = worldObj.isRemote ? Side.CLIENT : Side.SERVER;

        container = ritual.createContainer(recipe, core, worldObj, pos, maxTicks, inventories, side);
        container.ticksLeft = ticksLeft;

        if(setDW) {
            this.dataWatcher.set(RITUAL, container.pos);
            this.dataWatcher.set(RECIPE, RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
            this.dataWatcher.set(TICKS_LEFT, container.ticksLeft);
            this.dataWatcher.set(TOTAL_TICKS, container.totalTicks);
            this.dataWatcher.set(SUCCESS, false);
        }

        isDead = false; // If everything was loaded correctly, keep it spawned
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        toLoad = nbt;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        if(container != null) {
            container.writeToNBT(nbt);
            nbt.setInteger("RitualX", container.pos.getX());
            nbt.setInteger("RitualY", container.pos.getY());
            nbt.setInteger("RitualZ", container.pos.getZ());
            nbt.setInteger("TicksLeft", container.ticksLeft);
            nbt.setInteger("TotalTicks", container.totalTicks);
            nbt.setString("Recipe", RitualHandler.getRitualRecipeName(container.ritual, container.recipe));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompund) {
        if(container != null) super.writeToNBT(tagCompund);
    }

    public RitualRecipeContainer getContainer() {
        return container;
    }

    public void finishRitual(boolean success) {
        this.dataWatcher.set(SUCCESS, success);
        this.success = success;
        if(!success) setDead();
    }

}
