package exomagica.client.entities;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderNothing extends Render<Entity> {

    protected RenderNothing(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {}

    @Override
    public boolean shouldRender(Entity livingEntity, ICamera camera, double camX, double camY, double camZ) {
        return false;
    }

    @Override
    protected void renderEntityName(Entity p_188296_1_, double p_188296_2_, double p_188296_4_, double p_188296_6_, String p_188296_8_, double p_188296_9_) {}

    @Override
    protected void renderName(Entity entity, double x, double y, double z) {}

    @Override
    public void bindTexture(ResourceLocation location) {}

    @Override
    protected boolean bindEntityTexture(Entity entity) {
        return false;
    }

    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {}

    @Override
    protected void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance) {}

    @Override
    public void renderMultipass(Entity p_188300_1_, double p_188300_2_, double p_188300_4_, double p_188300_6_, float p_188300_8_, float p_188300_9_) {}

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

    public static class RenderNothingFactory implements IRenderFactory<Entity> {

        @Override
        public Render<? super Entity> createRenderFor(RenderManager manager) {
            return new RenderNothing(manager);
        }
    }

}
