package exomagica.common.entities;

import exomagica.api.nodes.IAura;
import exomagica.api.nodes.INode;
import exomagica.client.particles.ColorfulFX;
import exomagica.common.handlers.NodeHandler;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityNode extends Entity implements INode {

    public static final DataParameter<String> AURA = EntityDataManager.createKey(EntityNode.class, DataSerializers.STRING);

    private IAura aura = null;

    public EntityNode(World w) {
        super(w);
        this.dataWatcher.register(AURA, "");
    }

    public EntityNode(World w, IAura aura) {
        super(w);
        this.aura = aura;
        this.dataWatcher.register(AURA, NodeHandler.getAuraName(aura));
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(worldObj.isRemote) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ColorfulFX(worldObj, posX, posY, posZ, false));
        }
        if(aura != null) {
            aura.tick(this);
        }
    }

    @Override
    protected void entityInit() {
        width = 0;
        height = 0;
    }

    @Override
    protected void doBlockCollisions() {}
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
    public void notifyDataManagerChange(DataParameter<?> key) {
        if(key == AURA) {
            this.aura = NodeHandler.findAura(dataWatcher.get(AURA));
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.aura = NodeHandler.findAura(nbt.getString("AuraType"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        if(aura != null) {
            nbt.setString("AuraType", NodeHandler.getAuraName(aura));
        }
    }

    @Override
    public IAura getAura() {
        return aura;
    }

    @Override
    public Vec3d getPos() {
        return getPositionVector();
    }

    @Override
    public World getWorld() {
        return getEntityWorld();
    }
}
