package net.gura.enderChestSee;

import net.gura.enderChestSee.commands.EndSee;
import net.gura.enderChestSee.gui.EnderSeeGUI;
import net.gura.enderChestSee.handler.MessageHandler;
import net.gura.enderChestSee.listeners.EndSeeListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnderChestSee extends JavaPlugin {

    private EnderSeeGUI gui;
    private MessageHandler messageHandler;

    @Override
    public void onEnable() {

        messageHandler = new MessageHandler(this);
        this.gui = new EnderSeeGUI();

        //Register Commands
        getCommand("endersee").setExecutor(new EndSee(this, messageHandler, gui));

        //Register Listeners
        getServer().getPluginManager().registerEvents(new EndSeeListener(this, gui), this);
        getServer().getConsoleSender().sendMessage("[EnderChestSee] Plugin enabled!");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : getServer().getOnlinePlayers()) {
            player.saveData();
        }

        getServer().getConsoleSender().sendMessage("[EnderChestSee] Plugin disabled!");
    }
}
