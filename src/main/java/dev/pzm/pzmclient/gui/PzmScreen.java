package dev.pzm.pzmclient.gui;

import dev.pzm.pzmclient.PzmClient;
import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.ArrayList;

/**
 * PZM Client main GUI screen.
 * Press Right Shift to open.
 * Tabs: General | Sounds | HUD | Crosshair | Extras
 */
public class PzmScreen extends Screen {

    private static final int BG_COLOR      = 0xDD0A0A0F;
    private static final int PANEL_COLOR   = 0xCC111118;
    private static final int ACCENT        = 0xFFFF4444;
    private static final int ACCENT_DIM    = 0x88FF4444;
    private static final int TEXT_COLOR    = 0xFFDDDDDD;
    private static final int TITLE_COLOR   = 0xFFFF5555;

    private static final String[] TABS = {"General", "Sounds", "HUD", "Crosshair", "Extras"};
    private int activeTab = 0;
    private boolean listeningForKey = false;

    public PzmScreen() {
        super(Text.literal("PZM Client"));
    }

    @Override
    protected void init() {
        clearChildren();
        buildTabWidgets();
    }

    private void buildTabWidgets() {
        ModConfig cfg = ModConfig.get();
        int panelX = width / 2 - 160;
        int panelY = 55;
        int ww = 320;

        // Tab buttons
        int tabW = ww / TABS.length;
        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            addDrawableChild(ButtonWidget.builder(Text.literal(TABS[i]), btn -> {
                activeTab = idx;
                init();
            }).dimensions(panelX + i * tabW, panelY - 20, tabW, 18).build());
        }

        switch (activeTab) {
            case 0 -> buildGeneral(cfg, panelX, panelY, ww);
            case 1 -> buildSounds(cfg, panelX, panelY, ww);
            case 2 -> buildHud(cfg, panelX, panelY, ww);
            case 3 -> buildCrosshair(cfg, panelX, panelY, ww);
            case 4 -> buildExtras(cfg, panelX, panelY, ww);
        }

