package com.kiraisuki.rrtkobolds.client.render.factory;

import com.kiraisuki.rrtkobolds.client.model.ModelKobold;
import com.kiraisuki.rrtkobolds.client.render.RenderKobold;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderKoboldFactory implements IRenderFactory<EntityLiving>
{
	public static final RenderKoboldFactory INSTANCE = new RenderKoboldFactory();

	@Override
	public Render<? super EntityLiving> createRenderFor(RenderManager manager)
	{
		// TODO Auto-generated method stub
		return new RenderKobold(manager, new ModelKobold(), 0.2f);
	}

}
