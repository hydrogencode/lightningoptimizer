package com.lightning.mod.mixin;

import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LightningGodOptim {

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void killClouds(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void preRenderCheck(CallbackInfo ci) {
        // Minimize unnecessary render calls
    }
}
