package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public class CrosshairRenderer {
    private static long rainbowTick = 0;

    public static void render(DrawContext context, MinecraftClient client) {
        ModConfig cfg = ModConfig.get();
        if (!cfg.crosshairEnabled) return;
        if ("DEFAULT".equals(cfg.crosshairStyle)) return; // Let vanilla handle it

        int screenW = client.getWindow().getScaledWidth();
        int screenH = client.getWindow().getScaledHeight();
        int cx = screenW / 2;
        int cy = screenH / 2;

        int color = cfg.rainbowCrosshair ? getRainbowColor() : cfg.crosshairColor;
        int size = cfg.crosshairSize;
        int gap = cfg.crosshairGapSize;
        int thick = cfg.crosshairThickness;

        switch (cfg.crosshairStyle) {
            case "PLUS" -> {
                // Horizontal
                context.fill(cx - size - gap, cy - thick / 2, cx - gap, cy + thick / 2 + 1, color);
                context.fill(cx + gap, cy - thick / 2, cx + size + gap, cy + thick / 2 + 1, color);
                // Vertical
                context.fill(cx - thick / 2, cy - size - gap, cx + thick / 2 + 1, cy - gap, color);
                context.fill(cx - thick / 2, cy + gap, cx + thick / 2 + 1, cy + size + gap, color);
            }
            case "DOT" -> {
                context.fill(cx - thick, cy - thick, cx + thick + 1, cy + thick + 1, color);
            }
            case "CROSS" -> {
                // Diagonal lines (X shape) simulated with fill
                for (int i = -size; i <= size; i++) {
                    context.fill(cx + i - thick / 2, cy + i - thick / 2, cx + i + thick / 2 + 1, cy + i + thick / 2 + 1, color);
                    context.fill(cx - i - thick / 2, cy + i - thick / 2, cx - i + thick / 2 + 1, cy + i + thick / 2 + 1, color);
                }
            }
            case "CIRCLE" -> {
                int r = size;
                for (int angle = 0; angle < 360; angle += 5) {
                    double rad = Math.toRadians(angle);
                    int px = (int) (cx + r * Math.cos(rad));
                    int py = (int) (cy + r * Math.sin(rad));
                    context.fill(px, py, px + thick, py + thick, color);
                }
            }
            case "DYNAMIC" -> {
                // Gap expands when moving/attacking
                float velocity = (client.player != null) ?
                    (float) client.player.getVelocity().horizontalLength() : 0f;
                int dynGap = gap + (int)(velocity * 8);
                context.fill(cx - size - dynGap, cy - thick / 2, cx - dynGap, cy + thick / 2 + 1, color);
                context.fill(cx + dynGap, cy - thick / 2, cx + size + dynGap, cy + thick / 2 + 1, color);
                context.fill(cx - thick / 2, cy - size - dynGap, cx + thick / 2 + 1, cy - dynGap, color);
                context.fill(cx - thick / 2, cy + dynGap, cx + thick / 2 + 1, cy + size + dynGap, color);
            }
        }

        if (cfg.crosshairOutline && !"DEFAULT".equals(cfg.crosshairStyle) && !"DOT".equals(cfg.crosshairStyle)) {
            // 1px black outline behind crosshair
            int outline = 0xFF000000;
            context.fill(cx - size - gap - 1, cy - thick / 2 - 1, cx - gap + 1, cy + thick / 2 + 2, outline);
            context.fill(cx + gap - 1, cy - thick / 2 - 1, cx + size + gap + 2, cy + thick / 2 + 2, outline);
        }
    }

    private static int getRainbowColor() {
        rainbowTick++;
        float hue = (rainbowTick % 360) / 360.0f;
        int rgb = java.awt.Color.HSBtoRGB(hue, 1f, 1f);
        return (rgb & 0x00FFFFFF) | 0xFF000000;
    }
}
