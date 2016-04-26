package exomagica.client;

import exomagica.ExoContent;
import exomagica.client.blocks.AltarRenderer;
import exomagica.client.entities.RenderNothing;
import exomagica.client.handlers.ParticleRenderer;
import exomagica.common.CommonProxy;
import exomagica.common.entities.EntityNode;
import exomagica.common.entities.EntityRitual;
import exomagica.common.tiles.TileAltar;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void prepare() {
        super.prepare();
    }

    @Override
    public void registerItems() {
        super.registerItems();
    }

    @Override
    public void registerBlocks() {
        super.registerBlocks();

        ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new AltarRenderer());

        BlockColors bc = Minecraft.getMinecraft().getBlockColors();
        bc.registerBlockColorHandler(ExoContent.CHALK, ExoContent.CHALK);
    }

    @Override
    public void registerEntities() {
        super.registerEntities();

        // TODO do we really need to specify a render for these?
        RenderingRegistry.registerEntityRenderingHandler(EntityRitual.class, new RenderNothing.RenderNothingFactory());
        RenderingRegistry.registerEntityRenderingHandler(EntityNode.class, new RenderNothing.RenderNothingFactory());
    }

    @Override
    public void registerHandlers() {
        super.registerHandlers();
        MinecraftForge.EVENT_BUS.register(new ParticleRenderer());
    }


    @Override
    protected void registerItem(Item item, String id) {
        super.registerItem(item, id);

        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        List<ItemStack> items = new ArrayList<ItemStack>();
        item.getSubItems(item, null, items);
        for(ItemStack stack : items) {
            mesher.register(item, stack.getMetadata(), loc);
        }
    }
    @Override
    protected void registerBlock(Block block, ItemBlock itemBlock, String id) {
        super.registerBlock(block, itemBlock, id);

        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        Item item = Item.getItemFromBlock(block);
        ModelResourceLocation loc = new ModelResourceLocation(block.getRegistryName(), "inventory");
        List<ItemStack> items = new ArrayList<ItemStack>();
        block.getSubBlocks(item, null, items);
        for(ItemStack stack : items) {
            mesher.register(item, stack.getMetadata(), loc);
        }
    }
}
