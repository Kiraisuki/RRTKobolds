package com.kiraisuki.rrtkobolds.core;

import com.kiraisuki.rrtkobolds.RRTKobolds;
import com.kiraisuki.rrtkobolds.blocks.BlockGoal;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks
{
	@GameRegistry.ObjectHolder(RRTKobolds.MODID + ":block_goal")
	public static BlockGoal blockGoal = new BlockGoal();
}
