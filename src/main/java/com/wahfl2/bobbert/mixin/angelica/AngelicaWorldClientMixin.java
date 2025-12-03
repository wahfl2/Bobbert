package com.wahfl2.bobbert.mixin.angelica;

import com.gtnewhorizons.angelica.compat.mojang.ChunkPos;
import com.wahfl2.bobbert.BobbertConfig;
import com.wahfl2.bobbert.compat.angelica.DummyChunkTracker;
import com.wahfl2.bobbert.ext.IChunkProviderClient;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkStatus;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldClient.class, priority = 1010)
public class AngelicaWorldClientMixin implements ChunkTrackerHolder {
    /* Shadows the one in Angelica's Mixin */
    @SuppressWarnings("MissingUnique")
    private ChunkTracker angelica$tracker;

    private ChunkTracker bobbert$realTracker;

    @Shadow
    private ChunkProviderClient clientChunkProvider;

    @Unique
    private IChunkProviderClient bobbert$chunkProvider;

    @Unique @SubscribeEvent
    private void bobbert$configChanged(ConfigChangedEvent event) {
        if (!event.modID.equals("bobbert")) return;

        if (BobbertConfig.enabled) {
            angelica$tracker = new DummyChunkTracker();
        } else {
            angelica$tracker = bobbert$realTracker;
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        bobbert$realTracker = angelica$tracker;
        bobbert$chunkProvider = (IChunkProviderClient) clientChunkProvider;

        if (BobbertConfig.enabled) {
            angelica$tracker = new DummyChunkTracker();
        }
    }

    @Inject(method = "doPreChunk", at = @At("TAIL"))
    private void notifyTracker(int x, int z, boolean load, CallbackInfo ci) {
        if (!BobbertConfig.enabled) return;

        if (load) {
            if (bobbert$chunkProvider.chunkbert$getChunkManager().getChunk(x, z) == null) {
                ChunkTrackerHolder.get((WorldClient)(Object) this).onChunkStatusAdded(x, z, ChunkStatus.FLAG_ALL);
            } else {
                // If we failed to load the chunk from the packet for whatever reason,
                // and if there was a fake chunk in its place previously,
                // we need to notify the listener that the chunk has indeed been unloaded.
                if (this.clientChunkProvider.chunkMapping.getValueByKey(ChunkPos.toLong(x, z)) == null) {
                    ChunkTrackerHolder.get((WorldClient)(Object) this).onChunkStatusRemoved(x, z, ChunkStatus.FLAG_ALL);
                }
            }
        } else {
            if (bobbert$chunkProvider.chunkbert$getChunkManager() != null && bobbert$chunkProvider.chunkbert$getChunkReplacement() != null) {
                ChunkTrackerHolder.get((WorldClient)(Object) this).onChunkStatusRemoved(x, z, ChunkStatus.FLAG_ALL);
            }
        }
    }

    @Override
    public ChunkTracker sodium$getTracker() {
        return bobbert$realTracker;
    }
}
