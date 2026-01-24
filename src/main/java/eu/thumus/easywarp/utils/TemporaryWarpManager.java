package eu.thumus.easywarp.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Manages temporary warps that expire after a set time
 */
public class TemporaryWarpManager {
    private final JavaPlugin plugin;
    private final boolean enabled;
    private final long maxDuration;
    private final Map<String, Long> expirationTimes = new ConcurrentHashMap<>();

    public TemporaryWarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("temporary-warps.enabled", true);
        this.maxDuration = plugin.getConfig().getLong("temporary-warps.max-duration", 86400) * 1000; // Convert to ms
        
        if (enabled) {
            startCleanupTask();
        }
    }

    /**
     * Start periodic cleanup task for expired warps
     */
    private void startCleanupTask() {
        long interval = plugin.getConfig().getLong("temporary-warps.cleanup-interval", 300) * 20; // Convert to ticks
        
        new BukkitRunnable() {
            @Override
            public void run() {
                cleanupExpiredWarps();
            }
        }.runTaskTimerAsynchronously(plugin, interval, interval);
    }

    /**
     * Register a temporary warp
     */
    public boolean registerTemporaryWarp(String warpKey, long durationSeconds) {
        if (!enabled) {
            return false;
        }
        
        long durationMs = durationSeconds * 1000;
        if (durationMs > maxDuration) {
            return false; // Exceeds max duration
        }
        
        long expiresAt = System.currentTimeMillis() + durationMs;
        expirationTimes.put(warpKey, expiresAt);
        return true;
    }

    /**
     * Check if a warp is expired
     */
    public boolean isExpired(String warpKey) {
        Long expiresAt = expirationTimes.get(warpKey);
        if (expiresAt == null) {
            return false; // Not a temporary warp
        }
        return System.currentTimeMillis() > expiresAt;
    }

    /**
     * Get remaining time for a temporary warp in seconds
     */
    public long getRemainingTime(String warpKey) {
        Long expiresAt = expirationTimes.get(warpKey);
        if (expiresAt == null) {
            return -1; // Not a temporary warp
        }
        
        long remaining = (expiresAt - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }

    /**
     * Remove a temporary warp registration
     */
    public void unregisterWarp(String warpKey) {
        expirationTimes.remove(warpKey);
    }

    /**
     * Cleanup expired warps
     */
    private void cleanupExpiredWarps() {
        long currentTime = System.currentTimeMillis();
        expirationTimes.entrySet().removeIf(entry -> entry.getValue() < currentTime);
    }

    /**
     * Check if temporary warps are enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get max duration in seconds
     */
    public long getMaxDuration() {
        return maxDuration / 1000;
    }

    /**
     * Notify player about expiring warp (5 minutes warning)
     */
    public void checkExpiringWarps(Player player, String warpKey) {
        if (!enabled) return;
        
        long remaining = getRemainingTime(warpKey);
        if (remaining > 0 && remaining <= 300) { // 5 minutes
            if (plugin.getConfig().getBoolean("notifications.warp-expiring-soon", true)) {
                player.sendMessage("§e[EasyWarp] §cYour warp will expire in " + formatTime(remaining) + "!");
            }
        }
    }

    private String formatTime(long seconds) {
        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            return (seconds / 60) + " minutes";
        } else {
            return (seconds / 3600) + " hours";
        }
    }
}
