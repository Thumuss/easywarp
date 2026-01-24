package eu.thumus.easywarp.utils;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Manages conditional warp access based on various criteria
 */
public class WarpConditionsManager {
    private final JavaPlugin plugin;
    private final boolean enabled;

    public WarpConditionsManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("warps.players.features.conditions", true);
    }

    /**
     * Check if player meets all conditions to use a warp
     */
    public boolean meetsConditions(Player player, Map<String, String> conditions) {
        if (!enabled || conditions == null || conditions.isEmpty()) {
            return true;
        }

        for (Map.Entry<String, String> condition : conditions.entrySet()) {
            if (!checkCondition(player, condition.getKey(), condition.getValue())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check a specific condition
     */
    private boolean checkCondition(Player player, String type, String value) {
        switch (type.toLowerCase()) {
            case "permission":
                return player.hasPermission(value);
                
            case "world":
                return player.getWorld().getName().equalsIgnoreCase(value);
                
            case "worldtype":
                World.Environment env = player.getWorld().getEnvironment();
                return env.name().equalsIgnoreCase(value);
                
            case "minlevel":
                try {
                    int minLevel = Integer.parseInt(value);
                    return player.getLevel() >= minLevel;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "maxlevel":
                try {
                    int maxLevel = Integer.parseInt(value);
                    return player.getLevel() <= maxLevel;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "gamemode":
                return player.getGameMode().name().equalsIgnoreCase(value);
                
            case "health":
                try {
                    double minHealth = Double.parseDouble(value);
                    return player.getHealth() >= minHealth;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "minplayers":
                try {
                    int minPlayers = Integer.parseInt(value);
                    return plugin.getServer().getOnlinePlayers().size() >= minPlayers;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "maxplayers":
                try {
                    int maxPlayers = Integer.parseInt(value);
                    return plugin.getServer().getOnlinePlayers().size() <= maxPlayers;
                } catch (NumberFormatException e) {
                    return false;
                }
                
            case "time":
                // Format: "day" or "night"
                long time = player.getWorld().getTime();
                boolean isDay = time < 13000 || time > 23000;
                return (value.equalsIgnoreCase("day") && isDay) || 
                       (value.equalsIgnoreCase("night") && !isDay);
                
            case "weather":
                // Format: "clear" or "storm"
                boolean hasStorm = player.getWorld().hasStorm();
                return (value.equalsIgnoreCase("clear") && !hasStorm) || 
                       (value.equalsIgnoreCase("storm") && hasStorm);
                
            default:
                plugin.getLogger().warning("Unknown condition type: " + type);
                return true; // Unknown conditions are ignored
        }
    }

    /**
     * Get human-readable failure reason
     */
    public String getFailureReason(Player player, Map<String, String> conditions) {
        if (!enabled || conditions == null || conditions.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> condition : conditions.entrySet()) {
            if (!checkCondition(player, condition.getKey(), condition.getValue())) {
                return formatFailureReason(condition.getKey(), condition.getValue());
            }
        }

        return null;
    }

    private String formatFailureReason(String type, String value) {
        switch (type.toLowerCase()) {
            case "permission":
                return "Missing permission: " + value;
            case "world":
                return "Must be in world: " + value;
            case "worldtype":
                return "Must be in " + value + " world";
            case "minlevel":
                return "Minimum level required: " + value;
            case "maxlevel":
                return "Maximum level allowed: " + value;
            case "gamemode":
                return "Must be in gamemode: " + value;
            case "health":
                return "Minimum health required: " + value;
            case "minplayers":
                return "Minimum players online: " + value;
            case "maxplayers":
                return "Maximum players online: " + value;
            case "time":
                return "Can only be used during: " + value;
            case "weather":
                return "Can only be used in weather: " + value;
            default:
                return "Condition not met: " + type + "=" + value;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }
}
