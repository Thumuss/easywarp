# API Overview

EasyWarp provides a public API for other plugins to integrate with the warp system.

## Getting Started

### Maven Dependency

Add EasyWarp as a dependency in your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>eu.thumus</groupId>
        <artifactId>easywarp</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### plugin.yml Dependency

Declare EasyWarp as a dependency:

```yaml
name: MyPlugin
depend: [EasyWarp]
```

Or as a soft dependency:

```yaml
softdepend: [EasyWarp]
```

## Getting the API Instance

```java
import eu.thumus.easywarp.api.EasyWarpAPI;

public class MyPlugin extends JavaPlugin {
    private EasyWarpAPI warpAPI;
    
    @Override
    public void onEnable() {
        warpAPI = EasyWarpAPI.getInstance();
        
        if (warpAPI == null) {
            getLogger().warning("EasyWarp not found!");
            return;
        }
        
        getLogger().info("Successfully hooked into EasyWarp!");
    }
}
```

## API Methods

### Get Warp Location

Retrieve the location of a specific warp.

```java
Location getWarpLocation(String warpType, String key, String warpName)
```

**Parameters**:
- `warpType` - Warp system name (from config.yml)
- `key` - Player UUID (for per-player warps) or warp system name (for shared warps)
- `warpName` - Name of the warp

**Returns**: `Location` or `null` if warp doesn't exist

**Example**:

```java
// Get a player's personal warp
String playerUUID = player.getUniqueId().toString();
Location homeLocation = warpAPI.getWarpLocation("players", playerUUID, "Home");

if (homeLocation != null) {
    player.teleport(homeLocation);
} else {
    player.sendMessage("Warp 'Home' not found!");
}

// Get a global warp
Location spawnWarp = warpAPI.getWarpLocation("ww", "ww", "Spawn");
```

### Get All Warp Names

Get a set of all warp names for a player or system.

```java
Set<String> getWarpNames(String warpType, String key)
```

**Parameters**:
- `warpType` - Warp system name
- `key` - Player UUID or system name

**Returns**: `Set<String>` of warp names, or empty set if none exist

**Example**:

```java
// Get all player warps
String playerUUID = player.getUniqueId().toString();
Set<String> playerWarps = warpAPI.getWarpNames("players", playerUUID);

player.sendMessage("You have " + playerWarps.size() + " warps:");
for (String warpName : playerWarps) {
    player.sendMessage("- " + warpName);
}

// Get all global warps
Set<String> globalWarps = warpAPI.getWarpNames("ww", "ww");
```

### Check if Warp Exists

```java
// Simple existence check
String playerUUID = player.getUniqueId().toString();
Set<String> warps = warpAPI.getWarpNames("players", playerUUID);

if (warps.contains("Home")) {
    player.sendMessage("You have a Home warp!");
}
```

## Complete Example

```java
package com.example.myplugin;

import eu.thumus.easywarp.api.EasyWarpAPI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class MyWarpPlugin extends JavaPlugin {
    
    private EasyWarpAPI warpAPI;
    
    @Override
    public void onEnable() {
        // Get API instance
        warpAPI = EasyWarpAPI.getInstance();
        
        if (warpAPI == null) {
            getLogger().severe("EasyWarp not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        getLogger().info("Successfully integrated with EasyWarp!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, 
                           String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();
        
        if (command.getName().equalsIgnoreCase("listhomes")) {
            // List all player's warps
            Set<String> warps = warpAPI.getWarpNames("players", uuid);
            
            if (warps.isEmpty()) {
                player.sendMessage("§cYou have no warps!");
                return true;
            }
            
            player.sendMessage("§aYour warps:");
            for (String warpName : warps) {
                Location loc = warpAPI.getWarpLocation("players", uuid, warpName);
                if (loc != null) {
                    player.sendMessage("§7- " + warpName + " §8(§e" + 
                        loc.getBlockX() + "§8, §e" + 
                        loc.getBlockY() + "§8, §e" + 
                        loc.getBlockZ() + "§8)");
                }
            }
            return true;
        }
        
        if (command.getName().equalsIgnoreCase("tpwarp")) {
            if (args.length == 0) {
                player.sendMessage("§cUsage: /tpwarp <name>");
                return false;
            }
            
            String warpName = args[0];
            Location warpLoc = warpAPI.getWarpLocation("players", uuid, warpName);
            
            if (warpLoc == null) {
                player.sendMessage("§cWarp '" + warpName + "' not found!");
                return true;
            }
            
            player.teleport(warpLoc);
            player.sendMessage("§aTeleported to " + warpName + "!");
            return true;
        }
        
        return false;
    }
}
```

## Working with Different Warp Systems

### Player Warps (Per-Player)

