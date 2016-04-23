package exomagica.common.entities;

import exomagica.api.nodes.IAura;
import exomagica.api.nodes.INode;
import exomagica.common.handlers.NodeHandler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityNode extends Entity implements INode {

    private IAura aura = null;

    public EntityNode(World w) {
        super(w);
    }

    public EntityNode(World w, IAura aura) {
        super(w);
        this.aura = aura;
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        aura.tick(this);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {
        this.aura = NodeHandler.findAura(nbt.getString("AuraType"));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setString("AuraType", NodeHandler.getAuraName(aura));
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
