package eu.thumus.easywarp.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages player notifications for various warp events
 */
public class NotificationManager {
    private final JavaPlugin plugin;
    private final boolean cooldownReady;
    private final boolean warpExpiring;
    private final boolean warpShared;
    private final boolean warpUpdated;

    public NotificationManager(JavaPlugin pl) {
        this.plugin = pl;
        this.cooldownReady = plugin.getConfig().getBoolean("notifications.cooldown-ready", true);
        this.warpExpiring = plugin.getConfig().getBoolean("notifications.warp-expiring-soon", true);
        this.warpShared = plugin.getConfig().getBoolean("notifications.warp-shared", true);
        this.warpUpdated = plugin.getConfig().getBoolean("notifications.warp-updated", true);
    }

    /**
     * Notify player that cooldown is ready
     */
    public void notifyCooldownReady(Player player, String warpName) {
        if (!cooldownReady) return;
        
        player.sendMessage("§e[EasyWarp] §aYour cooldown is ready! You can now teleport to §6" + warpName + "§a.");
        playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

    /**
     * Notify player that their warp is expiring soon
     */
    public void notifyWarpExpiring(Player player, String warpName, long secondsRemaining) {
        if (!warpExpiring) return;
        
        player.sendMessage("§e[EasyWarp] §cYour warp §6" + warpName + "§c will expire in " + formatTime(secondsRemaining) + "!");
        playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING);
    }

    /**
     * Notify player that someone shared a warp with them
     */
    public void notifyWarpShared(Player player, String warpName, String ownerName) {
        if (!warpShared) return;
        
        player.sendMessage("§e[EasyWarp] §a" + ownerName + " has shared their warp §6" + warpName + "§a with you!");
        playSound(player, Sound.ENTITY_PLAYER_LEVELUP);
    }

    /**
     * Notify player that their warp was updated
     */
    public void notifyWarpUpdated(Player player, String warpName) {
        if (!warpUpdated) return;
        
        player.sendMessage("§e[EasyWarp] §aYour warp §6" + warpName + "§a has been updated.");
        playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    }

    /**
     * Play a sound to the player (with error handling)
     */
    private void playSound(Player player, Sound sound) {
        try {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        } catch (Exception e) {
            // Ignore if sound doesn't exist in this version
        }
    }

    /**
     * Format time duration for display
     */
    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            return (seconds / 60) + " minutes";
        } else if (seconds < 86400) {
            return (seconds / 3600) + " hours";
        } else {
            return (seconds / 86400) + " days";
        }
    }

    // Getters for notification settings
    public boolean isCooldownReadyEnabled() {
        return cooldownReady;
    }

    public boolean isWarpExpiringEnabled() {
        return warpExpiring;
    }

    public boolean isWarpSharedEnabled() {
        return warpShared;
    }

    public boolean isWarpUpdatedEnabled() {
        return warpUpdated;
    }
}
