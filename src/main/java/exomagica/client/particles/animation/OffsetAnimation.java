package exomagica.client.particles.animation;

import exomagica.client.particles.ExoFX;

/**
 * @author Guilherme Chaguri
 */
public class OffsetAnimation implements IParticleAnimation {
    private final double maxX, maxY, maxZ;

    public OffsetAnimation(double offsetX, double offsetY, double offsetZ) {
        this.maxX = offsetX;
        this.maxY = offsetY;
        this.maxZ = offsetZ;
    }

    public OffsetAnimation(double multiplier) {
        this((Math.random() - 0.5) * multiplier, (Math.random() - 0.5) * multiplier, (Math.random() - 0.5) * multiplier);
    }

    @Override
    public void animate(ExoFX particle, float ticks, int maxAge) {
        float d = maxAge / 10F;
        particle.addOffset(calcEffect(maxX, d, ticks, maxAge),
                            calcEffect(maxY, d, ticks, maxAge),
                            calcEffect(maxZ, d, ticks, maxAge));
    }

    private double calcEffect(double max, float effectTicks, float ticks, int maxAge) {
        if(maxAge - effectTicks < ticks) {
            return max * ((maxAge - ticks) / effectTicks);
        } else if(ticks < effectTicks) {
            return max * (ticks / effectTicks);
        }
        return max;
    }

}