```java
// Key format: warpType + "." + playerUUID
String uuid = player.getUniqueId().toString();
String key = uuid; // For systems with usingPlayerUID: true

Location loc = warpAPI.getWarpLocation("players", key, "Home");
Set<String> warps = warpAPI.getWarpNames("players", key);
```

### Global Warps (Shared)

```java
// Key format: warpType (same as warp system name)
String warpType = "ww";
String key = "ww"; // For systems with usingPlayerUID: false

Location loc = warpAPI.getWarpLocation(warpType, key, "Spawn");
Set<String> warps = warpAPI.getWarpNames(warpType, key);
```

### Custom Warp Systems

If you have custom warp systems in config.yml:

```yaml
warps:
  vip:
    usingPlayerUID: false
    # ...
```

```java
// Access VIP warps
Location vipWarp = warpAPI.getWarpLocation("vip", "vip", "VIPLounge");
Set<String> vipWarps = warpAPI.getWarpNames("vip", "vip");
```

## Error Handling

```java
// Always check for null
Location loc = warpAPI.getWarpLocation("players", uuid, "Home");
if (loc == null) {
    // Warp doesn't exist or system not found
    player.sendMessage("Warp not found!");
    return;
}

// Check if world is loaded
if (loc.getWorld() == null) {
    player.sendMessage("Warp world is not loaded!");
    return;
}

// Safe teleport
if (player.teleport(loc)) {
    player.sendMessage("Teleported successfully!");
} else {
    player.sendMessage("Teleportation failed!");
}
```

## Best Practices

### 1. Cache the API Instance

```java
// Good - cache it
private EasyWarpAPI api;

@Override
public void onEnable() {
    api = EasyWarpAPI.getInstance();
}

// Use throughout plugin
Location loc = api.getWarpLocation(...);
```

```java
// Bad - repeated lookups
Location loc = EasyWarpAPI.getInstance().getWarpLocation(...);
```

### 2. Check for Null

```java
// Always validate
Location loc = api.getWarpLocation("players", uuid, "Home");
if (loc != null && loc.getWorld() != null) {
    player.teleport(loc);
}
```

### 3. Use Async for Batch Operations

```java
// When checking many warps
Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
    Set<String> warps = api.getWarpNames("players", uuid);
    
    // Process warps...
    
    // Return to main thread for player interaction
    Bukkit.getScheduler().runTask(plugin, () -> {
        player.sendMessage("Found " + warps.size() + " warps!");
    });
});
```

### 4. Handle Plugin Reload

```java
@Override
public void onEnable() {
    // Re-get API instance on reload
    warpAPI = EasyWarpAPI.getInstance();
    
    if (warpAPI == null) {
        // EasyWarp not loaded
        return;
    }
}
```

## Use Cases

### Custom Home Command

```java
@Override
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player p = (Player) sender;
    String uuid = p.getUniqueId().toString();
    
    Location home = warpAPI.getWarpLocation("players", uuid, "Home");
    if (home == null) {
        p.sendMessage("§cYou don't have a home set!");
        p.sendMessage("§7Use /pwarp set Home to create one");
        return true;
    }
    
    p.teleport(home);
    p.sendMessage("§aWelcome home!");
    return true;
}
```

### Warp Count

```java
int warpCount = warpAPI.getWarpNames("players", uuid).size();
p.sendMessage("§7You have §e" + warpCount + "§7 warps");
```

### Random Warp

```java
Set<String> warps = warpAPI.getWarpNames("ww", "ww");
if (!warps.isEmpty()) {
    String random = warps.stream()
        .skip(new Random().nextInt(warps.size()))
        .findFirst()
        .orElse(null);
    
    Location loc = warpAPI.getWarpLocation("ww", "ww", random);
    player.teleport(loc);
}
```

### Warp Distance Calculator

```java
Location playerLoc = player.getLocation();
Set<String> warps = warpAPI.getWarpNames("players", uuid);

for (String warpName : warps) {
    Location warpLoc = warpAPI.getWarpLocation("players", uuid, warpName);
    if (warpLoc != null && warpLoc.getWorld().equals(playerLoc.getWorld())) {
        double distance = playerLoc.distance(warpLoc);
        player.sendMessage(warpName + ": " + String.format("%.1f", distance) + " blocks");
    }
}
```

## API Limitations

- Cannot create warps (use commands or events)
- Cannot delete warps (use commands or events)
- Cannot modify warp data (use commands or events)
- Read-only access to warp locations and names

For write operations, use the command system or listen to events.

## Next Steps

- [Learn about Events](./events.md)
- [See Integration Examples](./integration.md)
- [View Source Code](https://github.com/Thumuss/EasyWarp)
