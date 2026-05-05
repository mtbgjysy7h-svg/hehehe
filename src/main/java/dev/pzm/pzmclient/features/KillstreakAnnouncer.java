package dev.pzm.pzmclient.features;

import dev.pzm.pzmclient.PzmClient;
import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.UUID;

/**
 * Displays killstreak announcements and boss-bar killstreak tracker.
 * "SURPRISE" feature — Paetyn asked for extras ;)
 */
public class KillstreakAnnouncer {

    private static final String[] STREAK_NAMES = {
        "",           // 0
        "",           // 1
        "§eDouble Kill!",
        "§6Triple Kill!",
        "§cQuadra Kill!",
        "§5PENTA KILL!",
        "§d§lGODLIKE!!",
        "§b§lBEYOND GODLIKE!!!",
    };

    private static int currentStreak = 0;
    private static long lastKillTime = 0;
    private static final long STREAK_WINDOW_MS = 8000; // 8s between kills = streak reset

    public static void onKill(MinecraftClient client) {
        if (!ModConfig.get().killStreakAnnouncementsEnabled) return;
        long now = System.currentTimeMillis();

        if (now - lastKillTime > STREAK_WINDOW_MS) {
            currentStreak = 0;
        }

        currentStreak++;
        lastKillTime = now;

        if (currentStreak >= 2 && client.player != null) {
            String msg;
            if (currentStreak < STREAK_NAMES.length) {
                msg = STREAK_NAMES[currentStreak];
            } else {
                msg = "§4§l★ UNSTOPPABLE [" + currentStreak + " kills] ★";
            }
            client.player.sendMessage(Text.literal(msg), true); // action bar
        }
    }

    public static void tick(MinecraftClient client) {
        // Reset streak if player hasn't killed in a while
        if (currentStreak > 0 && System.currentTimeMillis() - lastKillTime > STREAK_WINDOW_MS) {
            currentStreak = 0;
        }
    }

    public static int getStreak() { return currentStreak; }
}
