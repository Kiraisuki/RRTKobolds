package com.kiraisuki.rrtkobolds.core;

import com.kiraisuki.rrtkobolds.RRTKobolds;
import com.kiraisuki.rrtkobolds.items.ItemKoboldNavigator;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems
{
	@GameRegistry.ObjectHolder(RRTKobolds.MODID + ":item_kobold_navigator")
	public static Item itemKoboldNavigator = new ItemKoboldNavigator();
}
