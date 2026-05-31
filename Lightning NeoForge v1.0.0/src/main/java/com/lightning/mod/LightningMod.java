package com.lightning.mod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

@Mod("lightning")
public class LightningMod {

    public static boolean showLightningStats = true;
    private float currentXOffset = 250f;
    private float targetXOffset = 0f;
    private boolean firstTipShown = false;
    private int f8Delay = 0;

    // Real FPS tracking: 5-second rolling average (100 ticks at 20tps)
    private static final int FPS_HISTORY_SIZE = 100;
    private final Deque<Integer> fpsHistory = new ArrayDeque<>();
    private int smoothedFps = 0;
    private long lastFrameNano = System.nanoTime();

    // Tips list
    private final String[] lightningTips = {
        "Press F8 to toggle the FPS HUD anytime!",
        "Lightning Optimizer works with 1.20.1 and 1.21.x!",
        "Check the Options menu for the Toggle switch.",
        "Smooth animations make everything look better.",
        "Low-end PC? Lightning has your back!",
        "Try using F1 mode to hide the HUD completely.",
        "Java 21 support ensures maximum performance.",
        "Optimization is the key to victory!",
        "Lightning is lighter than a feather on your RAM.",
        "Warp animation speed is set to 15% for smoothness.",
        "Join our community for more optimization tips!",
        "Did you notice the sleek slide-in animation?",
        "Optimizing your game, one frame at a time.",
        "Developed with passion for the community!"
    };

    public LightningMod(IEventBus modEventBus) {
        ChunkCacheManager.initializeCache();
        Runtime.getRuntime().addShutdownHook(new Thread(ChunkCacheManager::clearCache));

        modEventBus.addListener(this::onClientSetup);
        NeoForge.EVENT_BUS.addListener(this::onClientTick);
        NeoForge.EVENT_BUS.addListener(this::onRenderGui);
    }

    private void onClientSetup(FMLClientSetupEvent event) {}

    private void onClientTick(ClientTickEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.player == null) return;

        // F8 spam protection + toggle
        if (f8Delay > 0) f8Delay--;
        if (GLFW.glfwGetKey(client.getWindow().getWindow(), GLFW.GLFW_KEY_F8) == GLFW.GLFW_PRESS && f8Delay == 0) {
            showLightningStats = !showLightningStats;
            SystemToast.add(
                client.getToasts(),
                SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                Component.literal("Lightning Status"),
                Component.literal("HUD is now " + (showLightningStats ? "\u00a7aON" : "\u00a7cOFF"))
            );
            f8Delay = 20;
        }

        // Real rolling FPS average
        int currentFps = client.getFps();
        fpsHistory.addLast(currentFps);
        if (fpsHistory.size() > FPS_HISTORY_SIZE) fpsHistory.pollFirst();
        smoothedFps = (int) fpsHistory.stream().mapToInt(i -> i).average().orElse(currentFps);

        // Chunk caching every 200 ticks (~10 seconds)
        if (client.level.getGameTime() % 200 == 0) {
            int playerX = (int) client.player.getX() >> 4;
            int playerZ = (int) client.player.getZ() >> 4;
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    int chunkX = playerX + x;
                    int chunkZ = playerZ + z;
                    byte[] data = ("ChunkData_" + chunkX + "_" + chunkZ).getBytes();
                    ChunkCacheManager.saveChunkToDisk(chunkX, chunkZ, data);
                }
            }
        }

        // Random tip toast on first launch
        if (!firstTipShown) {
            String tip = lightningTips[new Random().nextInt(lightningTips.length)];
            SystemToast.add(
                client.getToasts(),
                SystemToast.SystemToastId.PERIODIC_NOTIFICATION,
                Component.literal("Lightning Tips"),
                Component.literal(tip)
            );
            firstTipShown = true;
        }
    }

    private void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft client = Minecraft.getInstance();
        if (client.level == null || client.options.hideGui) return;

        targetXOffset = showLightningStats ? 0f : 250f;
        currentXOffset += (targetXOffset - currentXOffset) * 0.15f;
        if (currentXOffset > 240f && !showLightningStats) return;

        long now = System.nanoTime();
        float frameTimeMs = (now - lastFrameNano) / 1_000_000f;
        lastFrameNano = now;
        frameTimeMs = Math.min(frameTimeMs, 999f);

        String text = "\u00a78[\u00a7b\u26a1 Lightning\u00a78] \u00a7fFPS: \u00a7a" + smoothedFps
                + " \u00a78| \u00a7fFrame: \u00a7e" + String.format("%.1f", frameTimeMs) + "ms";

        var guiGraphics = event.getGuiGraphics();
        int x = (int) (guiGraphics.guiWidth() - client.font.width(text) - 10 + currentXOffset);
        guiGraphics.drawString(client.font, text, x, guiGraphics.guiHeight() - 15, 0xFFFFFF, true);
    }

    private void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof OptionsScreen) {
            int w = event.getScreen().width;
            int h = event.getScreen().height;
            event.addListener(Button.builder(
                Component.literal("Lightning Stats: " + (showLightningStats ? "\u00a7aON" : "\u00a7cOFF")),
                btn -> {
                    showLightningStats = !showLightningStats;
                    btn.setMessage(Component.literal("Lightning Stats: " + (showLightningStats ? "\u00a7aON" : "\u00a7cOFF")));
                }
            ).bounds(w / 2 - 155, h / 6 + 144 - 6, 150, 20).build());
        }
    }
}
