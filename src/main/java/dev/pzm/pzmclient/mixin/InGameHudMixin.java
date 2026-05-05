package dev.pzm.pzmclient.mixin;

import dev.pzm.pzmclient.config.ModConfig;
import dev.pzm.pzmclient.features.MatrixMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void onRenderCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        ModConfig cfg = ModConfig.get();
        if (cfg.crosshairEnabled && !"DEFAULT".equals(cfg.crosshairStyle)) {
            ci.cancel(); // cancel vanilla, our CrosshairRenderer handles it
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderTail(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Render matrix mode on top of everything
        MatrixMode.render(context, client);

        // Screen flash overlay
        ScreenFlash.tick();
        int flashTicks = ScreenFlash.getTicks();
        if (flashTicks > 0 && ModConfig.get().screenFlashOnKill) {
            int alpha = (int)(ScreenFlash.getAlpha() * 180);
            int color = (alpha << 24) | 0x00FF3300;
            context.fill(0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), color);
        }
    }
}
