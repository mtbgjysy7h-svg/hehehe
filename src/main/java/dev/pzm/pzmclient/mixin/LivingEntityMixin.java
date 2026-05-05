package dev.pzm.pzmclient.mixin;

import dev.pzm.pzmclient.PzmClient;
import dev.pzm.pzmclient.config.ModConfig;
import dev.pzm.pzmclient.features.KillstreakAnnouncer;
import dev.pzm.pzmclient.sound.SoundManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        LivingEntity self = (LivingEntity)(Object)this;

        // Is this entity dying and was killed by our player?
        if (source.getAttacker() instanceof PlayerEntity attacker
                && attacker == client.player
                && !(self instanceof PlayerEntity)) {
            // NPC/mob kill
            PzmClient.killCounter.onKill();
            SoundManager.playKillSound();
            KillstreakAnnouncer.onKill(client);

            // Screen flash
            if (ModConfig.get().screenFlashOnKill) {
                ScreenFlash.trigger();
            }
        }

        // Player kills player
        if (self instanceof PlayerEntity victim
                && victim != client.player
                && source.getAttacker() == client.player) {
            PzmClient.killCounter.onKill();
            SoundManager.playKillSound();
            KillstreakAnnouncer.onKill(client);
            if (ModConfig.get().screenFlashOnKill) ScreenFlash.trigger();
        }

        // Local player died
        if (self == client.player) {
            PzmClient.killCounter.onDeath();
        }
    }
}
