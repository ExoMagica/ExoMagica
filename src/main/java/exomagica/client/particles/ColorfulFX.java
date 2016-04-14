package exomagica.client.particles;

import exomagica.client.particles.animation.IParticleAnimation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ColorfulFX extends RadialFX {
    public ColorfulFX(World w, double x, double y, double z, boolean depth, IParticleAnimation ... animations) {
        super(w, x, y, z, depth, 1, 1, 1, animations);
    }

    @Override
    public float getRedColorF(float partialTicks) {
        return (MathHelper.sin(0.1F * (particleAge + partialTicks) + 0) * 127 + 128) / 255F;
    }

    @Override
    public float getGreenColorF(float partialTicks) {
        return (MathHelper.sin(0.1F * (particleAge + partialTicks) + 2) * 127 + 128) / 255F;
    }

    @Override
    public float getBlueColorF(float partialTicks) {
        return (MathHelper.sin(0.1F * (particleAge + partialTicks) + 4) * 127 + 128) / 255F;
    }

}
