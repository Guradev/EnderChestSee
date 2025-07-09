package net.gura.enderChestSee.utils;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import net.gura.enderChestSee.EnderChestSee;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.function.Consumer;

public final class CompatibilityUtils {

    private static boolean isFolia;

    static {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            isFolia = true;

        } catch (final ClassNotFoundException e) {
            isFolia = false;
        }
    }

    public static void run(Runnable runnable) {
        if (isFolia) {
            Bukkit.getGlobalRegionScheduler()
                    .execute(EnderChestSee.getInstance(), runnable);
        } else {
            Bukkit.getScheduler().runTask(EnderChestSee.getInstance(), runnable);
        }
    }

    public static void runForPlayer(Player player, Runnable runnable) {
        if (isFolia) {
            player.getScheduler().run(EnderChestSee.getInstance(),
                    task -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(EnderChestSee.getInstance(), runnable);
        }
    }

    public static void runForAllPlayersSafe(JavaPlugin plugin, Consumer<Player> playerTask) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

        for (Player player : players) {
            if (isFolia) {
                player.getScheduler().run(plugin, task -> {
                    try {
                        playerTask.accept(player);
                    } catch (Exception e) {
                        plugin.getLogger().severe("Error while running task for player in folia " + player.getName() + ": " + e.getMessage());
                    }
                }, null);
            } else {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    try {
                        playerTask.accept(player);
                    } catch (Exception e) {
                        plugin.getLogger().severe("Error while running task for player in paper " + player.getName() + ": " + e.getMessage());
                    }
                });
            }
        }
    }

    public static Task runTaskLaterForPlayer(Player player, Runnable runnable, long delayTicks) {
        if (isFolia) {
            return new Task(player.getScheduler().runDelayed(EnderChestSee.getInstance(),
                    task -> runnable.run(), null, delayTicks));
        } else {
            return new Task(Bukkit.getScheduler().runTaskLater(EnderChestSee.getInstance(),
                    runnable, delayTicks));
        }
    }

    public static Task runLater(Runnable runnable, long delayTicks) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler()
                    .runDelayed(EnderChestSee.getInstance(), t -> runnable.run(), delayTicks)
            );
        } else {
            return new Task(Bukkit.getScheduler().runTaskLater(EnderChestSee.getInstance(), runnable, delayTicks));
        }
    }

    public static void runAsync(Runnable runnable) {
        if (isFolia) {
            Bukkit.getAsyncScheduler().runNow(EnderChestSee.getInstance(),
                    task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(EnderChestSee.getInstance(), runnable);
        }
    }

    public static Task runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (isFolia) {
            return new Task(Bukkit.getGlobalRegionScheduler()
                    .runAtFixedRate(EnderChestSee.getInstance(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks)
            );
        } else {
            return new Task(Bukkit.getScheduler().runTaskTimer(EnderChestSee.getInstance(), runnable, delayTicks, periodTicks));
        }
    }

    public static boolean isFolia() {
        return isFolia;
    }

    public static class Task {

        private Object foliaTask;
        private BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
        }

        Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        public void cancel() {
            if (foliaTask != null)
                ((ScheduledTask) foliaTask).cancel();
            else
                bukkitTask.cancel();
        }
    }
}