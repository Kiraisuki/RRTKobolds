package com.kiraisuki.rrtkobolds.blocks;

import com.kiraisuki.rrtkobolds.RRTKobolds;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGoal extends Block
{
	private volatile ItemBlock itemBlock = null;

	public BlockGoal()
	{
		super(Material.GROUND);
		
		this.setRegistryName("block_goal");
		this.setUnlocalizedName("block_goal");
		this.setCreativeTab(RRTKobolds.TAB);
		
		this.setHardness(1.0f);
		this.setResistance(2.0f);
		
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.SOLID;
	}
	
	public ItemBlock getItemBlock()
	{
		if(itemBlock == null)
		{
			itemBlock = new ItemBlock(this);
			itemBlock.setRegistryName(this.getRegistryName());
		}
		
		return itemBlock;
	}
}
