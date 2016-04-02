package exomagica.common;

import exomagica.ExoContent;
import exomagica.api.IExoMagicaAPI;
import exomagica.common.handlers.RitualHandler;
import exomagica.common.handlers.SpellHandler;
import exomagica.common.items.ItemExoBlock;
import exomagica.common.packets.RitualPacket;
import exomagica.common.packets.RitualPacket.RitualPacketHandler;
import exomagica.common.rituals.RitualBasic;
import exomagica.common.rituals.RitualBasic.RitualBasicRecipe;
import exomagica.common.tiles.TileAltar;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

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

    public void registerPackets(SimpleNetworkWrapper network) {
        network.registerMessage(new RitualPacketHandler(), RitualPacket.class, 0, Side.CLIENT);
    }

    public void registerRituals(IExoMagicaAPI api) {
        api.registerRitual(new RitualBasic(), "basic");
    }

    public void registerRecipes(IExoMagicaAPI api) {
        api.registerRecipe("basic", new RitualBasicRecipe(new ItemStack(Items.elytra), Items.diamond,
                Items.flint_and_steel, FluidRegistry.LAVA, Items.coal, Blocks.torch));
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
