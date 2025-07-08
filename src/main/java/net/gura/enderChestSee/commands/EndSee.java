package net.gura.enderChestSee.commands;

import net.gura.enderChestSee.gui.EnderSeeGUI;
import net.gura.enderChestSee.handler.MessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class EndSee implements CommandExecutor {

    private final JavaPlugin plugin;
    private final EnderSeeGUI gui;
    private MessageHandler messageHandler;

    public EndSee(JavaPlugin plugin, MessageHandler messageHandler, EnderSeeGUI gui) {
        this.plugin = plugin;
        this.messageHandler = messageHandler;
        this.gui = gui;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("endersee.reload")) {
                sender.sendMessage(messageHandler.get("no-permission"));
                return true;
            }

            messageHandler.reload();
            sender.sendMessage(messageHandler.get("reload-complete"));
            return true;
        }

        if (!(sender instanceof Player viewer)) {
            sender.sendMessage(messageHandler.get("only-players"));
            return true;
        }

        if (!viewer.hasPermission("endersee.use")) {
            viewer.sendMessage(messageHandler.get("no-permission"));
            return true;
        }

        if (args.length != 1) {
            viewer.sendMessage(messageHandler.get("invalid-usage"));
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            viewer.sendMessage(messageHandler.get("player-not-found"));
            return true;
        }

        gui.openEnderChest(viewer, target);
        viewer.sendMessage(messageHandler.get("open-endchest", Map.of("player", target.getName())));
        return true;
    }
}
