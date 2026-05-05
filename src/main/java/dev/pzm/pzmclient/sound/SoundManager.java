package dev.pzm.pzmclient.sound;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;

public class SoundManager {

    public static void playHitSound() {
        ModConfig cfg = ModConfig.get();
        if (!cfg.soundChangerEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var sound = switch (cfg.hitSound) {
            case "CLICK"  -> SoundEvents.UI_BUTTON_CLICK.value();
            case "POP"    -> SoundEvents.ENTITY_CHICKEN_EGG;
            case "HURT"   -> SoundEvents.ENTITY_PLAYER_HURT;
            case "ORB"    -> SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
            default       -> SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP;
        };

        client.player.playSound(sound, cfg.hitSoundVolume, cfg.hitSoundPitch);
    }

    public static void playKillSound() {
        ModConfig cfg = ModConfig.get();
        if (!cfg.soundChangerEnabled || !cfg.killSoundEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var sound = switch (cfg.killSound) {
            case "LEVELUP"    -> SoundEvents.ENTITY_PLAYER_LEVELUP;
            case "ANVIL"      -> SoundEvents.BLOCK_ANVIL_LAND;
            case "EXPLOSION"  -> SoundEvents.ENTITY_GENERIC_EXPLODE.value();
            default           -> SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER;
        };

        client.player.playSound(sound, 1.0f, 1.0f);
    }
}
