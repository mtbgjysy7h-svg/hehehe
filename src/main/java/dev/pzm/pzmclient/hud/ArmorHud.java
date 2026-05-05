package dev.pzm.pzmclient.hud;

import dev.pzm.pzmclient.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ArmorHud {

    public void render(DrawContext context) {
        ModConfig cfg = ModConfig.get();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.textRenderer == null) return;

        int x = cfg.armorHudX;
        int y = cfg.armorHudY;

        // Render helmet, chestplate, leggings, boots top to bottom
        String[] slots = {"Helmet", "Chest", "Legs", "Boots"};
        ItemStack[] armor = new ItemStack[]{
            client.player.getInventory().getArmorStack(3),
            client.player.getInventory().getArmorStack(2),
            client.player.getInventory().getArmorStack(1),
            client.player.getInventory().getArmorStack(0)
        };

        for (int i = 0; i < 4; i++) {
            ItemStack stack = armor[i];
            if (stack.isEmpty()) continue;

            int renderY = y + (i * 20);

            // Render item icon
            context.drawItem(stack, x, renderY);

            if (cfg.armorHudShowDurability && stack.isDamageable()) {
                int maxDur = stack.getMaxDamage();
                int curDur = maxDur - stack.getDamage();
                int pct = (int) ((curDur / (float) maxDur) * 100);

                int color;
                if (cfg.armorHudColorWarning) {
                    if (pct <= cfg.armorWarnThreshold) {
                        color = 0xFFFF5555; // red = danger
                    } else if (pct <= 50) {
                        color = 0xFFFFAA00; // orange = low
                    } else {
                        color = 0xFF55FF55; // green = good
                    }
                } else {
                    color = 0xFFFFFFFF;
                }

                context.drawTextWithShadow(client.textRenderer,
                        Text.literal(pct + "%"), x + 20, renderY + 4, color);
            }
        }
    }
}
