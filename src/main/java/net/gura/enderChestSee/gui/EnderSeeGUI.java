package net.gura.enderChestSee.gui;

import net.gura.enderChestSee.handler.MessageHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderSeeGUI {
    private MessageHandler messageHandler;
    private final Map<UUID, UUID> enderSeeSession = new HashMap<>();
    private final String GUI_TITLE_PREFIX = "ENDERSEE";

    public EnderSeeGUI(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    public void openEnderChest(Player viewer, Player target) {
        if (!viewer.hasPermission("endersee.use")) {
            viewer.sendMessage(messageHandler.get("no-permission"));
            return;
        }
        Component title = messageHandler != null ?
                messageHandler.get("enderchest-gui", Map.of("player", target.getName())) :
                Component.text(target.getName() + "'s Enderchest");

        Inventory gui = Bukkit.createInventory(viewer, 27, title);

        Inventory targetEnderChest = target.getEnderChest();
        for (int i = 0; i < 27; i++) {
            ItemStack item = targetEnderChest.getItem(i);
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

    public boolean isEnderSeeGUI(Player viewer) {
        return enderSeeSession.containsKey(viewer.getUniqueId());
    }

    /**
     * No usar solo en caso de que sea totalmente necesario
     */
    @Deprecated
    public boolean isEnderSeeGUI(String title) {
        return title != null && title.startsWith(GUI_TITLE_PREFIX);
    }
}