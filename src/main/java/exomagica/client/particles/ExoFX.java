package exomagica.client.particles;

import com.google.common.collect.Lists;
import exomagica.client.particles.animation.IParticleAnimation;
import java.util.ArrayDeque;
import java.util.List;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class ExoFX extends EntityFX {

    public static final ArrayDeque<ExoFX> PARTICLES = new ArrayDeque<ExoFX>();

    protected ArrayDeque<ExoFX> array = null;
    protected float rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ;
    protected boolean noClip = false;
    protected double offsetX, offsetY, offsetZ;

    protected final List<IParticleAnimation> animations;

    protected Object owner = null;
    protected boolean registered = false;

    protected boolean alphaEffect = true, scaleEffect = true;
    protected float animationTicks = 40;
    protected boolean hasFinalCoords = false;
    protected double finalX, finalY, finalZ, finalRange;

    public ExoFX(World world, double x, double y, double z, ArrayDeque<ExoFX> array, IParticleAnimation ... animations) {
        super(world, x, y, z);
        this.array = array;
        this.animations = Lists.newArrayList(animations);
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks,
                               float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        addToQueue(this.array, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);

        if(!animations.isEmpty()) {
            float ticks = particleAge + partialTicks;
            setOffset(0, 0, 0);
            for(IParticleAnimation animation : animations) {
                animation.animate(this, ticks, particleMaxAge);
            }
        }

        if(!registered) {
            if(!PARTICLES.contains(this)) PARTICLES.add(this);
            registered = true;
        }
    }

    @Override
    public int getFXLayer() {
        return 0;
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

    @Override
    public void setExpired() {
        super.setExpired();
        PARTICLES.remove(this);
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
        if(alphaEffect && particleMaxAge - animationTicks < particleAge) {
            return particleAlpha * ((particleMaxAge - particleAge) / animationTicks);
        }
        return particleAlpha;
    }

    public float getScale(float partialTicks) {
        if(scaleEffect && particleMaxAge - animationTicks < particleAge) {
            return particleScale * ((particleMaxAge - particleAge) / animationTicks);
        }
        return particleScale;
    }

    public void setSpeed(double x, double y, double z) {
        this.xSpeed = x;
        this.ySpeed = y;
        this.zSpeed = z;
    }

    public void randomizeSpeed() {
        setSpeed((rand.nextDouble() - 0.5) * 2, (rand.nextDouble() - 0.5) * 2, (rand.nextDouble() - 0.5) * 2);
    }

    @Override
    public EntityFX multiplyVelocity(float multiplier) {
        this.xSpeed *= multiplier;
        this.ySpeed *= multiplier;
        this.zSpeed *= multiplier;
        return this;
    }

    public void setGravity(float gravity) {
        this.particleGravity = gravity;
    }

    public void setAge(int age) {
        this.particleAge = age;
    }

    public void setAnimationTicks(int ticks) {
        animationTicks = ticks;
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

    public void setOffset(double offsetX, double offsetY, double offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public void addOffset(double offsetX, double offsetY, double offsetZ) {
        this.offsetX += offsetX;
        this.offsetY += offsetY;
        this.offsetZ += offsetZ;
    }

    public void addAnimation(IParticleAnimation animation) {
        animations.add(animation);
    }

    public List<IParticleAnimation> getAnimations() {
        return animations;
    }

    public void clearAnimations() {
        animations.clear();
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public void renderExoParticle(float partialTicks) {
        float f10 = 0.5F * getScale(partialTicks);
        float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX + offsetX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY + offsetY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ + offsetZ);

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
