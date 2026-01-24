package eu.thumus.easywarp.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages confirmation dialogs for dangerous operations
 */
public class ConfirmationManager {
    private final JavaPlugin plugin;
    private final ConcurrentHashMap<UUID, PendingAction> pendingActions = new ConcurrentHashMap<>();
    private final int timeoutSeconds;

    public ConfirmationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.timeoutSeconds = plugin.getConfig().getInt("protection.confirmation-timeout", 10);
    }

    /**
     * Request confirmation from player
     */
    public void requestConfirmation(Player player, String action, Runnable onConfirm) {
        UUID uuid = player.getUniqueId();
        
        // Cancel any existing pending action
        if (pendingActions.containsKey(uuid)) {
            pendingActions.get(uuid).cancel();
        }
        
        PendingAction pending = new PendingAction(player, action, onConfirm, timeoutSeconds);
        pendingActions.put(uuid, pending);
        
        player.sendMessage("§e[EasyWarp] §cAre you sure you want to " + action + "?");
        player.sendMessage("§e[EasyWarp] §6Type /warp confirm to proceed or /warp cancel to abort.");
        player.sendMessage("§e[EasyWarp] §7This will expire in " + timeoutSeconds + " seconds.");
        
        // Schedule timeout
        new BukkitRunnable() {
            @Override
            public void run() {
                if (pendingActions.remove(uuid) != null) {
                    player.sendMessage("§e[EasyWarp] §cConfirmation expired.");
                }
            }
        }.runTaskLater(plugin, timeoutSeconds * 20L);
    }

    /**
     * Confirm pending action
     */
    public boolean confirm(Player player) {
        UUID uuid = player.getUniqueId();
        PendingAction pending = pendingActions.remove(uuid);
        
        if (pending == null) {
            return false;
        }
        
        pending.execute();
        return true;
    }

    /**
     * Cancel pending action
     */
    public boolean cancel(Player player) {
        UUID uuid = player.getUniqueId();
        PendingAction pending = pendingActions.remove(uuid);
        
        if (pending == null) {
            return false;
        }
        
        pending.cancel();
        return true;
    }

    /**
     * Check if player has a pending confirmation
     */
    public boolean hasPending(Player player) {
        return pendingActions.containsKey(player.getUniqueId());
    }

    private static class PendingAction {
        private final Player player;
        private final String action;
        private final Runnable onConfirm;
        private boolean cancelled = false;

        public PendingAction(Player player, String action, Runnable onConfirm, int timeout) {
            this.player = player;
            this.action = action;
            this.onConfirm = onConfirm;
        }

        public void execute() {
            if (!cancelled) {
                onConfirm.run();
                player.sendMessage("§e[EasyWarp] §aAction confirmed.");
            }
        }

        public void cancel() {
            cancelled = true;
            player.sendMessage("§e[EasyWarp] §7Action cancelled.");
        }
    }
}
