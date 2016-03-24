package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import net.minecraft.world.World;

public class ColorfulFX extends ExoFX {

    public ColorfulFX(World w, double x, double y, double z, boolean depth) {
        super(w, x, y, z, depth ? ParticleRenderer.RADIAL_PARTICLES : ParticleRenderer.RADIAL_NO_DEPTH_PARTICLES);
        this.particleMaxAge = 100;
        this.particleGravity = 0.01F;
        this.particleScale = 1;
        this.particleAlpha = (rand.nextFloat() * 0.75F) + 0.3F;
        this.ySpeed = 0.05F;
    }

    @Override
    public float getRedColorF(float partialTicks) {
        return (float)(Math.sin(0.1 * (particleAge + partialTicks) + 0) * 127 + 128) / 255F;
    }

    @Override
    public float getGreenColorF(float partialTicks) {
        return (float)(Math.sin(0.1 * (particleAge + partialTicks) + 2) * 127 + 128) / 255F;
    }

    @Override
    public float getBlueColorF(float partialTicks) {
        return (float)(Math.sin(0.1 * (particleAge + partialTicks) + 4) * 127 + 128) / 255F;
    }

}
