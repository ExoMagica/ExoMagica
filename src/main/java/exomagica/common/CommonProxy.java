package exomagica.common;

import exomagica.ExoContent;
import exomagica.common.handlers.SpellHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {

    public void prepare() {

    }

    public void registerItems() {
        GameRegistry.registerItem(ExoContent.TEST, "test");
        GameRegistry.registerItem(ExoContent.SCROLL, "scroll");
    }

    public void registerBlocks() {

    }

    public void registerEntities() {

    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new SpellHandler());
    }

}
