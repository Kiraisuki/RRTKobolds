package com.kiraisuki.rrtkobolds;

import com.kiraisuki.rrtkobolds.client.render.factory.RenderKoboldFactory;
import com.kiraisuki.rrtkobolds.core.ModBlocks;
import com.kiraisuki.rrtkobolds.core.ModItems;
import com.kiraisuki.rrtkobolds.entities.EntityKobold;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ClientProxy
{
	@SideOnly(Side.CLIENT)
	private static void registerRender(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item,  0,  new ModelResourceLocation(item.getRegistryName(), "inventory"));
		RRTKobolds.logger.info("Registered item " + item.getRegistryName() + " renderer");
	}
	
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	private static void registerRender(Class<? extends Entity> entity)
	{
		RenderingRegistry.registerEntityRenderingHandler(entity, (IRenderFactory)RenderKoboldFactory.INSTANCE);
		RRTKobolds.logger.info("Registered entity " + entity + " renderer");
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void registerRenders(ModelRegistryEvent event)
	{
		//Items
		registerRender(ModItems.itemKoboldNavigator);
		
		//ItemBlocks
		registerRender(ModBlocks.blockGoal.getItemBlock());
		
		//Entites
		registerRender(EntityKobold.class);
	}
	
	
}
