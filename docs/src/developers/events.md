# Events

EasyWarp fires custom events that other plugins can listen to. All events are cancellable.

## Available Events

### WarpCreateEvent

Fired when a warp is created.

**Package**: `eu.thumus.easywarp.api.events.WarpCreateEvent`

#### Event Details

```java
public class WarpCreateEvent extends Event implements Cancellable
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPlayer()` | `Player` | Player creating the warp |
| `getWarpName()` | `String` | Name of the warp |
| `getLocation()` | `Location` | Warp location |
| `getIconMaterial()` | `Material` | Icon material |
| `getWarpType()` | `String` | Warp system name |
| `isCancelled()` | `boolean` | Check if cancelled |
| `setCancelled(boolean)` | `void` | Cancel the event |

#### Example

```java
@EventHandler
public void onWarpCreate(WarpCreateEvent event) {
    Player player = event.getPlayer();
    String warpName = event.getWarpName();
    Location loc = event.getLocation();
    
    // Example: Prevent warps in certain areas
    if (isInProtectedRegion(loc)) {
        event.setCancelled(true);
        player.sendMessage("§cYou cannot create warps here!");
        return;
    }
    
    // Example: Log warp creation
    getLogger().info(player.getName() + " created warp: " + warpName);
    
    // Example: Notify admins
    Bukkit.broadcast("§e" + player.getName() + " created warp: " + warpName, 
                     "easywarp.admin.notify");
}
```

---

### WarpDeleteEvent

Fired when a warp is deleted.

**Package**: `eu.thumus.easywarp.api.events.WarpDeleteEvent`

#### Event Details

```java
public class WarpDeleteEvent extends Event implements Cancellable
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPlayer()` | `Player` | Player deleting the warp |
| `getWarpName()` | `String` | Name of the warp |
| `getWarpType()` | `String` | Warp system name |
| `isCancelled()` | `boolean` | Check if cancelled |
| `setCancelled(boolean)` | `void` | Cancel the event |

#### Example

```java
@EventHandler
public void onWarpDelete(WarpDeleteEvent event) {
    Player player = event.getPlayer();
    String warpName = event.getWarpName();
    
    // Example: Prevent deletion of important warps
    if (warpName.equalsIgnoreCase("Spawn") && !player.hasPermission("admin.delete.spawn")) {
        event.setCancelled(true);
        player.sendMessage("§cYou cannot delete the Spawn warp!");
        return;
    }
    
    // Example: Confirmation for global warps
    if (event.getWarpType().equals("ww")) {
        getLogger().warning(player.getName() + " deleted global warp: " + warpName);
    }
}
```

---

### WarpTeleportEvent

Fired when a player teleports using a warp.

**Package**: `eu.thumus.easywarp.api.events.WarpTeleportEvent`

#### Event Details

```java
public class WarpTeleportEvent extends Event implements Cancellable
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPlayer()` | `Player` | Player teleporting |
| `getWarpName()` | `String` | Name of the warp |
| `getLocation()` | `Location` | Destination location |
| `getWarpType()` | `String` | Warp system name |
| `isCancelled()` | `boolean` | Check if cancelled |
| `setCancelled(boolean)` | `void` | Cancel the teleport |

#### Example

```java
@EventHandler
public void onWarpTeleport(WarpTeleportEvent event) {
    Player player = event.getPlayer();
    Location destination = event.getLocation();
    String warpName = event.getWarpName();
    
    // Example: Prevent teleport in combat
    if (isInCombat(player)) {
        event.setCancelled(true);
        player.sendMessage("§cYou cannot teleport while in combat!");
        return;
    }
    
    // Example: Charge money for teleports
    if (!withdrawMoney(player, 10.0)) {
        event.setCancelled(true);
        player.sendMessage("§cYou need $10 to teleport!");
        return;
    }
    
    // Example: Play particle effect
    destination.getWorld().spawnParticle(
        Particle.PORTAL, 
        destination, 
        50
    );
    
    // Example: Log teleport
    getLogger().info(player.getName() + " teleported to: " + warpName);
}
```

## Complete Example Plugin

