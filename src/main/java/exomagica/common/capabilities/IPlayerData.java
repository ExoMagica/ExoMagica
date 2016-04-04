package exomagica.common.capabilities;

import net.minecraft.item.Item;

public interface IPlayerData {

    int getHoldTicks();

    void setHoldTicks(int ticks);

    void setHoldItem(Item item);

    Item getHoldItem();

}
