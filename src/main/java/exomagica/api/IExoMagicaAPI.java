package exomagica.api;

import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualRecipe;

public interface IExoMagicaAPI {

    void registerRitual(IRitual ritual, String name);

    void registerRecipe(IRitual ritual, IRitualRecipe recipe);

    void registerRecipe(String ritualName, IRitualRecipe recipe);

    IRitual getRitual(String ritual);

}
