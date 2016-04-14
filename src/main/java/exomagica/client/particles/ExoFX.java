package exomagica.client.particles;

import com.google.common.collect.ImmutableList;
import exomagica.client.particles.animation.IParticleAnimation;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.List;

public class ExoFX extends EntityFX {

    protected ArrayDeque<ExoFX> array = null;
    protected float rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ;
    protected boolean noClip = false;

    protected final List<IParticleAnimation> animations;

    protected boolean alphaEffect = true, scaleEffect = true;
    protected boolean hasFinalCoords = false;
    protected double finalX, finalY, finalZ, finalRange;

    public ExoFX(World world, double x, double y, double z, ArrayDeque<ExoFX> array, IParticleAnimation ... animations) {
        super(world, x, y, z);
        this.array = array;
        this.animations = ImmutableList.copyOf(animations);
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        addToQueue(this.array, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

        float ticks = particleAge + partialTicks;
        for(IParticleAnimation animation : animations) {
            animation.animate(this, ticks, particleMaxAge);
        }
    }

    protected void addToQueue(ArrayDeque array, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        this.rotationX = rotationX;
        this.rotationZ = rotationZ;
        this.rotationYZ = rotationYZ;
        this.rotationXY = rotationXY;
        this.rotationXZ = rotationXZ;
        array.add(this);
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        if(noClip) {
            this.posX += x;
            this.posY += y;
            this.posZ += z;
        } else {
            super.moveEntity(x, y, z);
        }
    }

    @Override
    public void onUpdate() {
        if(hasFinalCoords) {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            if(this.particleAge++ >= this.particleMaxAge) this.setExpired();

            this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
            if(posX >= finalX - finalRange && posY >= finalY - finalRange && posZ >= finalZ - finalRange &&
                posX <= finalX + finalRange && posY <= finalY + finalRange && posZ <= finalZ + finalRange) {
                this.setExpired();
            }
        } else {
            super.onUpdate();
        }
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
        if(alphaEffect && particleMaxAge - 100 < particleAge) {
            return particleAlpha * ((particleMaxAge - particleAge) / 100F);
        }
        return particleAlpha;
    }

    public float getScale(float partialTicks) {
        if(scaleEffect && particleMaxAge - 40 < particleAge) {
            return particleScale * ((particleMaxAge - particleAge) / 40F);
        }
        return particleScale;
    }

    public void setSpeed(double x, double y, double z) {
        this.xSpeed = x;
        this.ySpeed = y;
        this.zSpeed = z;
    }

    public void randomizeSpeed() {
        setSpeed((rand.nextGaussian() * 2) - 1, (rand.nextGaussian() * 2) - 1, (rand.nextGaussian() * 2) - 1);
    }

    public void setGravity(float gravity) {
        this.particleGravity = gravity;
    }

    public void setAge(int age) {
        this.particleAge = age;
    }

    public void setAlphaEffect(boolean alphaEffect) {
        this.alphaEffect = alphaEffect;
    }

    public void setScaleEffect(boolean scaleEffect) {
        this.scaleEffect = scaleEffect;
    }

    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }

    public void disableFinalCoords() {
        this.hasFinalCoords = false;
    }

    public void enableFinalCoords(double x, double y, double z, double speed, int maxAge) {
        this.finalX = x;
        this.finalY = y;
        this.finalZ = z;
        this.xSpeed = (x - posX) * speed;
        this.ySpeed = (y - posY) * speed;
        this.zSpeed = (z - posZ) * speed;
        this.finalRange = speed * 2;
        this.hasFinalCoords = true;
        this.particleMaxAge = maxAge;
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
