package dev.pzm.pzmclient.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.*;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("pzmclient.json");
    private static ModConfig INSTANCE = new ModConfig();

    // ── Sound Changer ──────────────────────────────────────────────
    public boolean soundChangerEnabled = true;
    public String hitSound = "ORB";           // ORB, CLICK, POP, HURT, CUSTOM
    public float hitSoundVolume = 1.0f;
    public float hitSoundPitch = 1.0f;
    public boolean killSoundEnabled = true;
    public String killSound = "THUNDER";      // THUNDER, LEVELUP, ANVIL, EXPLOSION

    // ── Kill Counter ───────────────────────────────────────────────
    public boolean killCounterEnabled = true;
    public int killCounterX = 5;
    public int killCounterY = 5;
    public int killCounterColor = 0xFFFF5555;
    public boolean showDeathCounter = true;
    public boolean showKDRatio = true;

    // ── Reach Display ──────────────────────────────────────────────
    public boolean reachDisplayEnabled = true;
    public int reachDisplayX = 5;
    public int reachDisplayY = 30;
    public int reachDisplayColor = 0xFF55FFFF;

    // ── CPS Counter ────────────────────────────────────────────────
    public boolean cpsCounterEnabled = true;
    public int cpsCounterX = 5;
    public int cpsCounterY = 50;
    public int cpsCounterColor = 0xFF55FF55;
    public boolean showPeakCPS = false;

    // ── Crosshair Customizer ───────────────────────────────────────
    public boolean crosshairEnabled = true;
    public String crosshairStyle = "DEFAULT"; // DEFAULT, DOT, PLUS, CIRCLE, CROSS, DYNAMIC
    public int crosshairColor = 0xFFFFFFFF;
    public int crosshairSize = 5;
    public int crosshairThickness = 1;
    public int crosshairGapSize = 2;
    public boolean crosshairOutline = true;
    public boolean dynamicCrosshair = false;

    // ── Auto-Sprint ────────────────────────────────────────────────
    public boolean autoSprintEnabled = false;
    public boolean toggleSneakEnabled = false;

    // ── Armor Status HUD ──────────────────────────────────────────
    public boolean armorHudEnabled = true;
    public int armorHudX = 5;
    public int armorHudY = 70;
    public boolean armorHudShowDurability = true;
    public boolean armorHudColorWarning = true;
    public int armorWarnThreshold = 20; // percent

    // ── Combo Counter ─────────────────────────────────────────────
    public boolean comboCounterEnabled = true;
    public int comboCounterX = -1; // -1 = centered
    public int comboCounterY = 40;
    public boolean comboAnimations = true;
    public int comboTimeout = 3; // seconds

    // ── Surprises / Easter Eggs ───────────────────────────────────
    public boolean bloodParticlesEnabled = true;
    public boolean screenFlashOnKill = true;
    public boolean killStreakAnnouncementsEnabled = true;
    public boolean rainbowCrosshair = false;
    public boolean matrixMode = false;        // 👀
    public boolean hyperspeedTrails = false;
    public boolean bossbarKillstreak = true;

    // ─────────────────────────────────────────────────────────────

    public static ModConfig get() {
        return INSTANCE;
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                    INSTANCE = GSON.fromJson(reader, ModConfig.class);
                    if (INSTANCE == null) INSTANCE = new ModConfig();
                }
            }
        } catch (IOException e) {
            System.err.println("[PZMClient] Failed to load config: " + e.getMessage());
            INSTANCE = new ModConfig();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (IOException e) {
            System.err.println("[PZMClient] Failed to save config: " + e.getMessage());
        }
    }
}
