package com.lightning.mod.mixin;

import com.lightning.mod.ChunkCacheManager;
import net.minecraft.world.storage.RegionFile;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RegionFile.class)
public class ChunkStorageMixin {

    @Inject(method = "write", at = @At("HEAD"))
    private void onWrite(ChunkPos pos, NbtCompound nbt, CallbackInfo ci) {
        System.out.println("Saved Chunk! Pos: " + pos.x + ", " + pos.z);
        // Burada veriyi diske yazacağız
        // ChunkCacheManager.saveChunkToDisk(pos.x, pos.z, nbt.toString().getBytes());
    }
}