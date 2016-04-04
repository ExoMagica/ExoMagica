package exomagica.common.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityProvider implements ICapabilityProvider {

    @CapabilityInject(IPlayerData.class)
    public static Capability<IPlayerData> PLAYER_DATA = null;

    private IPlayerData data = null;

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == PLAYER_DATA;
    }
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability != PLAYER_DATA) return null;
        if(data == null) data = (IPlayerData)capability.getDefaultInstance();
        return (T)data;
    }

}
