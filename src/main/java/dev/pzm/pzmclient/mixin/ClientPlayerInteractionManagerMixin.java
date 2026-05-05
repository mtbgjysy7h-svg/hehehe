package dev.pzm.pzmclient.mixin;

import dev.pzm.pzmclient.PzmClient;
import dev.pzm.pzmclient.features.BloodParticles;
import dev.pzm.pzmclient.features.KillstreakAnnouncer;
import dev.pzm.pzmclient.sound.SoundManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {

    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        // Register CPS click
        PzmClient.cpsCounter.registerLeftClick();

        // Hit sound
        SoundManager.playHitSound();

        // Blood particles at target location
        BloodParticles.spawn(MinecraftClient.getInstance(), target.getX(), target.getY(), target.getZ());

        // Combo counter
        PzmClient.comboCounter.onHit();
    }
}
