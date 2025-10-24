package com.wahfl2.bobbert;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        Bobbert.LOG.info("Hello from Bobbert! v" + Tags.VERSION);
        MinecraftForge.EVENT_BUS.register(Bobbert.INSTANCE);

        try {
            ConfigurationManager.registerConfig(BobbertConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
}
