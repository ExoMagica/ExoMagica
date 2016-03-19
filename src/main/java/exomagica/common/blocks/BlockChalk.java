package exomagica.common.blocks;

import exomagica.ExoContent;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockChalk extends Block {

    public static final PropertyEnum<ChalkType> TYPE = PropertyEnum.create("type", ChalkType.class);

    public BlockChalk() {
        super(Material.circuits);
        this.setRegistryName("chalk");
        this.setUnlocalizedName("chalk");
        this.setBlockBounds(0F, 0.0F, 0F, 1F, 0.0625F, 1F);
        this.setLightOpacity(0);
        this.setHardness(0);
        this.setCreativeTab(ExoContent.TAB);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(TYPE, ChalkType.REGULAR));
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) ? super.canPlaceBlockAt(worldIn, pos) : false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos) {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        ChalkType[] values = ChalkType.values();
        if(values.length <= meta) return super.getStateFromMeta(meta);
        return this.getDefaultState().withProperty(TYPE, values[meta]);
    }

    @Override
    public boolean isOpaqueCube() {
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
    public boolean isVisuallyOpaque() {
        return false;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return side.getAxis() != EnumFacing.Axis.Y;
    }

    @Override
    public int getRenderColor(IBlockState state) {
        if(state.getValue(TYPE) == ChalkType.SPECIAL) {
            /*long time = Minecraft.getSystemTime() / 50;
            int r = (int)(Math.sin(0.02 * time + 0) * 127 + 128);
            int g = (int)(Math.sin(0.02 * time + 2) * 127 + 128);
            int b = (int)(Math.sin(0.02 * time + 4) * 127 + 128);
            return (r << 16) + (g << 8) + b;*/
            int r = (int)(Math.random() * 191) + 64;
            int g = (int)(Math.random() * 191) + 64;
            int b = (int)(Math.random() * 191) + 64;
            return (r << 16) + (g << 8) + b;
        }
        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass) {
        return getRenderColor(world.getBlockState(pos));
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    enum ChalkType implements IStringSerializable {

        REGULAR("regular"), SPECIAL("special");

        private final String name;
        ChalkType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

}
