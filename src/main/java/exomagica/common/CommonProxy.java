package exomagica.common;

import exomagica.ExoContent;
import exomagica.common.handlers.RitualHandler;
import exomagica.common.handlers.SpellHandler;
import exomagica.common.items.ItemExoBlock;
import exomagica.common.tiles.TileAltar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void prepare() {

    }

    public void registerItems() {
        registerItem(ExoContent.TEST, "test");
        registerItem(ExoContent.SCROLL, "scroll");
    }

    public void registerBlocks() {
        registerBlock(ExoContent.CHALK, ItemExoBlock.class, "chalk");
        registerBlock(ExoContent.ALTAR, "altar");

        registerTileEntity(TileAltar.class, "TileAltar");
    }

    public void registerEntities() {

    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new SpellHandler());
        MinecraftForge.EVENT_BUS.register(new RitualHandler());
    }



    protected void registerItem(Item item, String id) {
        GameRegistry.registerItem(item, id);
    }

    protected void registerBlock(Block block, String id) {
        registerBlock(block, ItemExoBlock.class, id);
    }

    protected void registerBlock(Block block, Class<? extends ItemBlock> itemClass, String id) {
        GameRegistry.registerBlock(block, itemClass, id);
    }

    protected void registerTileEntity(Class<? extends TileEntity> tileClass, String id) {
        GameRegistry.registerTileEntity(tileClass, id);
    }

}
