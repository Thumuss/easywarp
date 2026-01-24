package eu.thumus.easywarp.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a warp is deleted
 */
public class WarpDeleteEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    
    private final Player player;
    private final String warpName;
    private final String warpType;
    
    public WarpDeleteEvent(Player player, String warpName, String warpType) {
        this.player = player;
        this.warpName = warpName;
        this.warpType = warpType;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public String getWarpName() {
        return warpName;
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
