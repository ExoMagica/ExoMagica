package exomagica.client.particles;

import net.minecraft.world.World;

/**
 * @author Guilherme Chaguri
 */
public class ColorfulFX extends RadialFX {
    public ColorfulFX(World w, double x, double y, double z, boolean depth) {
        super(w, x, y, z, depth, 1, 1, 1);
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
