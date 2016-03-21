package exomagica.client;

import exomagica.ExoContent;
import exomagica.client.blocks.AltarRenderer;
import exomagica.client.handlers.ParticleRenderer;
import exomagica.common.CommonProxy;
import exomagica.common.tiles.TileAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void prepare() {
        super.prepare();
        //OBJLoader.instance.addDomain(ExoMagica.MODID.toLowerCase());
    }

    @Override
    public void registerItems() {
        super.registerItems();

        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

        mesher.register(ExoContent.SCROLL, 0, ExoContent.SCROLL.MODEL);
    }

    @Override
    public void registerBlocks() {
        super.registerBlocks();

        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new AltarRenderer());
    }

    @Override
    public void registerEntities() {
        super.registerEntities();
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ParticleRenderer());
    }

}
