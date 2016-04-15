package exomagica.client.particles.animation;

import exomagica.client.particles.ExoFX;
import net.minecraft.util.math.MathHelper;

public class RotateAnimation implements IParticleAnimation {

    private final float speed, startAngle;
    private final double x, y, z;

    private float radius;

    public RotateAnimation(float speed, double startX, double startY, double startZ, double centerX, double centerY, double centerZ) {
        this.speed = speed;
        this.x = centerX - startX;
        this.y = centerY - startY;
        this.z = centerZ - startZ;
        this.startAngle = (float)Math.atan2(startX - centerX, startZ - centerZ);
        this.radius = (float)Math.sqrt(Math.pow(startX - centerX, 2) + Math.pow(startZ - centerZ, 2));
    }

    @Override
    public void animate(ExoFX particle, float ticks, int maxAge) {
        float angle = startAngle + ((ticks / 20F) * speed);
        float r = radius * ((maxAge - ticks) / maxAge);
        particle.addOffset(x + (MathHelper.sin(angle) * r), y, z + (MathHelper.cos(angle) * r));
    }

}
