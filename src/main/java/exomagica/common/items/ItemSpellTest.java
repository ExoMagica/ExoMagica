package exomagica.common.items;

import exomagica.ExoContent;
import exomagica.ExoMagica;
import exomagica.api.spells.IItemSpell;
import exomagica.api.spells.SpellType;
import exomagica.client.particles.CubeFX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSpellTest extends Item implements IItemSpell {

    @SideOnly(Side.CLIENT)
    private final ModelResourceLocation MODEL = new ModelResourceLocation(new ResourceLocation(ExoMagica.MODID, "models/items/scrol2l.json"), "inventory");

    public ItemSpellTest() {
        setCreativeTab(ExoContent.TAB);
    }

    @Override
    public void cast(EntityPlayer player, ItemStack stack) {
        World w = player.getEntityWorld();
        if(w.isRemote) {
            System.out.println("PARTICLE");
            //ColorfulFX particle = new ColorfulFX(w, player.posX + 1, player.posY, player.posZ + 1, !player.isSneaking());
            CubeFX particle = new CubeFX(w, player.posX + 1, player.posY, player.posZ + 1);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
        }
        //player.addVelocity(0, 1, 0);
    }

    @Override
    public SpellType getType() {
        return SpellType.HOLD;
    }

}
