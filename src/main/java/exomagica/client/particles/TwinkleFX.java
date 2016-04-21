package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import exomagica.client.particles.animation.IParticleAnimation;
import net.minecraft.world.World;

public class TwinkleFX extends ExoFX {

    public TwinkleFX(World world, double x, double y, double z, boolean depth, IParticleAnimation... animations) {
        super(world, x, y, z, depth ? ParticleRenderer.TWINKLE_PARTICLES : ParticleRenderer.TWINKLE_NO_DEPTH_PARTICLES, animations);
    }

}
