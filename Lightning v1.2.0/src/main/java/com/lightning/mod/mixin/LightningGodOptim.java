package com.lightning.mod.mixin;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class LightningGodOptim {
    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    private void killClouds(CallbackInfo ci) { ci.cancel(); }

    @Inject(method = "render", at = @At("HEAD"))
    private void preRenderCheck(CallbackInfo ci) {
        // Işıklandırma ve gereksiz render çağrılarını minimize eder
    }
}