package com.kiraisuki.rrtkobolds;

import com.kiraisuki.rrtkobolds.core.ModBlocks;
import com.kiraisuki.rrtkobolds.core.ModItems;
import com.kiraisuki.rrtkobolds.entities.EntityKobold;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod.EventBusSubscriber
public class CommonProxy
{
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		//Items
		registerItem(event, ModItems.itemKoboldNavigator);
		
		//ItemBlocks
		registerItem(event, ModBlocks.blockGoal.getItemBlock());
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		registerBlock(event, ModBlocks.blockGoal);
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event)
	{
		registerEntityWithEgg(EntityEntryBuilder.<EntityKobold>create(), event, EntityKobold.class, "entity_kobold", 1, EntityKobold.EGG_MAJOR_COLOR, EntityKobold.EGG_MINOR_COLOR, 256, 3);
	}
	
	private static void registerBlock(RegistryEvent.Register<Block> event, Block block)
	{
		event.getRegistry().register(block);
		RRTKobolds.logger.info("Registered block " + block.getRegistryName());
	}
	
	private static void registerItem(RegistryEvent.Register<Item> event, Item item)
	{
		event.getRegistry().register(item);
		RRTKobolds.logger.info("Registered item " + item.getRegistryName());
	}
	
	private static void registerEntityWithEgg(EntityEntryBuilder builder, RegistryEvent.Register<EntityEntry> event, Class<? extends Entity> entityClass, String name, int id, int mainColor, int subColor, int range, int frequency)
	{
		builder.entity(entityClass);
        builder.id(new ResourceLocation(RRTKobolds.MODID, name), id);
        builder.name(name);
        builder.egg(mainColor, subColor);
        builder.tracker(range, frequency, true);
        event.getRegistry().register(builder.build());
        
        RRTKobolds.logger.info("Registered entity " + name + " with spawn egg");
	}
}
