package exomagica.common.blocks;

import exomagica.ExoContent;
import exomagica.common.tiles.TileAltar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockAltar extends Block {

    public BlockAltar() {
        super(Material.rock);
        this.setCreativeTab(ExoContent.TAB);
        this.setUnlocalizedName("altar");
        this.setRegistryName("altar");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileAltar();
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isBlockNormalCube() {
        return false;
    }

    @Override
    public boolean isTranslucent() {
        return true;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumFacing side, float hitX, float hitY, float hitZ) {
        //if(world.isRemote) return true; FIXME add client packets

        TileEntity te = world.getTileEntity(pos);
        if(te == null || !(te instanceof TileAltar)) {
            return false;
        }
        TileAltar altar = (TileAltar)te;
        int currentItem = p.inventory.currentItem;
        ItemStack item = p.inventory.getStackInSlot(currentItem);
        if(item == null) {
            // Remove item
            item = altar.removeStackFromSlot(0);

            if(item == null) return true;
            if(p.inventory.getStackInSlot(currentItem) == null) { // Double check
                p.inventory.setInventorySlotContents(currentItem, item);
            } else {
                if(!p.inventory.addItemStackToInventory(item)) {
                    p.dropItem(item, true, false);
                }
            }

        } else if(altar.isItemValidForSlot(0, item)) {
            // Add item
            altar.setInventorySlotContents(0, p.inventory.decrStackSize(currentItem, 1));
        } else {
            // Swap items
            ItemStack i = altar.removeStackFromSlot(0);
            altar.setInventorySlotContents(0, p.inventory.decrStackSize(currentItem, 1));

            if(i == null) return true;
            if(p.inventory.getStackInSlot(currentItem) == null) {
                p.inventory.setInventorySlotContents(currentItem, i);
            } else {
                if(!p.inventory.addItemStackToInventory(i)) {
                    p.dropItem(i, true, false);
                }
            }
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if(!world.isRemote && te != null && te instanceof TileAltar) {
            ItemStack item = ((TileAltar)te).removeStackFromSlot(0);
            world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), item));
        }
        super.breakBlock(world, pos, state);
    }
}
