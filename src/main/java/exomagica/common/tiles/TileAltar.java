package exomagica.common.tiles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class TileAltar extends TileEntity implements IInventory {

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
        if(stack != null) stack.writeToNBT(item);
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
        if(stack == null) return null;
        this.markDirty();
        return stack.splitStack(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(index != 0) return null;
        if(stack == null) return null;
        ItemStack i = stack;
        stack = null;
        this.markDirty();
        return i;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if(index != 0) return;
        this.stack = stack;
        this.markDirty();
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
        if(this.stack != null) return false;
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

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
}
