package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import net.minecraft.world.World;

public class RadialFX extends ExoFX {

    public RadialFX(World w, double x, double y, double z, boolean depth, float r, float g, float b) {
        super(w, x, y, z, depth ? ParticleRenderer.RADIAL_PARTICLES : ParticleRenderer.RADIAL_NO_DEPTH_PARTICLES);
        this.particleMaxAge = 100;
        this.particleGravity = 0.01F;
        this.particleScale = 1;
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleAlpha = (rand.nextFloat() * 0.75F) + 0.3F;
        this.ySpeed = 0.05F;
    }

}
