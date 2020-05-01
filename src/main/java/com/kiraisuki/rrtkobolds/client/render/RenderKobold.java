package com.kiraisuki.rrtkobolds.client.render;

import com.kiraisuki.rrtkobolds.RRTKobolds;
import com.kiraisuki.rrtkobolds.entities.EntityKobold;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderKobold extends RenderLiving
{
	private ResourceLocation[] textures;

	public RenderKobold(RenderManager rendermanagerIn, ModelBase mainModel, float shadowSize)
	{
		super(rendermanagerIn, mainModel, shadowSize);
		
		textures = new ResourceLocation[EntityKobold.NUM_VARIANTS];
		
		for(int i = 0; i < EntityKobold.NUM_VARIANTS; i++)
			textures[i] = new ResourceLocation(RRTKobolds.MODID + ":textures/entities/entity_kobold" + i + ".png");
		
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		int texid = ((EntityKobold)entity).getVariant();
		
		return textures[texid % EntityKobold.NUM_VARIANTS];
	}
}
