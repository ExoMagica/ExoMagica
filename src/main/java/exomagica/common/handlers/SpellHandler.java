package exomagica.common.handlers;

import exomagica.api.spells.IItemSpell;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellHandler {

    @SubscribeEvent
    public void interact(PlayerInteractEvent event) {
        if(event.action != Action.LEFT_CLICK_BLOCK) {
            ItemStack stack = event.entityPlayer.getActiveItemStack();
            if(stack == null) {

                // Check for hand spells

            } else if(stack.getItem() instanceof IItemSpell) {

                // Cast item spell
                ((IItemSpell)stack.getItem()).cast(event.entityPlayer, stack);

            }
        }
    }



}
