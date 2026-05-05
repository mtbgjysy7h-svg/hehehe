package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.Deque;

public class CpsCounter {
    private final Deque<Long> leftClicks = new ArrayDeque<>();
    private final Deque<Long> rightClicks = new ArrayDeque<>();
    private int peakCps = 0;

    public void registerLeftClick() {
        leftClicks.addLast(System.currentTimeMillis());
    }

    public void registerRightClick() {
        rightClicks.addLast(System.currentTimeMillis());
    }

    public void tick() {
        long now = System.currentTimeMillis();
        long cutoff = now - 1000L;
        leftClicks.removeIf(t -> t < cutoff);
        rightClicks.removeIf(t -> t < cutoff);

        int current = leftClicks.size();
        if (current > peakCps) peakCps = current;
    }

    public int getLeftCps() { return leftClicks.size(); }
    public int getRightCps() { return rightClicks.size(); }
    public int getPeakCps() { return peakCps; }

    public void render(DrawContext context) {
        ModConfig cfg = ModConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null) return;

        int x = cfg.cpsCounterX;
        int y = cfg.cpsCounterY;
        int color = cfg.cpsCounterColor;

        context.drawTextWithShadow(client.textRenderer,
                Text.literal("§aCPS: §f" + getLeftCps()), x, y, color);

        if (cfg.showPeakCPS) {
            context.drawTextWithShadow(client.textRenderer,
                    Text.literal("§aPeak: §f" + peakCps), x, y + 10, color);
        }
    }
}
