package exomagica.common.blocks;

import exomagica.ExoContent;
import exomagica.api.ritual.IRitualCore;
import exomagica.common.tiles.TileAltar;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAltar extends Block implements IRitualCore {

    public static final PropertyBool BASE = PropertyBool.create("base");

    private final AxisAlignedBB BASE_BOX = new AxisAlignedBB(0, 0, 0, 1, 2, 1);
    private final AxisAlignedBB TOP_BOX = new AxisAlignedBB(0, -1, 0, 1, 1, 1);

    public BlockAltar() {
        super(Material.rock);
        this.setCreativeTab(ExoContent.TAB);
        this.setUnlocalizedName("altar");
        this.setHardness(1.5F);
        this.setResistance(10);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.getValue(BASE);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if(state.getValue(BASE)) return new TileAltar();
        return null;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BASE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BASE, meta == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BASE) ? 0 : 1;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isTranslucent(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        if(state.getValue(BASE)) return EnumBlockRenderType.MODEL;
        return EnumBlockRenderType.INVISIBLE;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        BlockPos pos2 = pos.up();
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos) &&
                world.getBlockState(pos2).getBlock().isReplaceable(world, pos2);
    }

    @Override
    public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        world.setBlockState(pos.up(), getDefaultState().withProperty(BASE, false));
        return super.onBlockPlaced(world, pos, facing, hitX, hitY, hitZ, meta, placer);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(BASE) ? BASE_BOX : TOP_BOX;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer p, EnumHand hand, ItemStack item, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(world.isRemote) return true;

        if(!state.getValue(BASE)) {
            pos = pos.down();
        }

        TileEntity te = world.getTileEntity(pos);
        if(te == null || !(te instanceof TileAltar)) {
            return false;
        }
        TileAltar altar = (TileAltar)te;
        if(item == null) {
            // Remove item
            item = altar.removeStackFromSlot(0);

            if(item == null) return true;
            if(p.getHeldItem(hand) == null) { // Double check
                p.setHeldItem(hand, item);
            } else {
                if(!p.inventory.addItemStackToInventory(item)) {
                    p.dropItem(item, true, false);
                }
            }

        } else if(altar.getStackInSlot(0) == null) {
            // Add item
            if(hand == EnumHand.MAIN_HAND) {
                altar.setInventorySlotContents(0, p.inventory.decrStackSize(p.inventory.currentItem, 1));
            } else {
                int slot = p.inventory.getSlotFor(item);
                if(slot != -1) altar.setInventorySlotContents(0, p.inventory.decrStackSize(slot, 1));
            }
        } else {
            // Swap items
            ItemStack i = altar.removeStackFromSlot(0);

            if(hand == EnumHand.MAIN_HAND) {
                altar.setInventorySlotContents(0, p.inventory.decrStackSize(p.inventory.currentItem, 1));
            } else {
                altar.setInventorySlotContents(0, p.inventory.decrStackSize(p.inventory.getSlotFor(item), 1));
            }

            if(i == null) return true;
            if(p.getHeldItem(hand) == null) {
                p.setHeldItem(hand, i);
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
        TileEntity te = world.getTileEntity(state.getValue(BASE) ? pos : pos.down());

        if(te != null && te instanceof TileAltar) {
            ItemStack item = ((TileAltar)te).removeStackFromSlot(0);
            if(item != null) {
                world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), item));
            }
        }

        if(state.getValue(BASE)) {
            world.setBlockState(pos.up(), Blocks.air.getDefaultState());
        } else {
            world.setBlockState(pos.down(), Blocks.air.getDefaultState());
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean isRitualCore(IBlockAccess world, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public BlockPos getRitualCorePosition(IBlockAccess world, BlockPos pos, IBlockState state) {
        if(state.getValue(BASE)) return pos;
        return pos.down();
    }

    @Override
    public IInventory getInventory(IBlockAccess world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        return te != null && te instanceof TileAltar ? (TileAltar)te : null;
    }

}
