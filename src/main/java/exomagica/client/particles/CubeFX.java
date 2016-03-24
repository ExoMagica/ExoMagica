package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * @author Guilherme Chaguri
 */
public class CubeFX extends ExoFX {

    private static final ModelRenderer BOX = new ModelRenderer(new CubeModel()).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);

    private float rMod, rX, rY, rZ;

    public CubeFX(World w, double x, double y, double z) {
        super(w, x, y, z, ParticleRenderer.CUBE_PARTICLES);
        this.particleRed = particleGreen = particleBlue = 1;
        this.particleAlpha = 0.5F;
        //this.ySpeed = 0.2F;
        this.particleMaxAge = 500;

        this.rMod = rand.nextInt(100);
        this.rX = (rand.nextFloat() * 2) - 1;
        this.rY = (rand.nextFloat() * 2) - 1;
        this.rZ = (rand.nextFloat() * 2) - 1;
    }

    @Override
    public void renderExoParticle(float partialTicks) {
        GL11.glPushMatrix();

        float x = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float y = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float z = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        GL11.glTranslated(x, y, z);

        float scale = (getScale(partialTicks) + 0.1F) / 10F;
        GL11.glScalef(scale, scale, scale);

        GL11.glRotatef(rMod + (particleAge + partialTicks), rX, rY, rZ);

        GL11.glColor4f(getRedColorF(partialTicks), getGreenColorF(partialTicks), getBlueColorF(partialTicks), getAlpha(partialTicks));

        BOX.render(1);

        GL11.glPopMatrix();
    }

    static class CubeModel extends ModelBase {

    }
}
