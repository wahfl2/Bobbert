package com.wahfl2.bobbert.chunk;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.world.chunk.storage.RegionFileCache;

import com.wahfl2.bobbert.BobbertConfig;
import com.wahfl2.bobbert.ext.AnvilChunkLoaderExt;
import com.wahfl2.bobbert.util.ChunkPos;

public class FakeChunkStorage extends AnvilChunkLoader {

    public FakeChunkStorage(File file) {
        super(file);
        if (BobbertConfig.noBlockEntities) ((AnvilChunkLoaderExt) this).chunkbert$setLoadsTileEntities(false);
    }

    public static FakeChunkStorage getFor(File file) {
        return new FakeChunkStorage(file);
    }

    public NBTTagCompound loadTag(ChunkPos pos) throws IOException {
        NBTTagCompound compound;
        DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, pos.x, pos.z);

        if (datainputstream == null) {
            return null;
        }

        compound = CompressedStreamTools.read(datainputstream);
        datainputstream.close(); // Forge: close stream after use
        return compound;
    }

    public Supplier<Chunk> deserialize(ChunkPos pos, NBTTagCompound tag, WorldClient world) {
        Object[] data = this.checkedReadChunkFromNBT__Async(world, pos.x, pos.z, tag);
        if (data != null) {
            Chunk chunk = (Chunk) data[0];
            NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
            return () -> {
                this.loadEntities(world, nbttagcompound.getCompoundTag("Level"), chunk);
                return chunk;
            };
        }
        return null;
    }

    public NBTTagCompound serialize(Chunk chunk) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound.setTag("Level", nbttagcompound1);
        nbttagcompound.setInteger("DataVersion", 1343);

        this.writeChunkToNBT(chunk, chunk.worldObj, nbttagcompound1);
        return nbttagcompound;
    }

    public void save(ChunkPos pos, NBTTagCompound compound) {
        this.addChunkToPending(pos.toChunkCoord(), compound);
    }
}
