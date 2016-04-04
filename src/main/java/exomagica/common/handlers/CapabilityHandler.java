package exomagica.common.handlers;

import exomagica.ExoMagica;
import exomagica.common.capabilities.CapabilityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

    private final ResourceLocation PLAYER_DATA_NAME = new ResourceLocation(ExoMagica.MODID, "PlayerData");
    private final CapabilityProvider PROVIDER = new CapabilityProvider();

    @SubscribeEvent
    public void attach(AttachCapabilitiesEvent.Entity event) {
        if(event.getEntity() instanceof EntityPlayer) {
            event.addCapability(PLAYER_DATA_NAME, PROVIDER);
        }
    }

}