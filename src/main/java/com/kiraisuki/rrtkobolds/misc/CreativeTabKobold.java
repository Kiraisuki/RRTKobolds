package com.kiraisuki.rrtkobolds.misc;

import com.kiraisuki.rrtkobolds.core.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabKobold extends CreativeTabs
{

	public CreativeTabKobold(String label)
	{
		super(label);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(ModItems.itemKoboldNavigator);
	}

	@Override
	public boolean hasSearchBar() 
	{
		return false;
	}
}
