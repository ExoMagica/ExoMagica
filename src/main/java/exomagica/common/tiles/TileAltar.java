package exomagica.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;

public class TileAltar extends TileEntity implements ISidedInventory {

    private ItemStack stack;

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("AltarItem"));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        NBTTagCompound item = new NBTTagCompound();
        if(stack != null && stack.stackSize > 0) stack.writeToNBT(item);
        compound.setTag("AltarItem", item);
    }

    @Override
    public boolean hasFastRenderer() {
        return super.hasFastRenderer();
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if(index != 0) return null;
        return stack;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(index != 0) return null;
        ItemStack r = stack.splitStack(count);
        if(stack.stackSize <= 0) stack = null;
        this.markUpdate();
        return r;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index != 0) return null;
        if(stack == null) return null;
        ItemStack i = stack;
        stack = null;
        this.markUpdate();
        return i.stackSize == 0 ? null : i;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(index != 0) return;
        if(stack != null && stack.stackSize == 0) stack = null;
        this.stack = stack;
        this.markUpdate();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if(index != 0) return false;
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        stack = null;
    }

    @Override
    public String getName() {
        return "altar";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(pos, 0, nbt);
    }

    private void markUpdate() {
        IBlockState state = worldObj.getBlockState(pos);
        worldObj.notifyBlockUpdate(pos, state, state, 2);
        markDirty();
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if(side == EnumFacing.DOWN) {
            return new int[]{0};
        }
        return new int[0];
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return direction == EnumFacing.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return direction == EnumFacing.DOWN;
    }
}
