package com.kiraisuki.rrtkobolds;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.Logger;

import com.kiraisuki.rrtkobolds.misc.CreativeTabKobold;

//http://jabelarminecraft.blogspot.com/p/creating-custom-entities.html

@Mod(modid = RRTKobolds.MODID, name = RRTKobolds.NAME, version = RRTKobolds.VERSION)
public class RRTKobolds
{
    public static final String MODID = "rrtkobolds";
    public static final String NAME = "RRT* Kobolds";
    public static final String VERSION = "0.1";
    
    public static CreativeTabs TAB;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("RRT* Kobolds init begin");
        TAB = new CreativeTabKobold(MODID + "_items");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	logger.info("RRT* Kobolds init complete");
    }
}
