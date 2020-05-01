package com.kiraisuki.rrtkobolds.entities.ai;

import java.util.concurrent.LinkedBlockingQueue;

import com.kiraisuki.rrtkobolds.RRTKobolds;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

/**
 * AI task for RRT pathfinding
 * @author kiraisuki
 *
 */
public class EntityAIMoveToBlockRRT extends EntityAIBase
{

	private final EntityCreature creature;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double movementSpeed;
    public BlockPos destination = null;
    private final LinkedBlockingQueue<Path> pathQueue;

    public EntityAIMoveToBlockRRT(EntityCreature creatureIn, double speedIn)
    {
        this.creature = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
        
        pathQueue = new LinkedBlockingQueue<>();
    }
    
    public void startPathfindingThread()
    {
    	
    }
    
    public void setDestination(BlockPos dest)
    {
    	destination = dest;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if(destination == null)
    		return false;

        this.movePosX = destination.getX();
        this.movePosY = destination.getY();
        this.movePosZ = destination.getZ();
        
        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.creature.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
        RRTKobolds.logger.info("Path planning complete");
        destination = null;
    }

}
