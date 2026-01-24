package eu.thumus.easywarp.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages cooldowns for warp teleportation
 */
public class CooldownManager {
    
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();
    private final int defaultCooldownSeconds;
    
    public CooldownManager(int defaultCooldownSeconds) {
        this.defaultCooldownSeconds = defaultCooldownSeconds;
    }
    
    /**
     * Check if a player has a cooldown for a specific warp type
     * @param player The player to check
     * @param warpType The warp type (e.g., "warp", "pwarp")
     * @return true if the player is on cooldown
     */
    public boolean hasCooldown(Player player, String warpType) {
        UUID playerId = player.getUniqueId();
        
        if (!cooldowns.containsKey(playerId)) {
            return false;
        }
        
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (!playerCooldowns.containsKey(warpType)) {
            return false;
        }
        
        long cooldownEnd = playerCooldowns.get(warpType);
        long currentTime = System.currentTimeMillis();
        
        if (currentTime >= cooldownEnd) {
            // Cooldown expired
            playerCooldowns.remove(warpType);
            return false;
        }
        
        return true;
    }
    
    /**
     * Get remaining cooldown time in seconds
     * @param player The player to check
     * @param warpType The warp type
     * @return Remaining seconds, or 0 if no cooldown
     */
    public int getRemainingCooldown(Player player, String warpType) {
        UUID playerId = player.getUniqueId();
        
        if (!cooldowns.containsKey(playerId)) {
            return 0;
        }
        
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (!playerCooldowns.containsKey(warpType)) {
            return 0;
        }
        
        long cooldownEnd = playerCooldowns.get(warpType);
        long currentTime = System.currentTimeMillis();
        long remaining = cooldownEnd - currentTime;
        
        if (remaining <= 0) {
            playerCooldowns.remove(warpType);
            return 0;
        }
        
        return (int) Math.ceil(remaining / 1000.0);
    }
    
    /**
     * Set a cooldown for a player
     * @param player The player
     * @param warpType The warp type
     */
    public void setCooldown(Player player, String warpType) {
        setCooldown(player, warpType, defaultCooldownSeconds);
    }
    
    /**
     * Set a cooldown for a player with custom duration
     * @param player The player
     * @param warpType The warp type
     * @param seconds Cooldown duration in seconds
     */
    public void setCooldown(Player player, String warpType, int seconds) {
        UUID playerId = player.getUniqueId();
        long cooldownEnd = System.currentTimeMillis() + (seconds * 1000L);
        
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                 .put(warpType, cooldownEnd);
    }
    
    /**
     * Clear cooldown for a player
     * @param player The player
     * @param warpType The warp type
     */
    public void clearCooldown(Player player, String warpType) {
        UUID playerId = player.getUniqueId();
        
        if (cooldowns.containsKey(playerId)) {
            cooldowns.get(playerId).remove(warpType);
        }
    }
    
    /**
     * Clear all cooldowns for a player
     * @param player The player
     */
    public void clearAllCooldowns(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
    
    /**
     * Clear all cooldowns
     */
    public void clearAll() {
        cooldowns.clear();
    }
}
