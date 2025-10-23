package com.wahfl2.bobbert.mixin.angelica;

import com.wahfl2.bobbert.compat.angelica.DummyChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldClient.class, priority = 1010)
public class AngelicaWorldClientMixin implements ChunkTrackerHolder {
    /* Shadows the one in Angelica's Mixin */
    @SuppressWarnings("MissingUnique")
    private ChunkTracker angelica$tracker;

    private ChunkTracker bobbert$realTracker;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        bobbert$realTracker = angelica$tracker;
        angelica$tracker = new DummyChunkTracker();
    }

    @Override
    public ChunkTracker sodium$getTracker() {
        return bobbert$realTracker;
    }
}
