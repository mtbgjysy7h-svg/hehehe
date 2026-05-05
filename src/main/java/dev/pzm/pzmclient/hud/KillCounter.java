package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class KillCounter {
    private int kills = 0;
    private int deaths = 0;

    public void onKill() {
        kills++;
    }

    public void onDeath() {
        deaths++;
    }

    public int getKills() { return kills; }
    public int getDeaths() { return deaths; }

    public void reset() {
        kills = 0;
        deaths = 0;
    }

    public void render(DrawContext context) {
        ModConfig cfg = ModConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null) return;

        int x = cfg.killCounterX;
        int y = cfg.killCounterY;
        int color = cfg.killCounterColor;

        context.drawTextWithShadow(client.textRenderer,
                Text.literal("§cKills: §f" + kills), x, y, color);

        if (cfg.showDeathCounter) {
            context.drawTextWithShadow(client.textRenderer,
                    Text.literal("§cDeaths: §f" + deaths), x, y + 10, color);
        }

        if (cfg.showKDRatio) {
            double kd = deaths == 0 ? kills : (double) kills / deaths;
            String kdStr = String.format("%.2f", kd);
            context.drawTextWithShadow(client.textRenderer,
                    Text.literal("§cK/D: §f" + kdStr), x, y + (cfg.showDeathCounter ? 20 : 10), color);
        }
    }
}
