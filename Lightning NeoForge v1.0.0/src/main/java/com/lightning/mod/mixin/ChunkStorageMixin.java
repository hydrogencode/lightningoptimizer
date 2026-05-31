package com.lightning.mod.mixin;

import com.lightning.mod.ChunkCacheManager;
import net.minecraft.world.level.chunk.storage.RegionFile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegionFile.class)
public class ChunkStorageMixin {

    @Inject(method = "write", at = @At("HEAD"))
    private void onWrite(ChunkPos pos, CompoundTag nbt, CallbackInfo ci) {
        System.out.println("Saved Chunk! Pos: " + pos.x + ", " + pos.z);
        // ChunkCacheManager.saveChunkToDisk(pos.x, pos.z, nbt.toString().getBytes());
    }
}
