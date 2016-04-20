package exomagica.client.sounds;

import exomagica.common.entities.EntityRitual;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class SoundRitualLoop extends PositionedSound implements ITickableSound {
    private final EntityRitual ritual;
    private int ticks = 0;

    public SoundRitualLoop(EntityRitual ritual, SoundEvent sound) {
        super(sound, SoundCategory.AMBIENT);
        this.ritual = ritual;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    @Override
    public boolean isDonePlaying() {
        return !ritual.isEntityAlive();
    }

    @Override
    public void update() {
        this.xPosF = (float)ritual.posX;
        this.yPosF = (float)ritual.posY;
        this.zPosF = (float)ritual.posZ;
        ticks++;
        if(ticks < 10) this.volume = ticks / 10F;
    }
}
