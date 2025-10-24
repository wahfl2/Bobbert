package com.wahfl2.bobbert.compat.angelica;

import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.WorldClient;

public class ChunkTrackerHolderWrapper {
    public static ChunkTracker get(WorldClient world) {
        return ChunkTrackerHolder.get(world);
    }
}
