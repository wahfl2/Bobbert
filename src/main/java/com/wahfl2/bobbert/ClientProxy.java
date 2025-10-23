package com.wahfl2.bobbert;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Bobbert.LOG.info("Hello from Bobbert!");
        MinecraftForge.EVENT_BUS.register(Bobbert.INSTANCE);
    }
}
