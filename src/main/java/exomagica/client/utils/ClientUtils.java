package exomagica.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ClientUtils {

    public static void playSound(SoundEvent sound, SoundCategory cat, BlockPos pos) {
        if(sound == null) return;
        Minecraft.getMinecraft().getSoundHandler().playSound(new PositionedSoundRecord(sound, cat, 1, 1, pos));
    }

}
