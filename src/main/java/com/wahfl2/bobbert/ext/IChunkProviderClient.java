package com.wahfl2.bobbert.ext;

import com.wahfl2.bobbert.chunk.FakeChunkManager;
import net.minecraft.nbt.NBTTagCompound;

public interface IChunkProviderClient {
    FakeChunkManager chunkbert$getChunkManager();
    NBTTagCompound chunkbert$getChunkReplacement();
}
