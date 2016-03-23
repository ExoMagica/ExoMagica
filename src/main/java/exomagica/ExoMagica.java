package exomagica;

import exomagica.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExoMagica.MODID, version = ExoMagica.VERSION)
public class ExoMagica {

    public static final String MODID = "exomagica";
    public static final String VERSION = "1.0.0";

    public static final Logger LOG = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "exomagica.client.ClientProxy", serverSide = "exomagica.common.CommonProxy")
    private static CommonProxy PROXY;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOG.info("Loading ExoMagica components...");
        LOG.debug("Preparing...");
        PROXY.prepare();
        LOG.debug("Registering Items...");
        PROXY.registerItems();
        LOG.debug("Registering Blocks...");
        PROXY.registerBlocks();
        LOG.debug("Registering Entities...");
        PROXY.registerEntities();
        LOG.debug("Registering Handlers...");
        PROXY.registerHandlers();
        LOG.info("ExoMagica components are now loaded.");
    }

}
