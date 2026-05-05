package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ComboCounter {
    private int combo = 0;
    private long lastHitTime = 0;
    private float scale = 1.0f;
    private boolean animating = false;

    public void onHit() {
        combo++;
        lastHitTime = System.currentTimeMillis();
        if (ModConfig.get().comboAnimations) {
            scale = 1.6f;
            animating = true;
        }
    }

    public void tick() {
        ModConfig cfg = ModConfig.get();
        long elapsed = (System.currentTimeMillis() - lastHitTime) / 1000L;
        if (combo > 0 && elapsed >= cfg.comboTimeout) {
            combo = 0;
            scale = 1.0f;
        }

        // Animate scale back to 1.0
        if (animating && scale > 1.0f) {
            scale -= 0.05f;
            if (scale <= 1.0f) {
                scale = 1.0f;
                animating = false;
            }
        }
    }

    public int getCombo() { return combo; }

    public void render(DrawContext context) {
        if (combo < 2) return;
        ModConfig cfg = ModConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null) return;

        int screenWidth = client.getWindow().getScaledWidth();
        String comboText = combo + "x COMBO";

        // Color ramps with combo count
        int color;
        if      (combo >= 20) color = 0xFFFF00FF; // purple
        else if (combo >= 10) color = 0xFFFF5500; // orange
        else if (combo >= 5)  color = 0xFFFFFF00; // yellow
        else                  color = 0xFFFFFFFF; // white

        int x = cfg.comboCounterX < 0
                ? (screenWidth / 2) - (client.textRenderer.getWidth(comboText) / 2)
                : cfg.comboCounterX;
        int y = cfg.comboCounterY;

        context.getMatrices().push();
        context.getMatrices().translate(x + client.textRenderer.getWidth(comboText) / 2f, y + 4f, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.getMatrices().translate(-(x + client.textRenderer.getWidth(comboText) / 2f), -(y + 4f), 0);

        context.drawTextWithShadow(client.textRenderer, Text.literal(comboText), x, y, color);
        context.getMatrices().pop();
    }
}
