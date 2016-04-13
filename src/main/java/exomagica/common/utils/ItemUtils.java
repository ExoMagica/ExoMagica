package exomagica.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author Guilherme Chaguri
 */
public class ItemUtils {

    public static boolean itemsMatches(List<Object> targets, List<ItemStack> inputs) {
        targetLoop: for(Object target : targets) {
            Class type = getTargetType(target);
            for(ItemStack in : inputs) {
                if(itemMatches(target, in, type)) continue targetLoop;
            }
            return false;
        }
        return true;
    }

    public static boolean itemMatches(Object target, ItemStack input) {
        if(input == null || target == null) return false;

        Class type = getTargetType(target);

        return itemMatches(target, input, type);
    }

    public static Class getTargetType(Object target) {
        if(target instanceof ItemStack) { // ItemStack
            return ItemStack.class;
        } else if(target instanceof String) { // OreDictionary
            return String.class;
        } else if(target instanceof Item) { // Item
            return Item.class;
        } else if(target instanceof Block) { // Block
            return Block.class;
        } else if(target instanceof Fluid) { // Fluid
            return Fluid.class;
        } else if(target instanceof FluidStack) { // FluidStack
            return FluidStack.class;
        } else if(target instanceof List) { // List
            return List.class;
        } else if(target instanceof Object[]) { // Array
            return Object[].class;
        }
        return target.getClass();
    }

    public static boolean itemMatches(Object target, ItemStack input, Class targetType) {
        if(input == null || target == null || targetType == null) return false;

        if(targetType == ItemStack.class) { // ItemStack

            return OreDictionary.itemMatches((ItemStack)target, input, false);

        } else if(targetType == String.class) { // OreDictionary

            for(ItemStack stack : OreDictionary.getOres((String) target)) {
                if(OreDictionary.itemMatches(stack, input, false)) return true;
            }

        } else if(targetType == Item.class) { // Item

            return input.getItem() == target;

        } else if(targetType == Block.class) { // Block

            if(input.getItem() instanceof ItemBlock) {
                return ((ItemBlock)input.getItem()).getBlock() == target;
            }

        } else if(targetType == Fluid.class) { // Fluid

            Item item = input.getItem();
            if(item instanceof IFluidContainerItem) {
                return ((IFluidContainerItem)item).getFluid(input).getFluid() == target;
            } else if(FluidContainerRegistry.isContainer(input)) {
                // TODO remove this deprecated stuff when vanilla containers implement IFluidContainerItem
                return FluidContainerRegistry.getFluidForFilledItem(input).getFluid() == target;
            }

        } else if(targetType == FluidStack.class) { // FluidStack

            return ((FluidStack)target).isFluidEqual(input);

        } else if(targetType == List.class) { // List

            for(Object t : (List)target) {
                if(itemMatches(t, input)) return true;
            }

        } else if(targetType == Object[].class) { // Array

            for(Object t : (Object[])target) {
                if(itemMatches(t, input)) return true;
            }

        }
        return false;
    }

    public static String toString(List<Object> items) {
        List<String> names = new ArrayList<String>();
        for(Object item : items) {
            names.add(toString(item));
        }
        Collections.sort(names);
        return "[" + Joiner.on(',').join(names) + "]";
    }

    public static String toString(Object item) {
        Class type = getTargetType(item);

        if(type == ItemStack.class) {
            return item.toString();
        } else if(type == Item.class) {
            return ((Item)item).getRegistryName().toString();
        } else if(type == String.class) {
            return "oredict_" + (String)item;
        } else if(type == Block.class) {
            return ((Block)item).getRegistryName().toString();
        } else if(type == Fluid.class) {
            return ((Fluid)item).getUnlocalizedName();
        } else if(type == FluidStack.class) {
            return ((FluidStack)item).getUnlocalizedName();
        } else if(type == List.class) {
            return toString((List)item);
        } else if(type == Object[].class) {
            return toString(Lists.newArrayList((Object[])item));
        }

        return item.toString();
    }


}
