package dev.pzm.pzmclient.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

// Reserved for future camera/FOV effects (e.g. hyperspeed trail zoom pulse)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // Future: renderWorld inject for hyperspeed motion blur trails
}
