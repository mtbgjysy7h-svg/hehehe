package dev.pzm.pzmclient.features;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleTypes;

public class BloodParticles {
    /**
     * Called when we land a hit on an entity.
     * Spawns dramatic red crit particles at the entity's position.
     */
    public static void spawn(MinecraftClient client, double x, double y, double z) {
        if (!ModConfig.get().bloodParticlesEnabled) return;
        if (client.particleManager == null) return;

        // Burst of red damage particles
        for (int i = 0; i < 8; i++) {
            double vx = (Math.random() - 0.5) * 0.3;
            double vy = Math.random() * 0.3;
            double vz = (Math.random() - 0.5) * 0.3;
            client.particleManager.addParticle(ParticleTypes.DAMAGE_INDICATOR,
                    x, y + 1.0, z, vx, vy, vz);
        }
    }
}
