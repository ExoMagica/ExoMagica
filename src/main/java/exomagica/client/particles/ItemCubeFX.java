package exomagica.client.particles;

import exomagica.client.handlers.ParticleRenderer;
import exomagica.client.particles.animation.IParticleAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCubeFX extends CubeFX {

    // I know this looks awful. It must be the size of the TextureMap.locationBlocksTexture or bigger
    protected static final int TEX_W = 50000, TEX_H = 50000;

    protected static CubeModel CUBE_MODEL = new CubeModel(TEX_W * 6, TEX_H * 6);

    protected TextureManager manager;

    public ItemCubeFX(World w, double x, double y, double z, ItemStack item, IParticleAnimation ... animations) {
        super(w, x, y, z, 1, 1, 1, animations);
        Minecraft mc = Minecraft.getMinecraft();

        this.particleTexture = mc.getRenderItem().getItemModelMesher().getParticleIcon(item.getItem(), item.getMetadata());
        if(particleTexture == null) this.particleTexture = mc.getTextureMapBlocks().getMissingSprite();

        this.manager = mc.getTextureManager();

        this.array = ParticleRenderer.ITEM_CUBE_PARTICLES;

        int minU = (int)(particleTexture.getMinU() * TEX_W);
        int minV = (int)(particleTexture.getMinV() * TEX_H);
        int maxU = (int)(particleTexture.getMaxU() * TEX_W);
        int maxV = (int)(particleTexture.getMaxV() * TEX_H);

        int u, v;
        if(maxU <= minU || maxV <= minV) {
            u = minU * 6;
            v = minV * 6;
        } else {
            u = (rand.nextInt(maxU - minU) + minU) * 6;
            v = (rand.nextInt(maxV - minV) + minV) * 6;
        }

        this.model = new ModelRenderer(CUBE_MODEL, u, v).addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
    }
}
