package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import java.util.ArrayDeque;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ExoFX extends EntityFX {

    protected boolean depth = true;
    protected float f2, f3, f4, f5, f6;

    public ExoFX(World worldIn, double posXIn, double posYIn, double posZIn, boolean depth) {
        super(worldIn, posXIn, posYIn, posZIn);
        this.depth = depth;
    }

    @Override
    public void renderParticle(WorldRenderer worldRenderer, Entity e, float partialTicks, float f2, float f3, float f4, float f5, float f6) {
        addToQueue(this.depth ? ParticleRenderer.RADIAL_PARTICLES : ParticleRenderer.RADIAL_NO_DEPTH_PARTICLES, f2, f3, f4, f5, f6);
    }

    public void addToQueue(ArrayDeque array, float f2, float f3, float f4, float f5, float f6) {
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.f6 = f6;
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
            return getAlpha() * ((particleMaxAge - particleAge) / 100F);
        }
        return getAlpha();
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
        GL11.glVertex3f(f11 - f2 * f10 - f5 * f10, f12 - f3 * f10, f13 - f4 * f10 - f6 * f10);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex3f(f11 - f2 * f10 + f5 * f10, f12 + f3 * f10, f13 - f4 * f10 + f6 * f10);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex3f(f11 + f2 * f10 + f5 * f10, f12 + f3 * f10, f13 + f4 * f10 + f6 * f10);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex3f(f11 + f2 * f10 - f5 * f10, f12 - f3 * f10, f13 + f4 * f10 - f6 * f10);
    }
}
