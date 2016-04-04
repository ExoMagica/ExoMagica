package exomagica.common.capabilities;

import java.util.concurrent.Callable;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author Guilherme Chaguri
 */
public class PlayerData implements IPlayerData {

    private int ticksHolding = 0;
    private Item itemHolding = null;

    @Override
    public int getHoldTicks() {
        return ticksHolding;
    }

    @Override
    public void setHoldTicks(int ticks) {
        ticksHolding = ticks;
    }

    @Override
    public void setHoldItem(Item item) {
        itemHolding = item;
    }

    @Override
    public Item getHoldItem() {
        return itemHolding;
    }


    public static class PlayerDataStorage implements IStorage<IPlayerData> {

        @Override
        public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
            return null; // No need to save anything yet
        }

        @Override
        public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {

        }

    }

    public static class PlayerDataFactory implements Callable<IPlayerData> {

        @Override
        public IPlayerData call() throws Exception {
            return new PlayerData();
        }

    }

    public static class PlayerDataProvider implements ICapabilityProvider {

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return true;
        }
        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability.getDefaultInstance();
        }
    }

}
