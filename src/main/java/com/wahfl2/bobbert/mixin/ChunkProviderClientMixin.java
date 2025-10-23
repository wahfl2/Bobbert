package com.wahfl2.bobbert.mixin;

import javax.annotation.Nullable;

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

import com.wahfl2.bobbert.BobbertConfig;
import com.wahfl2.bobbert.chunk.FakeChunkManager;
import com.wahfl2.bobbert.chunk.FakeChunkStorage;
import com.wahfl2.bobbert.ext.IChunkProviderClient;
import com.wahfl2.bobbert.util.ChunkPos;

@Mixin(ChunkProviderClient.class)
public class ChunkProviderClientMixin implements IChunkProviderClient {

    @Shadow
    private Chunk blankChunk;
    @Shadow
    private World worldObj;

    @Unique
    protected @Nullable FakeChunkManager chunkbert$ChunkManager = null;

    // Cache of chunk which was just unloaded so we can immediately
    // load it again without having to wait for the storage io worker.
    @Unique
    protected @Nullable NBTTagCompound chunkbert$ChunkReplacement;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void bobbyInit(World worldIn, CallbackInfo ci) {
        if (BobbertConfig.enabled)
            chunkbert$ChunkManager = new FakeChunkManager((WorldClient) worldIn, (ChunkProviderClient) (Object) this);
    }

    @Override
    public @Nullable FakeChunkManager chunkbert$getChunkManager() {
        return chunkbert$ChunkManager;
    }

    @Inject(method = "provideChunk", at = @At("RETURN"), cancellable = true)
    private void bobbyGetChunk(int x, int z, CallbackInfoReturnable<Chunk> ci) {
        // Did we find a live chunk?
        if (ci.getReturnValue() != blankChunk) {
            return;
        }

        if (chunkbert$ChunkManager == null) {
            return;
        }

        // Otherwise, see if we've got one
        Chunk chunk = chunkbert$ChunkManager.getChunk(x, z);
        if (chunk != null) {
            ci.setReturnValue(chunk);
        }
    }

    @Inject(method = "unloadChunk", at = @At("HEAD"))
    private void bobbySaveChunk(int chunkX, int chunkZ, CallbackInfo ci) {
        if (chunkbert$ChunkManager == null) {
            return;
        }

        Chunk chunk = null;
        if (worldObj.getChunkProvider() instanceof ChunkProviderClient provider) {
            chunk = (Chunk) provider.chunkMapping.getValueByKey(ChunkPos.toLong(chunkX, chunkZ));
        }

        if (chunk == null) {
            return;
        }

        FakeChunkStorage storage = chunkbert$ChunkManager.getStorage();
        NBTTagCompound tag = storage.serialize(chunk);
        storage.save(ChunkPos.of(chunk), tag);
        chunkbert$ChunkReplacement = tag;
    }

    @Inject(method = "unloadChunk", at = @At("RETURN"))
    private void bobbyReplaceChunk(int chunkX, int chunkZ, CallbackInfo ci) {
        if (chunkbert$ChunkManager == null) {
            return;
        }

        NBTTagCompound tag = chunkbert$ChunkReplacement;
        chunkbert$ChunkReplacement = null;
        if (tag == null) {
            return;
        }
        chunkbert$ChunkManager.load(chunkX, chunkZ, tag, chunkbert$ChunkManager.getStorage());
    }

    @Inject(method = "makeString", at = @At("RETURN"), cancellable = true)
    private void bobbyDebugString(CallbackInfoReturnable<String> cir) {
        if (chunkbert$ChunkManager == null) {
            return;
        }

        cir.setReturnValue(cir.getReturnValue() + " " + chunkbert$ChunkManager.getDebugString());
    }
}
