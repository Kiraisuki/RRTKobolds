package com.kiraisuki.rrtkobolds.entities.ai;

import com.kiraisuki.rrtkobolds.RRTKobolds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIDoorInteract;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class EntityAIOpenDoorRRT extends EntityAIOpenDoor
{
	private final IBlockAccess blockAccess;

	public EntityAIOpenDoorRRT(EntityLiving entityIn, boolean closeDoor)
	{
		super(entityIn, closeDoor);
		
		blockAccess = entityIn.world;
	}
	
	private boolean isDoor(BlockPos pos)
	{
		return blockAccess.getBlockState(pos).getBlock() instanceof BlockDoor;
	}


	/**
     * Returns whether the EntityAIBase should begin execution.
     */
	@Override
    public boolean shouldExecute()
    {
        if (!this.entity.collidedHorizontally)
        {
            return false;
        }
        else
        {
        	BlockPos doorPos = new BlockPos(entity.posX, entity.posY, entity.posZ);
        	//Entity standing in door
        	if(isDoor(doorPos))
        	{
        		doorPosition = doorPos;
        		doorBlock = getBlockDoor(doorPos);
        		return true;
        	}
        	
        	doorPos = new BlockPos(entity.posX + 1, entity.posY, entity.posZ);
        	
        	//Door to the left
        	if(isDoor(doorPos))
        	{
        		doorPosition = doorPos;
        		doorBlock = getBlockDoor(doorPos);
        		return true;
        	}
        	
        	doorPos = new BlockPos(entity.posX - 1, entity.posY, entity.posZ);
        	
        	//Door to the right
        	if(isDoor(doorPos))
        	{
        		doorPosition = doorPos;
        		doorBlock = getBlockDoor(doorPos);
        		return true;
        	}
        	
        	doorPos = new BlockPos(entity.posX, entity.posY, entity.posZ - 1);
        	
        	//Door to the front
        	if(isDoor(doorPos))
        	{
        		doorPosition = doorPos;
        		doorBlock = getBlockDoor(doorPos);
        		return true;
        	}
        	
        	doorPos = new BlockPos(entity.posX, entity.posY, entity.posZ + 1);
        	
        	//Door to the back
        	if(isDoor(doorPos))
        	{
        		doorPosition = doorPos;
        		doorBlock = getBlockDoor(doorPos);
        		return true;
        	}
        	
        	return false;
        }
    }
	
	private BlockDoor getBlockDoor(BlockPos pos)
    {
        IBlockState iblockstate = this.entity.world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return block instanceof BlockDoor ? (BlockDoor)block : null;
    }
}
