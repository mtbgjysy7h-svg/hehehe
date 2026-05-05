package dev.pzm.pzmclient.mixin;

/**
 * Tracks a screen flash timer, rendered by GameRendererMixin.
 */
public class ScreenFlash {
    private static int flashTicks = 0;

    public static void trigger() {
        flashTicks = 8; // flash for 8 ticks
    }

    public static int getTicks() { return flashTicks; }
    public static void tick() { if (flashTicks > 0) flashTicks--; }
    public static float getAlpha() { return flashTicks / 8.0f; }
}
