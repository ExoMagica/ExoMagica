package exomagica.client;

import exomagica.ExoContent;
import exomagica.client.handlers.ParticleRenderer;
import exomagica.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.common.MinecraftForge;
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
