package exomagica.common.packets;

import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.handlers.RitualHandler;
import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author Guilherme Chaguri
 */
public class RitualPacket implements IMessage {
    /**
     * 0: Started
     * 1: Cancelled
     * 2: Finished (Success)
     */
    public byte type = 0;
    public BlockPos pos;

    public RitualPacket() {

    }

    public RitualPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class RitualPacketHandler implements IMessageHandler<RitualPacket, IMessage> {

        @Override
        public IMessage onMessage(RitualPacket message, MessageContext ctx) {
            if(ctx.side != Side.CLIENT) return null;

            World world = Minecraft.getMinecraft().theWorld;

            IRitualCore core = RitualHandler.getCore(world, message.pos);
            if(core == null) return null;

            IRitual ritual = RitualHandler.findRitual(core, world, message.pos);
            if(ritual == null) return null;

            Map<String, List<IInventory>> inventories = ritual.getInventories(core, world, message.pos);
            if(inventories.isEmpty()) return null;

            List<IRitualRecipe> recipes = RitualHandler.RITUALS_RECIPES.get(ritual);
            IRitualRecipe recipe = RitualHandler.findRitualRecipe(ritual, recipes, inventories);
            if(recipe == null) return null;

            int ticks = recipe.getDuration(ritual);
            if(ticks == -1) ticks = ritual.getDuration(recipe, core, world, message.pos);

            RitualRecipeContainer container = ritual.createContainer(recipe, core, world, message.pos, ticks, inventories, Side.CLIENT);
            if(container == null) return null;
            RitualHandler.CLIENT_ACTIVE_RITUALS.add(container);

            return null;
        }
    }
}