```java
package com.example.warplistener;

import eu.thumus.easywarp.api.events.WarpCreateEvent;
import eu.thumus.easywarp.api.events.WarpDeleteEvent;
import eu.thumus.easywarp.api.events.WarpTeleportEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WarpListenerPlugin extends JavaPlugin implements Listener {
    
    private Map<UUID, Long> combatTags = new HashMap<>();
    
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("WarpListener enabled!");
    }
    
    // High priority - run before other plugins
    @EventHandler(priority = EventPriority.HIGH)
    public void onWarpCreate(WarpCreateEvent event) {
        Player player = event.getPlayer();
        String warpName = event.getWarpName();
        Location loc = event.getLocation();
        
        // Example: Warp name validation
        if (warpName.length() > 16) {
            event.setCancelled(true);
            player.sendMessage("§cWarp name too long! Max 16 characters.");
            return;
        }
        
        // Example: World restrictions
        if (loc.getWorld().getName().equals("world_nether")) {
            if (!player.hasPermission("warps.create.nether")) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot create warps in the Nether!");
                return;
            }
        }
        
        // Example: Announce to admins
        Bukkit.getOnlinePlayers().stream()
            .filter(p -> p.hasPermission("warps.admin.notify"))
            .forEach(admin -> admin.sendMessage(
                "§7[WarpNotify] §e" + player.getName() + 
                " §7created warp: §a" + warpName
            ));
    }
    
    @EventHandler
    public void onWarpDelete(WarpDeleteEvent event) {
        Player player = event.getPlayer();
        String warpName = event.getWarpName();
        
        // Example: Protect certain warps
        String[] protectedWarps = {"Spawn", "Shop", "PvP"};
        for (String protected : protectedWarps) {
            if (warpName.equalsIgnoreCase(protected)) {
                if (!player.hasPermission("warps.delete.protected")) {
                    event.setCancelled(true);
                    player.sendMessage("§cThis warp is protected!");
                    return;
                }
            }
        }
        
        // Log deletion
        getLogger().warning(
            player.getName() + " deleted warp: " + warpName + 
            " (System: " + event.getWarpType() + ")"
        );
    }
    
    @EventHandler
    public void onWarpTeleport(WarpTeleportEvent event) {
        Player player = event.getPlayer();
        String warpName = event.getWarpName();
        
        // Example: Combat check
        if (combatTags.containsKey(player.getUniqueId())) {
            long combatEnd = combatTags.get(player.getUniqueId());
            if (System.currentTimeMillis() < combatEnd) {
                event.setCancelled(true);
                player.sendMessage("§cYou cannot teleport for " + 
                    ((combatEnd - System.currentTimeMillis()) / 1000) + 
                    " more seconds!");
                return;
            }
            combatTags.remove(player.getUniqueId());
        }
        
        // Example: Economy integration
        if (!hasPermission(player, "warps.free")) {
            double cost = 10.0;
            if (!withdrawMoney(player, cost)) {
                event.setCancelled(true);
                player.sendMessage("§cYou need $" + cost + " to teleport!");
                return;
            }
            player.sendMessage("§7Charged $" + cost + " for teleport");
        }
        
        // Example: Statistics tracking
        incrementStat(player, "warps_used");
        
        // Example: Custom message
        player.sendMessage("§aTeleporting to §e" + warpName + "§a...");
    }
    
    // Helper methods
    private boolean withdrawMoney(Player player, double amount) {
        // Implement with your economy plugin
        return true;
    }
    
    private boolean hasPermission(Player player, String perm) {
        return player.hasPermission(perm);
    }
    
    private void incrementStat(Player player, String stat) {
        // Implement your stats system
    }
}
```

## Event Priority

Use event priorities to control execution order:

```java
@EventHandler(priority = EventPriority.LOWEST)
public void onEarly(WarpCreateEvent event) {
    // Runs first
}

@EventHandler(priority = EventPriority.LOW)
public void onLow(WarpCreateEvent event) {
    // Runs second
}

@EventHandler(priority = EventPriority.NORMAL)  // Default
public void onNormal(WarpCreateEvent event) {
    // Runs third
}

@EventHandler(priority = EventPriority.HIGH)
public void onHigh(WarpCreateEvent event) {
    // Runs fourth
}

@EventHandler(priority = EventPriority.HIGHEST)
public void onHighest(WarpCreateEvent event) {
    // Runs fifth
}

@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
public void onMonitor(WarpCreateEvent event) {
    // Runs last, only if not cancelled
    // Use for logging, don't modify event here
}
```

## Common Use Cases

### 1. Economy Integration

```java
@EventHandler
public void onTeleport(WarpTeleportEvent event) {
    Player player = event.getPlayer();
    
    // Get cost from config
    double cost = getConfig().getDouble("warp-cost", 10.0);
    
    if (!economy.has(player, cost)) {
        event.setCancelled(true);
        player.sendMessage("§cNeed $" + cost + " to teleport!");
        return;
    }
    
    economy.withdrawPlayer(player, cost);
    player.sendMessage("§7Paid $" + cost);
}
```

### 2. Region Protection

```java
@EventHandler
public void onWarpCreate(WarpCreateEvent event) {
    Location loc = event.getLocation();
    
    // WorldGuard integration
    if (!canBuildAt(loc, event.getPlayer())) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("§cCannot create warps here!");
    }
}
```

### 3. Statistics Tracking

```java
@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
public void trackStats(WarpTeleportEvent event) {
    // Increment player stats
    UUID uuid = event.getPlayer().getUniqueId();
    stats.increment(uuid, "warps_used");
    
    // Track most used warps
    globalStats.increment("warp_usage", event.getWarpName());
}
```

### 4. Custom Cooldowns

```java
private Map<UUID, Long> customCooldowns = new HashMap<>();

@EventHandler
public void onTeleport(WarpTeleportEvent event) {
    UUID uuid = event.getPlayer().getUniqueId();
    
    if (customCooldowns.containsKey(uuid)) {
        long remaining = customCooldowns.get(uuid) - System.currentTimeMillis();
        if (remaining > 0) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cWait " + (remaining / 1000) + "s");
            return;
        }
    }
    
    // Set 30 second cooldown
    customCooldowns.put(uuid, System.currentTimeMillis() + 30000);
}
```

## Next Steps

- [API Overview](./api-overview.md)
- [Integration Examples](./integration.md)
- [View Events Source](https://github.com/Thumuss/EasyWarp/tree/main/src/main/java/eu/thumus/easywarp/api/events)
