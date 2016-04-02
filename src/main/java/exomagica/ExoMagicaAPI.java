package exomagica;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import exomagica.api.IExoMagicaAPI;
import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.common.handlers.RitualHandler;
import java.lang.reflect.Method;
import net.minecraftforge.fml.common.event.FMLInterModComms.IMCMessage;

/**
 * @author Guilherme Chaguri
 */
public class ExoMagicaAPI implements IExoMagicaAPI {

    protected static void processIMC(IMCMessage message) {
        if(message.key.equalsIgnoreCase("API") || message.key.equalsIgnoreCase("getAPI")) {
            createAPIHandler(message);
        } else {
            ExoMagica.LOG.debug("{} sent an unknown key: {}", message.getSender(), message.key);
        }
    }

    private static void createAPIHandler(IMCMessage message) {
        String modid = message.getSender();
        ExoMagica.LOG.debug("{} requested an API instance", modid);

        if(message.isFunctionMessage()) {

            Optional<Function<IExoMagicaAPI, Void>> value = message.getFunctionValue(IExoMagicaAPI.class, Void.class);

            if(value.isPresent()) {
                value.get().apply(new ExoMagicaAPI(modid));
            } else {
                ExoMagica.LOG.warn("{} requested an API instance without a function", modid);
            }

        } else if(message.isStringMessage()) {

            String value = message.getStringValue();
            int v = value.lastIndexOf('.');
            if(v != -1) {
                String className = value.substring(0, v);
                String methodName = value.substring(v + 1);

                try {
                    Class c = Class.forName(className);
                    Method m = c.getDeclaredMethod(methodName, IExoMagicaAPI.class);
                    m.setAccessible(true);
                    m.invoke(null, new ExoMagicaAPI(modid));
                } catch(Exception e) {
                    ExoMagica.LOG.warn("{} requested an API instance with an invalid method path: {}", modid, e.getMessage());
                }

            } else {
                ExoMagica.LOG.warn("{} requested an API instance with an invalid method path", modid);
            }

        } else {
            ExoMagica.LOG.warn("{} requested an API instance with an unknown value", modid);
        }

    }


    private final String MODID;

    protected ExoMagicaAPI(String modid) {
        MODID = modid;
    }

    @Override
    public void registerRitual(IRitual ritual, String name) {
        RitualHandler.registerRitual(ritual, name);
    }

    @Override
    public void registerRecipe(IRitual ritual, IRitualRecipe recipe) {
        RitualHandler.registerRecipe(ritual, recipe);
    }

    @Override
    public void registerRecipe(String ritualName, IRitualRecipe recipe) {
        RitualHandler.registerRecipe(ritualName, recipe);
    }

    @Override
    public IRitual getRitual(String ritual) {
        return RitualHandler.getRitual(ritual);
    }

    @Override
    public String toString() {
        return "ExoMagicaAPI@" + MODID;
    }

}
