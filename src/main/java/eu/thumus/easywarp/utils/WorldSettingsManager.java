package eu.thumus.easywarp.utils;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages per-world teleportation settings
 */
public class WorldSettingsManager {
    private final JavaPlugin plugin;

    public WorldSettingsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get teleportation delay for a specific world
     */
    public int getTeleportDelay(World world) {
        ConfigurationSection worldSettings = plugin.getConfig().getConfigurationSection("world-settings");
        if (worldSettings != null && worldSettings.contains(world.getName())) {
            return worldSettings.getInt(world.getName() + ".teleportation.delay", 
                                      plugin.getConfig().getInt("teleportation.delay", 0));
        }
        return plugin.getConfig().getInt("teleportation.delay", 0);
    }

    /**
     * Get cooldown for a specific world
     */
    public int getCooldown(World world) {
        ConfigurationSection worldSettings = plugin.getConfig().getConfigurationSection("world-settings");
        if (worldSettings != null && worldSettings.contains(world.getName())) {
            return worldSettings.getInt(world.getName() + ".cooldown.default", 
                                      plugin.getConfig().getInt("cooldown.default", 3));
        }
        return plugin.getConfig().getInt("cooldown.default", 3);
    }

    /**
     * Check if movement cancellation is enabled for a world
     */
    public boolean isCancelOnMove(World world) {
        ConfigurationSection worldSettings = plugin.getConfig().getConfigurationSection("world-settings");
        if (worldSettings != null && worldSettings.contains(world.getName())) {
            return worldSettings.getBoolean(world.getName() + ".teleportation.cancel-on-move", 
                                           plugin.getConfig().getBoolean("teleportation.cancel-on-move", false));
        }
        return plugin.getConfig().getBoolean("teleportation.cancel-on-move", false);
    }
}
