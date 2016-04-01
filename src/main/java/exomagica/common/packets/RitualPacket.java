package exomagica.common.packets;

import exomagica.api.ritual.IRitual;
import exomagica.api.ritual.IRitualCore;
import exomagica.api.ritual.IRitualRecipe;
import exomagica.api.ritual.RitualRecipeContainer;
import exomagica.common.handlers.RitualHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
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
            IRitual ritual = RitualHandler.findRitual(core, world, message.pos);
            IRitualRecipe recipe = RitualHandler.findRitualRecipe(ritual, core, world, message.pos);
            RitualRecipeContainer container = ritual.startRitual(recipe, core, world, message.pos, Side.CLIENT);
            RitualHandler.CLIENT_ACTIVE_RITUALS.add(container);

            return null;
        }
    }
}
