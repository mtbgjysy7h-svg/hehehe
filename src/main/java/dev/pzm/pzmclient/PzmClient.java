package dev.pzm.pzmclient;

import dev.pzm.pzmclient.config.ModConfig;
import dev.pzm.pzmclient.features.*;
import dev.pzm.pzmclient.gui.PzmScreen;
import dev.pzm.pzmclient.hud.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class PzmClient implements ClientModInitializer {

    public static final String MOD_ID = "pzmclient";
    public static KeyBinding openGuiKey;

    // Feature singletons
    public static KillCounter killCounter;
    public static CpsCounter cpsCounter;
    public static ReachDisplay reachDisplay;
    public static ArmorHud armorHud;
    public static ComboCounter comboCounter;
    public static AutoSprint autoSprint;

    @Override
    public void onInitializeClient() {
        ModConfig.load();

        // Keybind: Right Shift opens GUI
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pzmclient.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.pzmclient.main"
        ));

        // Init features
        killCounter  = new KillCounter();
        cpsCounter   = new CpsCounter();
        reachDisplay = new ReachDisplay();
        armorHud     = new ArmorHud();
        comboCounter = new ComboCounter();
        autoSprint   = new AutoSprint();

        // Register HUD renderers
        HudRenderCallback.EVENT.register((context, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null || client.options.hudHidden) return;

            ModConfig cfg = ModConfig.get();
            if (cfg.killCounterEnabled)  killCounter.render(context);
            if (cfg.cpsCounterEnabled)   cpsCounter.render(context);
            if (cfg.reachDisplayEnabled) reachDisplay.render(context);
            if (cfg.armorHudEnabled)     armorHud.render(context);
            if (cfg.comboCounterEnabled) comboCounter.render(context);
            CrosshairRenderer.render(context, client);
        });

        // Tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new PzmScreen());
                }
            }

            if (client.player == null) return;
            ModConfig cfg = ModConfig.get();

            cpsCounter.tick();
            comboCounter.tick();
            if (cfg.autoSprintEnabled) autoSprint.tick(client);
            KillstreakAnnouncer.tick(client);
        });

        System.out.println("[PZMClient] Loaded! Press Right Shift to open the GUI. gl hf ;)");
    }
}
