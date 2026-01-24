package eu.thumus.easywarp.placeholders;

import eu.thumus.easywarp.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * PlaceholderAPI expansion for EasyWarp
 * Placeholders:
 * - %easywarp_count% - Number of warps player has
 * - %easywarp_max% - Maximum warps allowed
 * - %easywarp_remaining% - Remaining warp slots
 * - %easywarp_cooldown% - Cooldown remaining in seconds
 * - %easywarp_last_warp% - Last used warp name
 * - %easywarp_most_used% - Most used warp name
 * - %easywarp_total_uses% - Total warp teleportations
 */
public class WarpPlaceholders extends PlaceholderExpansion {
    private final Main plugin;

    public WarpPlaceholders(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "easywarp";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // Keep expansion loaded even if PlaceholderAPI reloads
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        // %easywarp_count%
        if (params.equals("count")) {
            return String.valueOf(getPlayerWarpCount(player));
        }

        // %easywarp_max%
        if (params.equals("max")) {
            int max = plugin.getWarpLimitManager().getLimit(player);
            return max == -1 ? "∞" : String.valueOf(max);
        }

        // %easywarp_remaining%
        if (params.equals("remaining")) {
            int count = getPlayerWarpCount(player);
            int remaining = plugin.getWarpLimitManager().getRemaining(player, count);
            return remaining == -1 ? "∞" : String.valueOf(remaining);
        }

        // %easywarp_cooldown%
        if (params.equals("cooldown")) {
            // Get cooldown for default warp type "players"
            int cooldown = plugin.getCooldownManager().getRemainingCooldown(player, "players");
            return String.valueOf(cooldown);
        }

        // %easywarp_last_warp%
        if (params.equals("last_warp")) {
            // This would require storing last warp usage data
            // For now, return a placeholder value
            return "none";
        }

        // %easywarp_most_used%
        if (params.equals("most_used")) {
            // This would require storing warp usage statistics
            // For now, return a placeholder value
            return "none";
        }

        // %easywarp_total_uses%
        if (params.equals("total_uses")) {
            // This would require storing total teleportation count
            // For now, return a placeholder value
            return "0";
        }

        return null; // Placeholder not recognized
    }
    
    /**
     * Get the number of warps a player has
     */
    private int getPlayerWarpCount(Player player) {
        try {
            // Try to get player warps from the warp system
            // This assumes there's a "players" warp system in config
            var commands = plugin.getCommands();
            if (commands != null) {
                var warpCommands = commands.getWCMap().get("players");
                if (warpCommands != null) {
                    var warpBase = warpCommands.getWb();
                    if (warpBase != null) {
                        var inventory = warpBase.inv.get(player.getUniqueId().toString());
                        if (inventory != null) {
                            return inventory.getSize() - (int) inventory.all(org.bukkit.Material.AIR).size();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Silently fail and return 0
        }
        return 0;
    }
}
