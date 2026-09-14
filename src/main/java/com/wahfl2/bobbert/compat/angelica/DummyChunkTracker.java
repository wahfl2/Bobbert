package com.wahfl2.bobbert.compat.angelica;

import org.embeddedt.embeddium.impl.render.chunk.RenderSectionManager;
import org.embeddedt.embeddium.impl.render.chunk.map.ChunkTracker;

public class DummyChunkTracker implements ChunkTracker {
    @Override
    public void setFastMode(boolean fastMode) {
    }

    @Override
    public boolean isFastModeEnabled() {
        return false;
    }

    @Override
    public void onChunkStatusAdded(int x, int z, int flags) {
    }

    @Override
    public void onChunkStatusRemoved(int x, int z, int flags) {
    }

    @Override
    public void forEachReady(RenderSectionManager renderSectionManager) {
    }

    @Override
    public void forEachEvent(RenderSectionManager renderSectionManager) {
    }
}
