package exomagica.common.handlers;

import com.google.common.collect.HashBiMap;
import exomagica.api.nodes.IAura;

public class NodeHandler {

    public final static HashBiMap<String, IAura> NAMED_AURAS = HashBiMap.create();

    public static void registerAura(IAura aura, String name) {
        NAMED_AURAS.put(name, aura);
    }

    public static IAura findAura(String name) {
        return NAMED_AURAS.get(name);
    }

    public static String getAuraName(IAura aura) {
        return NAMED_AURAS.inverse().get(aura);
    }

}
