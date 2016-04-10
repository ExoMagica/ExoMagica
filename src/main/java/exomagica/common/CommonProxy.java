package exomagica.common;

import exomagica.ExoContent;
import exomagica.ExoMagica;
import exomagica.api.IExoMagicaAPI;
import exomagica.common.capabilities.IPlayerData;
import exomagica.common.capabilities.PlayerData.PlayerDataFactory;
import exomagica.common.capabilities.PlayerData.PlayerDataStorage;
import exomagica.common.entities.EntityRitual;
import exomagica.common.handlers.CapabilityHandler;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import scala.reflect.api.Internals;

public class CommonProxy {

    public void prepare() {
        CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerDataStorage(), new PlayerDataFactory());
    }

    public void registerItems() {
        registerItem(ExoContent.TEST, "test");
        registerItem(ExoContent.SCROLL, "scroll");
    }

    public void registerBlocks() {
        registerBlock(ExoContent.CHALK, new ItemExoBlock(ExoContent.CHALK), "chalk");
        registerBlock(ExoContent.ALTAR, "altar");

        registerTileEntity(TileAltar.class, "TileAltar");
    }

    public void registerEntities() {
        EntityRegistry.registerModEntity(EntityRitual.class, "ritual", 0, ExoMagica.MODID, 48, 1, false);
    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new SpellHandler());
        MinecraftForge.EVENT_BUS.register(new RitualHandler());
        MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
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
        GameRegistry.register(item, new ResourceLocation(ExoMagica.MODID, id));
    }

    protected void registerBlock(Block block, String id) {
        registerBlock(block, new ItemExoBlock(block), id);
    }

    protected void registerBlock(Block block, ItemBlock itemBlock, String id) {
        ResourceLocation r = new ResourceLocation(ExoMagica.MODID, id);
        GameRegistry.register(block, r);
        GameRegistry.register(itemBlock, r);
    }

    protected void registerTileEntity(Class<? extends TileEntity> tileClass, String id) {
        GameRegistry.registerTileEntity(tileClass, id);
    }

}
