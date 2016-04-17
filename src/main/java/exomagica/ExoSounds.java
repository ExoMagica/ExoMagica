package exomagica;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class ExoSounds {

    /* CHALK */
    public static final SoundEvent CHALK_PLACE = createSound("block.chalk.place");

    /* RITUAL */
    public static final SoundEvent RITUAL_START = createSound("ritual.start");
    public static final SoundEvent RITUAL_END = createSound("ritual.end");
    public static final SoundEvent RITUAL_CANCEL = createSound("ritual.cancel");
    public static final SoundEvent RITUAL_LOOP = createSound("ritual.loop");


    private static SoundEvent createSound(String name) {
        ResourceLocation loc = new ResourceLocation(ExoMagica.MODID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

}
