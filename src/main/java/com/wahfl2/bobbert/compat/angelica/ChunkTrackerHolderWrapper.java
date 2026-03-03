package com.wahfl2.bobbert.compat.angelica;

import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTracker;
import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.WorldClient;

public class ChunkTrackerHolderWrapper {
    public static ChunkTracker get(WorldClient world) {
        return ChunkTrackerHolder.get(world);
    }
}
