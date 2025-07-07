package net.gura.enderChestSee.listeners;

import net.gura.enderChestSee.gui.EnderSeeGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class EndSeeListener implements Listener {

    private final JavaPlugin plugin;
    private final EnderSeeGUI gui;

    public EndSeeListener(JavaPlugin plugin, EnderSeeGUI gui) {
        this.plugin = plugin;
        this.gui = gui;
    }

    @EventHandler
    public void onEnderSeeClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player viewer)) return;
        if (event.getView().getTopInventory().getType() != InventoryType.ENDER_CHEST) return;

        UUID targetUUID = gui.getEnderSeeSession(viewer);
        if (targetUUID == null) return;

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) return;

        if (event.getView().getTopInventory().equals(event.getClickedInventory())) {

            if (!viewer.hasPermission("endsee.modify")) {
                event.setCancelled(true);
                viewer.updateInventory();
                return;
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Inventory gui = event.getView().getTopInventory();
                Inventory realEnderChest = target.getEnderChest();

                for (int i = 0; i < 27; i++) {
                    realEnderChest.setItem(i, gui.getItem(i));
                }
                target.updateInventory();
            }, 1L);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player viewer)) return;

        UUID targetUUID = gui.getEnderSeeSession(viewer);
        if (targetUUID == null) return;

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) return;

        Inventory top = event.getView().getTopInventory();

        if (!viewer.hasPermission("endsee.modify")) {
            event.setCancelled(true);
            viewer.updateInventory();
            return;
        }

        for (int slot : event.getRawSlots()) {
            if (slot < top.getSize()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    Inventory gui = event.getView().getTopInventory();
                    Inventory realEnderChest = target.getEnderChest();

                    for (int i = 0; i < 27; i++) {
                        realEnderChest.setItem(i, gui.getItem(i));
                    }
                    target.updateInventory();
                }, 1L);
                break;
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player viewer)) return;

        UUID targetUUID = gui.getEnderSeeSession(viewer);
        if (targetUUID == null) return;

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) return;
        Inventory guiInv = event.getInventory();
        Inventory realEnderChest = target.getEnderChest();

        for (int i = 0; i < 27; i++) {
            realEnderChest.setItem(i, guiInv.getItem(i));
        }

        target.updateInventory();
        target.saveData();

        gui.removeEnderSeeSession(viewer);
    }
}