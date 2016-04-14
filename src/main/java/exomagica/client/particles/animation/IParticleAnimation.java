package exomagica.client.particles.animation;

import exomagica.client.particles.ExoFX;

public interface IParticleAnimation {

    void animate(ExoFX particle, float ticks, int maxAge);

}
