package com.kiraisuki.rrtkobolds.entities;

import java.util.Random;

import com.kiraisuki.rrtkobolds.RRTKobolds;
import com.kiraisuki.rrtkobolds.entities.ai.EntityAIMoveToBlockRRT;
import com.kiraisuki.rrtkobolds.entities.ai.EntityAIOpenDoorRRT;
import com.kiraisuki.rrtkobolds.entities.ai.PathNavigateGroundRRT;
import com.kiraisuki.rrtkobolds.items.ItemKoboldNavigator;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A short, cute, draconic entity
 * @author kiraisuki
 *
 */
public class EntityKobold extends EntityCreature
{
	private final Random rand = new Random();
	private static final float MOVEMENT_SPEED = 0.47f;
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityKobold.class, DataSerializers.VARINT);
	
	public static final int NUM_VARIANTS = 6;
	public static final int EGG_MAJOR_COLOR = 0x900056;
	public static final int EGG_MINOR_COLOR = 0xA78298;
	public final int ANIM_DESYNC = rand.nextInt() % 5000;
	public EntityAIMoveToBlockRRT moveToBlockRRT;

	public EntityKobold(World worldIn)
	{
		super(worldIn);
		
		setSize(0.5f, 1.1f);
		
		this.enablePersistence();
	}
	
	public void setDestination(BlockPos pos)
	{
		moveToBlockRRT.setDestination(pos);
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		Item item = player.getHeldItem(hand).getItem();
		
		if(item instanceof ItemKoboldNavigator)
		{
			((ItemKoboldNavigator)item).setKobold(player, this);
			return true;
		}
			
		return false;
	}
	
	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
	}
	
	@Override
	public PathNavigate createNavigator(World worldIn)
	{
		RRTKobolds.logger.info("Created RRT navigator");
		return new PathNavigateGroundRRT(this, worldIn);
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(VARIANT, Integer.valueOf(new Random().nextInt(NUM_VARIANTS)));
	}
	
	public int getVariant()
	{
		return Integer.valueOf(this.dataManager.get(VARIANT).intValue());
	}

	@Override
	protected void initEntityAI()
    {
		moveToBlockRRT = new EntityAIMoveToBlockRRT(this, MOVEMENT_SPEED);
		moveToBlockRRT.startPathfindingThread();
		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(2, moveToBlockRRT);
        tasks.addTask(3, new EntityAIOpenDoorRRT(this, true));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.dataManager.set(VARIANT, Integer.valueOf(compound.getInteger("variant")));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("variant", getVariant());
	}
}
