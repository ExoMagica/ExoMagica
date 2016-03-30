package exomagica.common.handlers;

import exomagica.api.spells.IItemSpell;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EmptyRightClick;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.ItemRightClick;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellHandler {

    @SubscribeEvent
    public void interact(ItemRightClick event) {
        ItemStack stack = event.getItemStack();
        if(stack.getItem() instanceof IItemSpell) {

            // Cast item spell
            ((IItemSpell)stack.getItem()).cast(event.getEntityPlayer(), stack);

        }
    }

    @SubscribeEvent
    public void interact(EmptyRightClick event) {
        // Cast hand spell
    }

}
