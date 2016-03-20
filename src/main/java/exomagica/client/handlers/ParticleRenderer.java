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

public class ParticleRenderer {

    public static final ArrayDeque<ExoFX> RADIAL_PARTICLES = new ArrayDeque<ExoFX>();
    public static final ResourceLocation RADIAL_TEXTURE = new ResourceLocation(ExoMagica.MODID, "textures/particle/radial.png");

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        TextureManager tex = Minecraft.getMinecraft().getTextureManager();

        GL11.glPushAttrib(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
        GL11.glDisable(GL11.GL_LIGHTING);

        // RADIAL

        tex.bindTexture(RADIAL_TEXTURE);

        //renderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        GL11.glBegin(GL11.GL_QUADS);

        for(ExoFX particle : RADIAL_PARTICLES) {
            particle.renderExoParticle(event.partialTicks);
        }
        RADIAL_PARTICLES.clear();

        GL11.glEnd();
        //tessellator.draw();

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glPopAttrib();
    }

}
