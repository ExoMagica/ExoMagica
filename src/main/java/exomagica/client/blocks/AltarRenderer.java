package exomagica.client.blocks;

import exomagica.common.tiles.TileAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class AltarRenderer extends TileEntitySpecialRenderer<TileAltar> {
    private RenderItem itemRenderer;
    public AltarRenderer() {
        this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderTileEntityAt(TileAltar te, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemStack item = te.getStackInSlot(0);
        if(item != null) {
            float t = Minecraft.getSystemTime() / 100F;
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y + 1.65 + (MathHelper.sin(t / 15) / 10), z + 0.5);
            GL11.glRotatef(t, 0, 60, 0);
            GL11.glColor3f(1, 1, 1);
            this.itemRenderer.renderItem(item, TransformType.GROUND);

            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glTranslatef(0, -0.035F, 0);
            GL11.glScalef(1.2F, 1.2F, 1.2F);
            this.itemRenderer.renderItem(item, TransformType.GROUND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glPopMatrix();
        }
    }

}
