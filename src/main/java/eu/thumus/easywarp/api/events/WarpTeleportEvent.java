package eu.thumus.easywarp.api.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a player is about to teleport to a warp
 */
public class WarpTeleportEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final String warpName;
    private final Location location;
    private final String warpType;
    
    public WarpTeleportEvent(Player player, String warpName, Location location, String warpType) {
        this.player = player;
        this.warpName = warpName;
        this.location = location;
        this.warpType = warpType;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getWarpName() {
        return warpName;
    }
    
    public Location getLocation() {
        return location;
    }
    
    /**
     * Get the warp type (e.g., "players", "ww")
     */
    public String getWarpType() {
        return warpType;
    }
    
    @Override
    public boolean isCancelled() {
        return cancelled;
    }
    
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
