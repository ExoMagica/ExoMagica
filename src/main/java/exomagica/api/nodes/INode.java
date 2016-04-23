package exomagica.api.nodes;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public interface INode {

    IAura getAura();

    Vec3d getPos();

    World getWorld();

}
