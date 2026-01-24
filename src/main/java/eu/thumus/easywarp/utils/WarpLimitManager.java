package eu.thumus.easywarp.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages warp limits per player based on permissions
 */
public class WarpLimitManager {
    private final JavaPlugin plugin;
    private final int defaultLimit;

    public WarpLimitManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.defaultLimit = plugin.getConfig().getInt("warp-limits.default", 5);
    }

    /**
     * Get the maximum number of warps a player can have
     */
    public int getLimit(Player player) {
        // Check for unlimited permission
        if (player.hasPermission("easywarp.limit.unlimited")) {
            return -1; // Unlimited
        }
        
        // Check for specific tier permissions
        FileConfiguration config = plugin.getConfig();
        for (String key : config.getConfigurationSection("warp-limits").getKeys(false)) {
            if (key.equals("default")) continue;
            
            if (player.hasPermission("easywarp.limit." + key)) {
                return config.getInt("warp-limits." + key, defaultLimit);
            }
        }
        
        return defaultLimit;
    }

    /**
     * Check if player can create more warps
     */
    public boolean canCreateWarp(Player player, int currentWarpCount) {
        int limit = getLimit(player);
        return limit == -1 || currentWarpCount < limit;
    }

    /**
     * Get remaining warps for player
     */
    public int getRemaining(Player player, int currentWarpCount) {
        int limit = getLimit(player);
        if (limit == -1) return -1; // Unlimited
        return Math.max(0, limit - currentWarpCount);
    }
}
