package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;

public class ReachDisplay {
    private double lastReach = 0.0;

    public void updateReach(double reach) {
        this.lastReach = reach;
    }

    public void render(DrawContext context) {
        ModConfig cfg = ModConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null || client.player == null) return;

        // Calculate real-time distance to targeted entity
        double display = lastReach;
        if (client.targetedEntity instanceof Entity target) {
            display = client.player.distanceTo(target);
        }

        String reachStr = String.format("%.2f", display);
        context.drawTextWithShadow(client.textRenderer,
                Text.literal("§bReach: §f" + reachStr + "m"), cfg.reachDisplayX, cfg.reachDisplayY, cfg.reachDisplayColor);
    }
}
