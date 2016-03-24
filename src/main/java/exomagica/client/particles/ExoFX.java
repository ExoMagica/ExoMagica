package exomagica.client.particles;

import java.util.ArrayDeque;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ExoFX extends EntityFX {

    protected ArrayDeque<ExoFX> array = null;
    protected float rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ;

    public ExoFX(World world, double x, double y, double z, ArrayDeque<ExoFX> array) {
        super(world, x, y, z);
        this.array = array;
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        addToQueue(this.array, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }

    public void addToQueue(ArrayDeque array, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        this.rotationX = rotationX;
        this.rotationZ = rotationZ;
        this.rotationYZ = rotationYZ;
        this.rotationXY = rotationXY;
        this.rotationXZ = rotationXZ;
        array.add(this);
    }

    public float getRedColorF(float partialTicks) {
        return getRedColorF();
    }

    public float getGreenColorF(float partialTicks) {
        return getGreenColorF();
    }

    public float getBlueColorF(float partialTicks) {
        return getBlueColorF();
    }

    public float getAlpha(float partialTicks) {
        if(particleMaxAge - 100 < particleAge) {
            return particleAlpha * ((particleMaxAge - particleAge) / 100F);
        }
        return particleAlpha;
    }

    public float getScale(float partialTicks) {
        if(particleMaxAge - 40 < particleAge) {
            return particleScale * ((particleMaxAge - particleAge) / 40F);
        }
        return particleScale;
    }

    public void renderExoParticle(float partialTicks) {
        float f10 = 0.5F * getScale(partialTicks);
        float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);

        GL11.glColor4f(getRedColorF(partialTicks), getGreenColorF(partialTicks), getBlueColorF(partialTicks), getAlpha(partialTicks));

        GL11.glTexCoord2f(0, 1);
        GL11.glVertex3f(f11 - rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 - rotationYZ * f10 - rotationXZ * f10);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(f11 - rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 - rotationYZ * f10 + rotationXZ * f10);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(f11 + rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 + rotationYZ * f10 + rotationXZ * f10);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(f11 + rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 + rotationYZ * f10 - rotationXZ * f10);
    }
}
