package exomagica;

import exomagica.api.IExoMagicaAPI;
import exomagica.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = ExoMagica.MODID, version = ExoMagica.VERSION)
public class ExoMagica {

    public static final String MODID = "exomagica";
    public static final String VERSION = "1.0.0";

    public static final Logger LOG = LogManager.getLogger(MODID);

    public static final IExoMagicaAPI API = new ExoMagicaAPI(MODID);

    @SidedProxy(clientSide = "exomagica.client.ClientProxy", serverSide = "exomagica.common.CommonProxy")
    private static CommonProxy PROXY;

    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(MODID);

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
        LOG.debug("Registering Packets...");
        PROXY.registerPackets(NETWORK);
        LOG.debug("Registering Rituals...");
        PROXY.registerRituals(API);
        LOG.debug("Registering Recipes...");
        PROXY.registerRecipes(API);
        LOG.info("ExoMagica components are now loaded.");
    }

    @EventHandler
    public void receiveIMC(IMCEvent event) {
        for(IMCMessage message : event.getMessages()) {
            ExoMagicaAPI.processIMC(message);
        }
    }

}
