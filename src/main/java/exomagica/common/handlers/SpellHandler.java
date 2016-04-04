package exomagica.common.handlers;

import exomagica.api.spells.IItemSpell;
import exomagica.api.spells.SpellType;
import exomagica.common.capabilities.CapabilityProvider;
import exomagica.common.capabilities.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class SpellHandler {

    @SubscribeEvent
    public void tick(PlayerTickEvent event) {
        if(event.phase != Phase.END) return;
        if(event.side != Side.SERVER) return;
        IPlayerData data = event.player.getCapability(CapabilityProvider.PLAYER_DATA, null);
        Item item = data.getHoldItem();
        if(item != null) {
            int holdTicks = data.getHoldTicks() + 1;
            data.setHoldTicks(holdTicks);
            System.out.println("TICK: " + holdTicks);
            if(holdTicks > 10) {
                System.out.println("STOP");
                data.setHoldItem(null);
                //event.player.getCooldownTracker().setCooldown(item, ((IItemSpell)item).getCooldown());
            }
        }
    }

    @SubscribeEvent
    public void interact(RightClickItem event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        if(item instanceof IItemSpell) {

            IItemSpell spell = (IItemSpell)item;
            EntityPlayer player = event.getEntityPlayer();

            if(player.getCooldownTracker().hasCooldown(item)) return;
            // Cast item spell
            spell.cast(player, stack);

            if(spell.getType() == SpellType.CLICK) {
                player.getCooldownTracker().setCooldown(item, spell.getCooldown());
            } else if(spell.getType() == SpellType.HOLD) {
                IPlayerData data = player.getCapability(CapabilityProvider.PLAYER_DATA, null);
                data.setHoldItem(item);
                data.setHoldTicks(0);
                System.out.println("RENEW");
            }

        }
    }

    @SubscribeEvent
    public void interact(RightClickEmpty event) {
        // Cast hand spell
    }

}
