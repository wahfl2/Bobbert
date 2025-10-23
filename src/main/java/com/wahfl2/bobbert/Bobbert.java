package com.wahfl2.bobbert;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = Bobbert.MODID,
    version = Tags.VERSION,
    name = "Bobbert",
    acceptedMinecraftVersions = "[1.7.10]")
public class Bobbert {

    public static final String MODID = "bobbert";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.Instance(MODID)
    public static Bobbert INSTANCE;

    @SidedProxy(clientSide = "com.wahfl2.bobbert.ClientProxy", serverSide = "com.wahfl2.bobbert.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }
}
