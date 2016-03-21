package exomagica.client.particles;

import net.minecraft.world.World;

public class ColorfulFX extends ExoFX {

    public ColorfulFX(World worldIn, double posXIn, double posYIn, double posZIn, boolean depth) {
        super(worldIn, posXIn, posYIn, posZIn, depth);
        this.particleMaxAge = 100;
        this.particleGravity = 0.01F;
        this.particleScale = 1;
        this.particleAlpha = (rand.nextFloat() * 0.75F) + 0.3F;
        this.motionY = 0.05F;
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