        // Save & Close
        addDrawableChild(ButtonWidget.builder(Text.literal("✔ Save & Close"), btn -> {
            ModConfig.save();
            close();
        }).dimensions(width / 2 - 60, height - 28, 120, 20).build());
    }

    // ── Tab 0: General ─────────────────────────────────────────────
    private void buildGeneral(ModConfig cfg, int x, int y, int w) {
        int row = y + 8;

        // Current keybind display + rebind button
        String boundKey = PzmClient.openGuiKey.getBoundKeyLocalizedText().getString();
        String keyLabel = listeningForKey ? "§e▶ Press any key..." : "§7Open GUI Key: §f" + boundKey;
        addDrawableChild(ButtonWidget.builder(Text.literal(keyLabel), btn -> {
            listeningForKey = true;
            init();
        }).dimensions(x + 8, row, w - 16, 16).build());
        row += 22;

        // Shortcut: open Controls screen for this keybind directly
        addDrawableChild(ButtonWidget.builder(Text.literal("§8[Open Controls Menu to rebind]"), btn -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new ControlsOptionsScreen(this, client.options));
        }).dimensions(x + 8, row, w - 16, 14).build());
        row += 22;

        addToggle("Auto-Sprint", cfg.autoSprintEnabled, x + 8, row, v -> cfg.autoSprintEnabled = v);     row += 24;
        addToggle("Kill Counter", cfg.killCounterEnabled, x + 8, row, v -> cfg.killCounterEnabled = v);   row += 24;
        addToggle("CPS Counter", cfg.cpsCounterEnabled, x + 8, row, v -> cfg.cpsCounterEnabled = v);       row += 24;
        addToggle("Reach Display", cfg.reachDisplayEnabled, x + 8, row, v -> cfg.reachDisplayEnabled = v); row += 24;
        addToggle("Armor HUD", cfg.armorHudEnabled, x + 8, row, v -> cfg.armorHudEnabled = v);             row += 24;
        addToggle("Combo Counter", cfg.comboCounterEnabled, x + 8, row, v -> cfg.comboCounterEnabled = v); row += 24;
        addToggle("Custom Crosshair", cfg.crosshairEnabled, x + 8, row, v -> cfg.crosshairEnabled = v);    row += 24;
        addToggle("Sound Changer", cfg.soundChangerEnabled, x + 8, row, v -> cfg.soundChangerEnabled = v);
    }

    // ── Tab 1: Sounds ──────────────────────────────────────────────
    private void buildSounds(ModConfig cfg, int x, int y, int w) {
        int row = y + 8;

        addToggle("Kill Sound", cfg.killSoundEnabled, x + 8, row, v -> cfg.killSoundEnabled = v); row += 24;

        addCycleButton("Hit Sound", new String[]{"ORB", "CLICK", "POP", "HURT"}, cfg.hitSound,
                x + 8, row, v -> cfg.hitSound = v); row += 24;

        addCycleButton("Kill Sound", new String[]{"THUNDER", "LEVELUP", "ANVIL", "EXPLOSION"}, cfg.killSound,
                x + 8, row, v -> cfg.killSound = v); row += 24;

        addSlider("Hit Volume", cfg.hitSoundVolume, 0f, 2f, x + 8, row, w - 16, v -> cfg.hitSoundVolume = v); row += 30;
        addSlider("Hit Pitch",  cfg.hitSoundPitch,  0.5f, 2f, x + 8, row, w - 16, v -> cfg.hitSoundPitch = v);
    }

    // ── Tab 2: HUD ─────────────────────────────────────────────────
    private void buildHud(ModConfig cfg, int x, int y, int w) {
        int row = y + 8;

        addToggle("Show K/D Ratio", cfg.showKDRatio, x + 8, row, v -> cfg.showKDRatio = v);           row += 24;
        addToggle("Show Death Counter", cfg.showDeathCounter, x + 8, row, v -> cfg.showDeathCounter = v); row += 24;
        addToggle("Show Peak CPS", cfg.showPeakCPS, x + 8, row, v -> cfg.showPeakCPS = v);            row += 24;
        addToggle("Armor Durability", cfg.armorHudShowDurability, x + 8, row, v -> cfg.armorHudShowDurability = v); row += 24;
        addToggle("Armor Color Warning", cfg.armorHudColorWarning, x + 8, row, v -> cfg.armorHudColorWarning = v);  row += 24;
        addToggle("Combo Animations", cfg.comboAnimations, x + 8, row, v -> cfg.comboAnimations = v);
    }

    // ── Tab 3: Crosshair ───────────────────────────────────────────
    private void buildCrosshair(ModConfig cfg, int x, int y, int w) {
        int row = y + 8;

        addCycleButton("Style", new String[]{"DEFAULT","PLUS","DOT","CROSS","CIRCLE","DYNAMIC"},
                cfg.crosshairStyle, x + 8, row, v -> cfg.crosshairStyle = v); row += 24;

        addToggle("Outline", cfg.crosshairOutline, x + 8, row, v -> cfg.crosshairOutline = v);    row += 24;
        addToggle("Dynamic Mode", cfg.dynamicCrosshair, x + 8, row, v -> cfg.dynamicCrosshair = v); row += 24;
        addToggle("Rainbow Mode 🌈", cfg.rainbowCrosshair, x + 8, row, v -> cfg.rainbowCrosshair = v); row += 24;

        addSlider("Size",      cfg.crosshairSize,      1, 20, x + 8, row, w - 16, v -> cfg.crosshairSize = (int) v);      row += 30;
        addSlider("Gap",       cfg.crosshairGapSize,   0, 10, x + 8, row, w - 16, v -> cfg.crosshairGapSize = (int) v);   row += 30;
        addSlider("Thickness", cfg.crosshairThickness, 1, 5,  x + 8, row, w - 16, v -> cfg.crosshairThickness = (int) v);
    }

    // ── Tab 4: Extras (Surprises!) ─────────────────────────────────
    private void buildExtras(ModConfig cfg, int x, int y, int w) {
        int row = y + 8;

        addToggle("💥 Blood Particles",         cfg.bloodParticlesEnabled,           x + 8, row, v -> cfg.bloodParticlesEnabled = v);           row += 24;
        addToggle("⚡ Screen Flash on Kill",    cfg.screenFlashOnKill,               x + 8, row, v -> cfg.screenFlashOnKill = v);               row += 24;
        addToggle("🔥 Killstreak Announcements",cfg.killStreakAnnouncementsEnabled,   x + 8, row, v -> cfg.killStreakAnnouncementsEnabled = v);   row += 24;
        addToggle("👑 Bossbar Killstreak",      cfg.bossbarKillstreak,               x + 8, row, v -> cfg.bossbarKillstreak = v);               row += 24;
        addToggle("💫 Hyperspeed Trails",       cfg.hyperspeedTrails,                x + 8, row, v -> cfg.hyperspeedTrails = v);                row += 24;
        addToggle("🟢 MATRIX MODE",             cfg.matrixMode,                      x + 8, row, v -> cfg.matrixMode = v);
    }

    // ── Widget helpers ─────────────────────────────────────────────
    interface FloatConsumer { void accept(float v); }
    interface BoolConsumer  { void accept(boolean v); }
    interface StrConsumer   { void accept(String v); }

    private void addToggle(String label, boolean current, int x, int y, BoolConsumer onChange) {
        String text = (current ? "§a■ " : "§c■ ") + "§r" + label;
        addDrawableChild(ButtonWidget.builder(Text.literal(text), btn -> {
            boolean newVal = !current;
            onChange.accept(newVal);
            ModConfig.save();
            init(); // rebuild widgets with new state
        }).dimensions(x, y, 200, 16).build());
    }

    private void addCycleButton(String label, String[] options, String current, int x, int y, StrConsumer onChange) {
        int idx = 0;
        for (int i = 0; i < options.length; i++) if (options[i].equals(current)) { idx = i; break; }
        final int next = (idx + 1) % options.length;
        addDrawableChild(ButtonWidget.builder(
                Text.literal("§7" + label + ": §e" + current),
                btn -> {
                    onChange.accept(options[next]);
                    ModConfig.save();
                    init();
                }).dimensions(x, y, 200, 16).build());
    }

    private void addSlider(String label, float current, float min, float max, int x, int y, int w, FloatConsumer onChange) {
        // Increment buttons as a poor man's slider (works without external libs)
        String valStr = (current == (int) current) ? String.valueOf((int) current) : String.format("%.2f", current);
        addDrawableChild(ButtonWidget.builder(Text.literal("§7" + label + ": §f" + valStr + " §7[+]"), btn -> {
            float step = (max - min) / 20f;
            float nv = Math.min(max, current + step);
            onChange.accept(nv);
            ModConfig.save();
            init();
        }).dimensions(x, y, (w / 2) - 2, 16).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("§7[-]"), btn -> {
            float step = (max - min) / 20f;
            float nv = Math.max(min, current - step);
            onChange.accept(nv);
            ModConfig.save();
            init();
        }).dimensions(x + w / 2, y, (w / 2) - 2, 16).build());
    }

    // ── Rendering ──────────────────────────────────────────────────
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey) {
            // ESC cancels rebind
            if (keyCode == org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE) {
                listeningForKey = false;
                init();
                return true;
            }
            // Apply the new keybind via Fabric's KeyBinding API
            PzmClient.openGuiKey.setBoundKey(InputUtil.fromKeyCode(keyCode, scanCode));
            listeningForKey = false;
            ModConfig.save();
            init();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);

        int panelX = width / 2 - 160;
        int panelY = 35;
        int panelW = 320;
        int panelH = height - 65;

        // Dark background panel
        context.fill(panelX - 2, panelY - 2, panelX + panelW + 2, panelY + panelH + 2, 0xFF222228);
        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, BG_COLOR);

        // Accent top bar
        context.fill(panelX, panelY, panelX + panelW, panelY + 3, ACCENT);

        // Title
        String title = "§c§lPZM §r§fClient §8v1.0";
        context.drawTextWithShadow(textRenderer, Text.literal(title), panelX + 8, panelY + 6, TITLE_COLOR);

        // Active tab indicator
        int tabW = panelW / TABS.length;
        context.fill(panelX + activeTab * tabW, 53, panelX + (activeTab + 1) * tabW, 55, ACCENT);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public void close() {
        ModConfig.save();
        super.close();
    }
}
