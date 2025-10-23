package com.wahfl2.bobbert;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Bobbert.LOG.info("Bobbert is a client-side only mod and will do nothing on the server!");
    }
}
