package eu.thumus.easywarp.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import eu.thumus.easywarp.Main;
import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.data.config.Config;
import eu.thumus.easywarp.data.config.WarpConfig;

/**
 * Public API for EasyWarp plugin
 * 
 * Usage example:
 * <pre>
 * EasyWarpAPI api = EasyWarpAPI.getInstance();
 * if (api != null) {
 *     Collection&lt;String&gt; warps = api.getWarpNames("players", player);
 *     api.teleportToWarp(player, "spawn", "ww");
 * }
 * </pre>
 */
public class EasyWarpAPI {
    
    private static EasyWarpAPI instance;
    private final Main plugin;
    
    private EasyWarpAPI(Main plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Initialize the API (called by plugin on enable)
     */
    public static void initialize(Main plugin) {
        instance = new EasyWarpAPI(plugin);
    }
    
    /**
     * Get the API instance
     * @return The API instance, or null if plugin is not loaded
     */
    public static EasyWarpAPI getInstance() {
        return instance;
    }
    
    /**
     * Get all warp types configured
     * @return Collection of warp type names (e.g., "players", "ww")
     */
    public Collection<String> getWarpTypes() {
        return Config.getWarps().keySet();
    }
    
    /**
     * Get all warp names for a specific type and player
     * @param warpType The warp type (e.g., "players", "ww")
     * @param player The player (required for player-specific warps)
     * @return Collection of warp names, or null if warp type doesn't exist
     */
    public Collection<String> getWarpNames(String warpType, Player player) {
        WarpConfig config = Config.getWarps().get(warpType);
        if (config == null) return null;
        
        WarpCommandsBase wcb = plugin.getCommands().getWCMap().get(warpType);
        if (wcb == null) return null;
        
        String key = config.isUsingPlayerUID() ? warpType + "." + player.getUniqueId().toString() : warpType;
        
        // Read directly from configuration
        org.bukkit.configuration.file.FileConfiguration fc = wcb.getWb().getFileConfiguration();
        org.bukkit.configuration.ConfigurationSection section = fc.getConfigurationSection(key);
        
        if (section == null) {
            return java.util.Collections.emptyList();
        }
        
        return section.getKeys(false);
    }
    
    /**
     * Get the location of a warp
     * @param warpType The warp type
     * @param warpName The warp name
     * @param player The player (for player-specific warps)
     * @return The location, or null if not found
     */
    public Location getWarpLocation(String warpType, String warpName, Player player) {
        WarpConfig config = Config.getWarps().get(warpType);
        if (config == null) return null;
        
        WarpCommandsBase wcb = plugin.getCommands().getWCMap().get(warpType);
        if (wcb == null) return null;
        
        // Build the config path
        String key = config.isUsingPlayerUID() ? warpType + "." + player.getUniqueId().toString() : warpType;
        String path = key + "." + warpName;
        
        // Get the configuration
        org.bukkit.configuration.file.FileConfiguration fc = wcb.getWb().getFileConfiguration();
        if (!fc.contains(path)) {
            return null;
        }
        
        try {
            // Read location data from config
            String worldName = fc.getString(path + ".world", "world");
            org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
            
            if (world == null) {
                plugin.console.warning("World '" + worldName + "' not found for warp: " + warpName);
                return null;
            }
            
            double x = fc.getDouble(path + ".x");
            double y = fc.getDouble(path + ".y");
            double z = fc.getDouble(path + ".z");
            float yaw = Float.parseFloat(fc.getString(path + ".yaw", "0.0f").replace("f", ""));
            float pitch = Float.parseFloat(fc.getString(path + ".pitch", "0.0f").replace("f", ""));
            
            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception e) {
            plugin.console.warning("Error loading warp location for " + warpName + ": " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if a warp exists
     * @param warpType The warp type
     * @param warpName The warp name
     * @param player The player (for player-specific warps)
     * @return true if the warp exists
     */
    public boolean warpExists(String warpType, String warpName, Player player) {
        return getWarpLocation(warpType, warpName, player) != null;
    }
    
    /**
     * Teleport a player to a warp
     * @param player The player to teleport
     * @param warpName The warp name
     * @param warpType The warp type
     * @return true if successful
     */
    public boolean teleportToWarp(Player player, String warpName, String warpType) {
        Location location = getWarpLocation(warpType, warpName, player);
        if (location == null) return false;
        
        return player.teleport(location);
    }
    
    /**
     * Get warp configuration
     */
    public WarpConfig getWarpConfig(String warpType) {
        return Config.getWarps().get(warpType);
    }
    
    /**
     * Get all warp configurations
     */
    public Map<String, WarpConfig> getAllWarpConfigs() {
        return new HashMap<>(Config.getWarps());
    }
}
