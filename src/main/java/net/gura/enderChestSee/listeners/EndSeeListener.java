package net.gura.enderChestSee.listeners;

import net.gura.enderChestSee.gui.EnderSeeGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
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

        UUID targetUUID = gui.getEnderSeeSession(viewer);
        if (targetUUID == null) return;

        if (!gui.isEnderSeeGUI(viewer)) return;

        Player target = Bukkit.getPlayer(targetUUID);
        if (target == null) return;

        Inventory clicked = event.getClickedInventory();
        Inventory top = event.getView().getTopInventory();

        boolean isTop = clicked != null && clicked.equals(top);
        boolean shiftClick = event.isShiftClick();
        boolean numberKey = event.getClick().isKeyboardClick();

        if (!viewer.hasPermission("endsee.modify")) {
            if (isTop) {
                event.setCancelled(true);
                return;
            }
            if (shiftClick && clicked != null && !clicked.equals(top)) {
                event.setCancelled(true);
                return;
            }
            if (numberKey) {
                event.setCancelled(true);
                return;
            }
        }
        // Chequeamos permisos y hacemos sync
        if (viewer.hasPermission("endsee.modify") && isTop) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                syncToRealEnderChest(target, top);
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

        boolean affectsTop = event.getRawSlots().stream().anyMatch(slot -> slot < top.getSize());

        if (affectsTop && !viewer.hasPermission("endsee.modify")) {
            event.setCancelled(true);
            return;
        }

        if (affectsTop && viewer.hasPermission("endsee.modify")) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                syncToRealEnderChest(target, top);
            }, 1L);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player viewer)) return;

        UUID targetUUID = gui.getEnderSeeSession(viewer);
        if (targetUUID == null) return;

        Player target = Bukkit.getPlayer(targetUUID);

        if (target != null && viewer.hasPermission("endsee.modify")) {
            Inventory guiInv = event.getInventory();
            syncToRealEnderChest(target, guiInv);
            target.saveData();
        }

        gui.removeEnderSeeSession(viewer);
    }

    private void syncToRealEnderChest(Player target, Inventory guiInventory) {
        Inventory realEnderChest = target.getEnderChest();

        for (int i = 0; i < 27; i++) {
            realEnderChest.setItem(i, guiInventory.getItem(i));
        }

        if (target.getOpenInventory().getTopInventory().equals(realEnderChest)) {
            target.updateInventory();
        }
    }
}