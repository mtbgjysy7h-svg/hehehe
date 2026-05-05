package dev.pzm.pzmclient.features;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Random;

/**
 * Matrix Mode — renders a falling green code overlay over the screen.
 * 100% a surprise easter egg. Toggle it in the Extras tab.
 */
public class MatrixMode {

    private static final int COLS = 40;
    private static final int[] drops = new int[COLS];
    private static final char[][] chars = new char[COLS][60];
    private static final Random rng = new Random();
    private static long lastTick = 0;
    private static boolean initialized = false;

    private static void init() {
        for (int i = 0; i < COLS; i++) {
            drops[i] = rng.nextInt(60);
            for (int j = 0; j < 60; j++) {
                chars[i][j] = randomChar();
            }
        }
        initialized = true;
    }

    private static char randomChar() {
        String pool = "アイウエオカキクケコ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return pool.charAt(rng.nextInt(pool.length()));
    }

    public static void render(DrawContext context, MinecraftClient client) {
        if (!ModConfig.get().matrixMode) return;
        if (client.textRenderer == null) return;
        if (!initialized) init();

        long now = System.currentTimeMillis();
        boolean shouldStep = (now - lastTick) > 80;
        if (shouldStep) lastTick = now;

        int sw = client.getWindow().getScaledWidth();
        int sh = client.getWindow().getScaledHeight();
        int colW = sw / COLS;
        int rowH = 9;
        int rows = sh / rowH + 1;

        for (int col = 0; col < COLS; col++) {
            int drop = drops[col];

            for (int row = 0; row < rows; row++) {
                int charIdx = row % 60;
                char c = chars[col][charIdx];

                int alpha = 255 - (int) ((Math.abs(drop - row) / (float) rows) * 200);
                alpha = Math.max(30, Math.min(255, alpha));

                // Head of drop = bright white, trail = green fade
                int color;
                if (row == drop) {
                    color = 0xFFFFFFFF;
                } else if (row < drop && row > drop - 8) {
                    color = (alpha << 24) | 0x00FF44;
                } else {
                    color = (Math.max(0, alpha - 100) << 24) | 0x003300;
                }

                if ((color >> 24 & 0xFF) > 10) {
                    context.drawText(client.textRenderer,
                            String.valueOf(c), col * colW, row * rowH, color, false);
                }
            }

            if (shouldStep) {
                // Randomly mutate characters
                if (rng.nextFloat() < 0.1f) {
                    chars[col][rng.nextInt(60)] = randomChar();
                }
                drops[col]++;
                if (drops[col] > rows + 10) {
                    drops[col] = rng.nextInt(10) - 10;
                }
            }
        }
    }
}
