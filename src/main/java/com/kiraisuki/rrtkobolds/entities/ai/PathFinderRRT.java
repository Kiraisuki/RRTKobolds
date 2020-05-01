package com.kiraisuki.rrtkobolds.entities.ai;

import java.util.Random;

import javax.annotation.Nullable;

import com.kiraisuki.rrtkobolds.RRTKobolds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

/**
 * Path finder for RRT-based paths
 * @author kiraisuki
 *
 */
public class PathFinderRRT extends PathFinder
{
	/** The path being generated */
    private final PathHeap path = new PathHeap();
    private final NodeProcessor nodeProcessor;
    private final Random rand;
    
    public static final int MAX_RANGE = 30;

    public PathFinderRRT(NodeProcessor processor)
    {
    	super(processor);
        this.nodeProcessor = processor;
        rand = new Random();
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, Entity targetEntity, float maxDistance)
    {
        return this.findPath(worldIn, entitylivingIn, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }

    @Nullable
    public Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, BlockPos targetPos, float maxDistance)
    {
        return this.findPath(worldIn, entitylivingIn, (double)((float)targetPos.getX() + 0.5F), (double)((float)targetPos.getY() + 0.5F), (double)((float)targetPos.getZ() + 0.5F), maxDistance);
    }

    @Nullable
    private Path findPath(IBlockAccess worldIn, EntityLiving entitylivingIn, double x, double y, double z, float maxDistance)
    {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, entitylivingIn);
        PathPoint pathpoint = this.nodeProcessor.getStart();
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(x, y, z);
        Path path = this.findPath(pathpoint, pathpoint1, maxDistance, worldIn);
        this.nodeProcessor.postProcess();
        return path;
    }
    
    /**
     * Makes a random (X, Z) point within [range] blocks of [start]
     * 
     * @param range
     * @param start
     * @return
     */
    private double[] getRandomPointInRange(PathPoint start, int range)
    {
    	double[] point = new double[3];
    	
    	point[0] = start.x + (rand.nextInt() % range) + rand.nextDouble();
    	point[1] = start.y + (rand.nextInt() % range) + rand.nextDouble();
    	point[2] = start.z + (rand.nextInt() % range) + rand.nextDouble();
    	
    	return point;
    } 

    /**
     * Find a path from one point to another using RRT algorithms
     * @param pathFrom PathPoint start point
     * @param pathTo PathPoint end point
     * @param maxDistance not used
     * @param blockAccess IBlockAccess used for collision detection
     * @return A completed path, or null
     */
    @Nullable
    private Path findPath(PathPoint pathFrom, PathPoint pathTo, float maxDistance, IBlockAccess blockAccess)
    {
    	int distance = (int)MathHelper.absMax(pathFrom.x - pathTo.x, MathHelper.absMax(pathFrom.y - pathTo.y, pathFrom.z - pathTo.z));
    	
    	if(distance > MAX_RANGE)
    	{
    		RRTKobolds.logger.info("Destination out of range. Distance: " + distance + "max distance: " + MAX_RANGE);
    		return null;
    	}
    	
    	RRTKobolds.logger.info("Pathfinding using RRT. Destination " + pathTo);
    	RRTTree tree = new RRTTree(pathFrom, pathTo, blockAccess);
    	
    	long time = System.currentTimeMillis();
    	int maxTime = 7000;
    	int iterations = 0;
    			
    	double[] randomCoord = new double[3];
		
		while(!tree.isSolved() && System.currentTimeMillis() - time < maxTime)
		{
			if(rand.nextDouble() < 0.05)
			{
				randomCoord[0] = pathTo.x;
				randomCoord[1] = pathTo.y;
				randomCoord[2] = pathTo.z;
			}
			
			else
				randomCoord = getRandomPointInRange(pathFrom, MAX_RANGE);
			
			tree.generateNextBranch(randomCoord);
			iterations++;
		}
		
		double timeSeconds = (double)(System.currentTimeMillis() - time) / 1000.0d;
		
		if(timeSeconds >= 7.0f)
			RRTKobolds.logger.info("Failed to pathfind: " + iterations + " iterations, time " + timeSeconds + "s");
		
		else
			RRTKobolds.logger.info("Pathfinding complete: " + iterations + " iterations, time " + timeSeconds + "s");
		
		return tree.getPath();
    }
}
