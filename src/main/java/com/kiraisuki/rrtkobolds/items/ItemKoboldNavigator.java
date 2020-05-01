package com.kiraisuki.rrtkobolds.items;

import com.kiraisuki.rrtkobolds.RRTKobolds;
import com.kiraisuki.rrtkobolds.entities.EntityKobold;
import com.kiraisuki.rrtkobolds.entities.ai.PathFinderRRT;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

/**
 * A familiar looking little device to dictate destinations to draconic beings
 * @author kiraisuki
 *
 */
public class ItemKoboldNavigator extends Item
{
	private EntityKobold kobold;
	
	public ItemKoboldNavigator()
	{
		super();
		
		this.setRegistryName("item_kobold_navigator");
		this.setUnlocalizedName("item_kobold_navigator");
		this.setCreativeTab(RRTKobolds.TAB);
		this.setMaxStackSize(1);
	}
	
	public void setKobold(EntityPlayer player, EntityKobold k)
	{
		kobold = k;
		player.sendStatusMessage(new TextComponentTranslation("kobold.selected", k.getEntityId()), true);
	}
	
	/**
     * This is called when the item is used, before the block is activated.
     * @param stack The Item Stack
     * @param player The Player that used the item
     * @param world The Current World
     * @param pos Target position
     * @param side The side of the target hit
     * @param hand Which hand the item is being held in.
     * @return Return PASS to allow vanilla handling, any other to skip normal code.
     */
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		if(kobold == null)
		{
			player.sendStatusMessage(new TextComponentTranslation("kobold.selected.error"), true);
			return EnumActionResult.PASS;
		}
		
		int distance = MathHelper.abs(kobold.getPosition().getX() - pos.getX()) + MathHelper.abs(kobold.getPosition().getZ() - pos.getZ());
		
		if(distance > PathFinderRRT.MAX_RANGE)
			player.sendStatusMessage(new TextComponentTranslation("kobold.selected.destination.outofrange", PathFinderRRT.MAX_RANGE, distance), true);
			
		else if(kobold.isEntityAlive())
		{
			if(kobold.moveToBlockRRT.destination != null)
			{
				player.sendStatusMessage(new TextComponentTranslation("kobold.selected.busy", kobold.getEntityId(), pos.getX(), pos.getY(), pos.getZ()), true);
				return EnumActionResult.PASS;
			}
			
			else
			{
				kobold.setDestination(pos);
			}
		}
		
		else if(kobold.isDead)
		{
			player.sendStatusMessage(new TextComponentTranslation("kobold.selected.dead", kobold.getEntityId()), true);
		}

        return EnumActionResult.PASS;
    }
}
