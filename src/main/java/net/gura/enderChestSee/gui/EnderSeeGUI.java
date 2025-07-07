package net.gura.enderChestSee.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderSeeGUI {

    private final Map<UUID, UUID> enderSeeSession = new HashMap<>();

    public void openEnderChest(Player viewer, Player target) {
        Inventory gui = Bukkit.createInventory(
                viewer, 27,
                Component.text(target.getName() + "'s Ender Chest")
                        .color(NamedTextColor.DARK_GRAY)
        );

        for (int i = 0; i < gui.getSize(); i++) {
            ItemStack item = target.getEnderChest().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                gui.setItem(i, item.clone());
            }
        }

        enderSeeSession.put(viewer.getUniqueId(), target.getUniqueId());

        viewer.openInventory(gui);
    }

    public UUID getEnderSeeSession(Player viewer) {
        return enderSeeSession.get(viewer.getUniqueId());
    }

    public void removeEnderSeeSession(Player viewer) {
        enderSeeSession.remove(viewer.getUniqueId());
    }
}
