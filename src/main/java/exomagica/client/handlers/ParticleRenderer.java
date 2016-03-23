package exomagica.client.handlers;

import exomagica.ExoMagica;
import exomagica.client.particles.ExoFX;
import java.util.ArrayDeque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * This is based on Vazkii's particle dispatcher
 */
public class ParticleRenderer {

    public static final ArrayDeque<ExoFX> RADIAL_PARTICLES = new ArrayDeque<ExoFX>();
    public static final ArrayDeque<ExoFX> RADIAL_NO_DEPTH_PARTICLES = new ArrayDeque<ExoFX>();
    public static final ResourceLocation RADIAL_TEXTURE = new ResourceLocation(ExoMagica.MODID, "textures/particle/radial.png");

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        //if(true) return;
        GL11.glPushMatrix();
        TextureManager tex = Minecraft.getMinecraft().getTextureManager();

        GL11.glPushAttrib(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        GL11.glDisable(GL11.GL_LIGHTING);

        /* RADIAL */
        renderRadial(tex, event.partialTicks);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();

        GL11.glPopMatrix();
    }

    public void renderRadial(TextureManager tex, float partialTicks) {
        tex.bindTexture(RADIAL_TEXTURE);

        // DEPTH

        GL11.glBegin(GL11.GL_QUADS);
        for(ExoFX particle : RADIAL_PARTICLES) particle.renderExoParticle(partialTicks);
        RADIAL_PARTICLES.clear();
        GL11.glEnd();

        // NO DEPTH

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glBegin(GL11.GL_QUADS);
        for(ExoFX particle : RADIAL_NO_DEPTH_PARTICLES) particle.renderExoParticle(partialTicks);
        RADIAL_NO_DEPTH_PARTICLES.clear();
        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}
