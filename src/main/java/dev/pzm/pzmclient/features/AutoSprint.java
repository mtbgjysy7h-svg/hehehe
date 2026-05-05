package dev.pzm.pzmclient.features;

import net.minecraft.client.MinecraftClient;

public class AutoSprint {
    public void tick(MinecraftClient client) {
        if (client.player == null) return;
        if (client.player.forwardSpeed > 0 && !client.player.isSprinting()
                && !client.player.isBlocking() && !client.player.isUsingItem()
                && client.player.getHungerManager().getFoodLevel() > 6) {
            client.player.setSprinting(true);
        }
    }
}
