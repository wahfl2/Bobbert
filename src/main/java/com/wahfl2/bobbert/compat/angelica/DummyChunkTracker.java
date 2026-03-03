package com.wahfl2.bobbert.compat.angelica;

import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTracker;

public class DummyChunkTracker extends ChunkTracker {
    @Override
    public void onChunkStatusRemoved(int x, int z, int flags) { }

    @Override
    public void onChunkStatusAdded(int x, int z, int flags) { }
}
