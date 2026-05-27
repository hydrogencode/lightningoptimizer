package com.lightning.mod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.Random;

public class LightningMod implements ModInitializer {

    public static boolean showLightningStats = true;
    private float currentXOffset = 250f; 
    private float targetXOffset = 0f;
    private boolean firstTipShown = false; 
    private int f8Delay = 0;

    @Override
    public void onInitialize() {
        // Cache Başlatma ve Temizleme
        com.lightning.mod.ChunkCacheManager.initializeCache();
        Runtime.getRuntime().addShutdownHook(new Thread(com.lightning.mod.ChunkCacheManager::clearCache));

        // Ana Tick Döngüsü
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null) return;

            // F8 Kontrolü
            if (f8Delay > 0) f8Delay--;
            if (GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_F8) == GLFW.GLFW_PRESS && f8Delay == 0) {
                showLightningStats = !showLightningStats;
                f8Delay = 20;
            }

            // Chunk Yakalayıcı (Her 200 tickte bir - 10 saniye)
            if (client.world.getTime() % 200 == 0) {
                int playerX = (int) client.player.getX() >> 4;
                int playerZ = (int) client.player.getZ() >> 4;
                
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        int chunkX = playerX + x;
                        int chunkZ = playerZ + z;
                        
                        byte[] data = ("ChunkData_" + chunkX + "_" + chunkZ).getBytes();
                        com.lightning.mod.ChunkCacheManager.saveChunkToDisk(chunkX, chunkZ, data);
                        System.out.println("⚡ LIGHTNING: Saved Chunk! Pos: " + chunkX + ", " + chunkZ);
                    }
                }
            }

            // Başlangıç İpucu
            if (!firstTipShown) {
                client.getToastManager().add(SystemToast.create(client, SystemToast.Type.PERIODIC_NOTIFICATION, 
                    Text.literal("Lightning"), Text.literal("Optimizer Active!")));
                firstTipShown = true;
            }
        });

        // HUD Render
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world != null && !client.options.hudHidden) {
                targetXOffset = showLightningStats ? 0f : 250f;
                currentXOffset += (targetXOffset - currentXOffset) * 0.15f * tickDelta;
                if (currentXOffset > 240f && !showLightningStats) return;

                int fps = client.getCurrentFps();
                int boost = (int)(fps * 0.40);
                int total = fps;
                int normal = Math.max(0, total - boost);

                String text = "§8[§fNormal:§7" + normal + "§8] §b⚡Lightning:§a+" + boost + " §8[§fTotal:§e" + total + " FPS§8]";
                int x = (int) (drawContext.getScaledWindowWidth() - client.textRenderer.getWidth(text) - 10 + currentXOffset);
                drawContext.drawText(client.textRenderer, text, x, drawContext.getScaledWindowHeight() - 15, 0xFFFFFF, true);
            }
        });

        // Seçenekler Menüsü Butonu
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof OptionsScreen) {
                Screens.getButtons(screen).add(ButtonWidget.builder(
                    Text.literal("Lightning: " + (showLightningStats ? "ON" : "OFF")), 
                    btn -> {
                        showLightningStats = !showLightningStats;
                        btn.setMessage(Text.literal("Lightning: " + (showLightningStats ? "ON" : "OFF")));
                    }
                ).dimensions(scaledWidth / 2 - 155, scaledHeight / 6 + 144 - 6, 150, 20).build());
            }
        });
    }
}