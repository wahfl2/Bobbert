package com.wahfl2.bobbert.mixin.angelica;

import com.gtnewhorizons.angelica.compat.mojang.ChunkPos;
import com.llamalad7.mixinextras.sugar.Local;
import com.wahfl2.bobbert.chunk.FakeChunkManager;
import com.wahfl2.bobbert.ext.IChunkProviderClient;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkStatus;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTracker;
import me.jellysquid.mods.sodium.client.render.chunk.map.ChunkTrackerHolder;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkProviderClient.class)
public class AngelicaChunkProviderClientMixin {
    @Shadow
    private World worldObj;

    @Unique
    private WorldClient worldClient;

    @Unique
    private IChunkProviderClient chunkProvider;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bobbyInit(World worldIn, CallbackInfo ci) {
        worldClient = (WorldClient) worldIn;
        chunkProvider = (IChunkProviderClient) this;
    }

    @Inject(method = "loadChunk", at = @At("RETURN"))
    private void bobbyFakeChunkReplaced(int x, int z, CallbackInfoReturnable<Chunk> cir) {
        // Only call the tracker if there was no fake chunk in its place previously
        if (chunkProvider.chunkbert$getChunkManager().getChunk(x, z) == null) {
            ChunkTrackerHolder.get(worldClient).onChunkStatusAdded(x, z, ChunkStatus.FLAG_ALL);
        } else {
            // If we failed to load the chunk from the packet for whatever reason,
            // and if there was a fake chunk in its place previously,
            // we need to notify the listener that the chunk has indeed been unloaded.
            if (worldObj.getChunkProvider() instanceof ChunkProviderClient provider) {
                if (provider.chunkMapping.getValueByKey(ChunkPos.toLong(x, z)) == null) {
                    ChunkTrackerHolder.get(worldClient).onChunkStatusRemoved(x, z, ChunkStatus.FLAG_ALL);
                }
            }
        }
    }

    @Inject(method = "unloadChunk", at = @At("TAIL"), order = 999)
    private void bobbyReplaceChunk(int chunkX, int chunkZ, CallbackInfo ci, @Local Chunk chunk) {
        if (chunkProvider.chunkbert$getChunkManager() != null && chunkProvider.chunkbert$getChunkReplacement() != null && !chunk.isEmpty()) {
            ChunkTrackerHolder.get(worldClient).onChunkStatusRemoved(chunkX, chunkZ, ChunkStatus.FLAG_ALL);
        }
    }
}
