package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import exomagica.client.particles.animation.IParticleAnimation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * @author Guilherme Chaguri
 */
public class CubeFX extends ExoFX {

    protected static final ModelRenderer BOX = new ModelRenderer(new CubeModel(1, 1)).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);

    protected float rMod, rX, rY, rZ;
    protected ModelRenderer model = BOX;

    public CubeFX(World w, double x, double y, double z, float r, float g, float b, IParticleAnimation ... animations) {
        super(w, x, y, z, ParticleRenderer.CUBE_PARTICLES, animations);
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleAlpha = 1;
        this.particleMaxAge = 500;

        this.rMod = rand.nextInt(100);
        this.rX = (rand.nextFloat() * 2) - 1;
        this.rY = (rand.nextFloat() * 2) - 1;
        this.rZ = (rand.nextFloat() * 2) - 1;
    }

    @Override
    public void renderExoParticle(float partialTicks) {
        GL11.glPushMatrix();

        double x = prevPosX + (posX - prevPosX) * partialTicks - interpPosX + offsetX;
        double y = prevPosY + (posY - prevPosY) * partialTicks - interpPosY + offsetY;
        double z = prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ + offsetZ;
        GL11.glTranslated(x, y, z);

        float scale = (getScale(partialTicks) + 0.1F) / 10F;
        GL11.glScalef(scale, scale, scale);

        GL11.glRotatef(rMod + (particleAge + partialTicks), rX, rY, rZ);

        GL11.glColor4f(getRedColorF(partialTicks), getGreenColorF(partialTicks), getBlueColorF(partialTicks), getAlpha(partialTicks));

        model.render(1);

        GL11.glPopMatrix();
    }

    protected static class CubeModel extends ModelBase {
        protected CubeModel(int w, int h) {
            this.textureWidth = w;
            this.textureHeight = h;
        }
    }
}
