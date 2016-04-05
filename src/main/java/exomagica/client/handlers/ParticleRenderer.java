package exomagica.client.handlers;

import exomagica.ExoMagica;
import exomagica.client.particles.ExoFX;
import java.util.ArrayDeque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * This is based on Vazkii's particle dispatcher
 */
public class ParticleRenderer {

    public static final ResourceLocation RADIAL_TEXTURE = new ResourceLocation(ExoMagica.MODID, "textures/particle/radial.png");
    public static final ResourceLocation CUBE_TEXTURE = new ResourceLocation(ExoMagica.MODID, "textures/particle/pixel.png"); // TODO remove this and make a better render code

    public static final ArrayDeque<ExoFX> RADIAL_PARTICLES = new ArrayDeque<ExoFX>();
    public static final ArrayDeque<ExoFX> RADIAL_NO_DEPTH_PARTICLES = new ArrayDeque<ExoFX>();

    public static final ArrayDeque<ExoFX> CUBE_PARTICLES = new ArrayDeque<ExoFX>();
    public static final ArrayDeque<ExoFX> TEXTURED_CUBE_PARTICLES = new ArrayDeque<ExoFX>();

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        TextureManager tex = Minecraft.getMinecraft().getTextureManager();
        float partialTicks = event.getPartialTicks();

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glPushAttrib(GL11.GL_LIGHTING);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LESS);
        /* CUBE */
        renderCube(tex, partialTicks);

        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        GL11.glDisable(GL11.GL_LIGHTING);

        /* RADIAL */
        renderRadial(tex, partialTicks);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
    }

    public void renderRadial(TextureManager tex, float partialTicks) {
        tex.bindTexture(RADIAL_TEXTURE);

        // DEPTH

        if(!RADIAL_PARTICLES.isEmpty()) {
            GL11.glBegin(GL11.GL_QUADS);
            for(ExoFX particle : RADIAL_PARTICLES) particle.renderExoParticle(partialTicks);
            RADIAL_PARTICLES.clear();
            GL11.glEnd();
        }

        // NO DEPTH

        if(!RADIAL_NO_DEPTH_PARTICLES.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBegin(GL11.GL_QUADS);
            for(ExoFX particle : RADIAL_NO_DEPTH_PARTICLES) particle.renderExoParticle(partialTicks);
            RADIAL_NO_DEPTH_PARTICLES.clear();
            GL11.glEnd();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    public void renderCube(TextureManager tex, float partialTicks) {
        tex.bindTexture(CUBE_TEXTURE);

        if(!CUBE_PARTICLES.isEmpty()) {
            for(ExoFX particle : CUBE_PARTICLES) particle.renderExoParticle(partialTicks);
            CUBE_PARTICLES.clear();
        }

        tex.bindTexture(TextureMap.locationBlocksTexture);

        if(!TEXTURED_CUBE_PARTICLES.isEmpty()) {
            for(ExoFX particle : TEXTURED_CUBE_PARTICLES) particle.renderExoParticle(partialTicks);
            TEXTURED_CUBE_PARTICLES.clear();
        }
    }

}
