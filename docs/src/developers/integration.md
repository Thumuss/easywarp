# Integration Examples
Learn how to integrate EasyWarp into your own plugins and extend its functionality.

## Getting Started

### Add Dependency

#### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.YourUsername</groupId>
        <artifactId>EasyWarp</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

#### Gradle

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.YourUsername:EasyWarp:1.0.0'
}
```

#### Manual

Add EasyWarp.jar to your build path as a library.

### plugin.yml Dependency

```yaml
name: YourPlugin
version: 1.0.0
depend: [EasyWarp]  # Hard dependency
# or
softdepend: [EasyWarp]  # Optional dependency
```

## Basic Usage

### Get API Instance

```java
import eu.thumus.easywarp.EasyWarp;
import eu.thumus.easywarp.api.EasyWarpAPI;

public class YourPlugin extends JavaPlugin {
    
    private EasyWarpAPI warpAPI;
    
    @Override
    public void onEnable() {
        // Get API
        Plugin plugin = getServer().getPluginManager().getPlugin("EasyWarp");
        if (plugin instanceof EasyWarp) {
            warpAPI = ((EasyWarp) plugin).getAPI();
            getLogger().info("EasyWarp integration enabled!");
        }
    }
}
```

### Check if EasyWarp is Available

```java
public boolean hasEasyWarp() {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("EasyWarp");
    return plugin != null && plugin.isEnabled();
}
```

## Warp Management

### Check if Warp Exists

```java
public boolean hasWarp(Player player, String warpName) {
    return warpAPI.hasWarp("player", player, warpName);
}
```

### Get Warp Data

```java
import eu.thumus.easywarp.model.Warp;

public Location getWarpLocation(Player player, String warpName) {
    Warp warp = warpAPI.getWarp("player", player, warpName);
    if (warp == null) {
        return null;
    }
    
    return new Location(
        Bukkit.getWorld(warp.getWorld()),
        warp.getX(),
        warp.getY(),
        warp.getZ(),
        warp.getYaw(),
        warp.getPitch()
    );
}
```

### List Player Warps

```java
import java.util.List;

public List<String> getPlayerWarps(Player player) {
    List<Warp> warps = warpAPI.getWarps("player", player);
    return warps.stream()
        .map(Warp::getName)
        .collect(Collectors.toList());
}
```

### Create Warp

```java
public boolean createWarp(Player player, String name, Location location) {
    try {
        warpAPI.createWarp("player", player, name, location);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
```

### Delete Warp

```java
public boolean deleteWarp(Player player, String name) {
    try {
        warpAPI.deleteWarp("player", player, name);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
```

## Teleportation

### Teleport to Warp

```java
public void teleportToWarp(Player player, String warpName) {
    Warp warp = warpAPI.getWarp("player", player, warpName);
    if (warp == null) {
        player.sendMessage("§cWarp not found!");
        return;
    }
    
    warpAPI.teleport(player, warp);
}
```

### Teleport with Delay

```java
public void teleportWithDelay(Player player, String warpName, int delay) {
    Warp warp = warpAPI.getWarp("player", player, warpName);
    if (warp == null) {
        player.sendMessage("§cWarp not found!");
        return;
    }
    
    warpAPI.teleportWithDelay(player, warp, delay);
}
```

### Check Cooldown

```java
public boolean canWarp(Player player) {
    return !warpAPI.hasCooldown(player, "player");
}

public long getCooldownRemaining(Player player) {
    return warpAPI.getCooldownRemaining(player, "player");
}
```

## Event Handling

### Listen to Warp Creation

```java
import eu.thumus.easywarp.events.WarpCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WarpListener implements Listener {
    
    @EventHandler
    public void onWarpCreate(WarpCreateEvent event) {
        Player player = event.getPlayer();
        String warpName = event.getWarpName();
        String system = event.getSystem();
        
        // Log to console
        Bukkit.getLogger().info(player.getName() + " created warp: " + warpName);
        
        // Cancel if name contains bad words
        if (warpName.contains("bad_word")) {
            event.setCancelled(true);
            player.sendMessage("§cInvalid warp name!");
        }
    }
}
```

### Listen to Warp Deletion

```java
import eu.thumus.easywarp.events.WarpDeleteEvent;

@EventHandler
public void onWarpDelete(WarpDeleteEvent event) {
    Player player = event.getPlayer();
    String warpName = event.getWarpName();
    
    // Notify admins
    for (Player admin : Bukkit.getOnlinePlayers()) {
        if (admin.hasPermission("yourplugin.admin")) {
            admin.sendMessage("§e" + player.getName() + " deleted warp: " + warpName);
        }
    }
}
```

### Listen to Teleportation

```java
import eu.thumus.easywarp.events.WarpTeleportEvent;

@EventHandler
public void onWarpTeleport(WarpTeleportEvent event) {
    Player player = event.getPlayer();
    Location to = event.getTo();
    
    // Cancel if player in combat
    if (isInCombat(player)) {
        event.setCancelled(true);
        player.sendMessage("§cYou can't warp during combat!");
    }
    
    // Charge money
    if (!chargeMoney(player, 100)) {
        event.setCancelled(true);
        player.sendMessage("§cYou need $100 to warp!");
    }
}

private boolean isInCombat(Player player) {
    // Your combat check logic
    return false;
}

private boolean chargeMoney(Player player, double amount) {
    // Your economy integration
    return true;
}
```

### Listen to Warp Edits

```java
import eu.thumus.easywarp.events.WarpEditEvent;

@EventHandler
public void onWarpEdit(WarpEditEvent event) {
    Player player = event.getPlayer();
    String warpName = event.getWarpName();
    String editType = event.getEditType();  // "location", "material", "name"
    
    // Log changes
    Bukkit.getLogger().info(
        player.getName() + " edited " + warpName + " (" + editType + ")"
    );
}
```

## Integration Examples

### Economy Integration

Charge for warp creation:

```java
import net.milkbowl.vault.economy.Economy;

public class EconomyWarpPlugin extends JavaPlugin implements Listener {
    
    private Economy economy;
    private EasyWarpAPI warpAPI;
    
    @Override
    public void onEnable() {
        // Setup economy
        RegisteredServiceProvider<Economy> rsp = 
            getServer().getServicesManager().getRegistration(Economy.class);
        economy = rsp.getProvider();
        
        // Setup EasyWarp
        Plugin plugin = getServer().getPluginManager().getPlugin("EasyWarp");
        if (plugin instanceof EasyWarp) {
            warpAPI = ((EasyWarp) plugin).getAPI();
        }
        
        getServer().getPluginManager().registerEvents(this, this);
    }
    
    @EventHandler
    public void onWarpCreate(WarpCreateEvent event) {
        Player player = event.getPlayer();
        double cost = 1000.0;  // $1000 per warp
        
        if (!economy.has(player, cost)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need $" + cost + " to create a warp!");
            return;
        }
        
        economy.withdrawPlayer(player, cost);
        player.sendMessage("§aCharged $" + cost + " for warp creation.");
    }
    
    @EventHandler
    public void onWarpTeleport(WarpTeleportEvent event) {
        Player player = event.getPlayer();
        double cost = 50.0;  // $50 per teleport
        
        if (!economy.has(player, cost)) {
            event.setCancelled(true);
            player.sendMessage("§cYou need $" + cost + " to teleport!");
            return;
        }
        
        economy.withdrawPlayer(player, cost);
        player.sendMessage("§aCharged $" + cost + " for teleport.");
    }
}
```

### Combat Tag Integration

Prevent warping during combat:

```java
public class CombatWarpPlugin extends JavaPlugin implements Listener {
    
    private Map<UUID, Long> combatTag = new HashMap<>();
    private static final long COMBAT_TIME = 10000;  // 10 seconds
    
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player victim = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            
            // Tag both players
            combatTag.put(victim.getUniqueId(), System.currentTimeMillis());
            combatTag.put(attacker.getUniqueId(), System.currentTimeMillis());
            
            victim.sendMessage("§cYou are now in combat!");
            attacker.sendMessage("§cYou are now in combat!");
        }
    }
    
    @EventHandler
    public void onWarpTeleport(WarpTeleportEvent event) {
        Player player = event.getPlayer();
        
        if (isInCombat(player)) {
            event.setCancelled(true);
            long remaining = getRemainingCombatTime(player);
            player.sendMessage("§cYou can't warp for " + (remaining / 1000) + " more seconds!");
        }
    }
    
    private boolean isInCombat(Player player) {
        Long tagTime = combatTag.get(player.getUniqueId());
        if (tagTime == null) return false;
        return System.currentTimeMillis() - tagTime < COMBAT_TIME;
    }
    
    private long getRemainingCombatTime(Player player) {
        Long tagTime = combatTag.get(player.getUniqueId());
        if (tagTime == null) return 0;
        long elapsed = System.currentTimeMillis() - tagTime;
        return Math.max(0, COMBAT_TIME - elapsed);
    }
}
```

### Region Protection Integration

Integrate with WorldGuard:

```java
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public class RegionWarpPlugin extends JavaPlugin implements Listener {
    
    @EventHandler
    public void onWarpCreate(WarpCreateEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        
        // Check if player can build in region
        if (!canBuildAt(location, player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou can't create warps in this region!");
        }
    }
    
    @EventHandler
    public void onWarpTeleport(WarpTeleportEvent event) {
        Location to = event.getTo();
        Player player = event.getPlayer();
        
        // Check if player can enter region
        if (!canEnterRegion(to, player)) {
            event.setCancelled(true);
            player.sendMessage("§cYou don't have access to that region!");
        }
    }
    
    private boolean canBuildAt(Location location, Player player) {
        RegionContainer container = WorldGuard.getInstance()
            .getPlatform()
            .getRegionContainer();
        RegionQuery query = container.createQuery();
        
        // Check build flag
        return query.testBuild(
            BukkitAdapter.adapt(location),
            WorldGuardPlugin.inst().wrapPlayer(player)
        );
    }
    
    private boolean canEnterRegion(Location location, Player player) {
        // Similar check for entry flag
        return true;  // Implement your logic
    }
}
```

### Statistics Tracking

Track warp usage:

```java
public class WarpStatsPlugin extends JavaPlugin implements Listener {
    
    private Map<String, Integer> warpUsage = new HashMap<>();
    private Map<UUID, Integer> playerUsage = new HashMap<>();
    
    @EventHandler
    public void onWarpTeleport(WarpTeleportEvent event) {
        Player player = event.getPlayer();
        String warpName = event.getWarpName();
        
        // Track warp usage
        warpUsage.put(warpName, warpUsage.getOrDefault(warpName, 0) + 1);
        
        // Track player usage
        UUID uuid = player.getUniqueId();
        playerUsage.put(uuid, playerUsage.getOrDefault(uuid, 0) + 1);
    }
    
    public void showTopWarps(Player player) {
        player.sendMessage("§6§lTop Warps:");
        
        warpUsage.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> {
                player.sendMessage("§e" + entry.getKey() + ": §f" + entry.getValue() + " uses");
            });
    }
    
    public void showPlayerStats(Player player) {
        UUID uuid = player.getUniqueId();
        int usage = playerUsage.getOrDefault(uuid, 0);
        player.sendMessage("§aYou have used warps §f" + usage + " §atimes!");
    }
}
```

### Custom Command Integration

Add custom warp commands:

```java
public class CustomWarpCommands extends JavaPlugin {
    
    private EasyWarpAPI warpAPI;
    
    @Override
    public void onEnable() {
        Plugin plugin = getServer().getPluginManager().getPlugin("EasyWarp");
        if (plugin instanceof EasyWarp) {
            warpAPI = ((EasyWarp) plugin).getAPI();
        }
        
        getCommand("home").setExecutor(this::homeCommand);
        getCommand("sethome").setExecutor(this::setHomeCommand);
        getCommand("delhome").setExecutor(this::delHomeCommand);
    }
    
    private boolean homeCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cPlayers only!");
            return true;
        }
        
        Player player = (Player) sender;
        String homeName = args.length > 0 ? args[0] : "home";
        
        Warp warp = warpAPI.getWarp("player", player, homeName);
        if (warp == null) {
            player.sendMessage("§cHome not found!");
            return true;
        }
        
        warpAPI.teleport(player, warp);
        return true;
    }
    
    private boolean setHomeCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cPlayers only!");
            return true;
        }
        
        Player player = (Player) sender;
        String homeName = args.length > 0 ? args[0] : "home";
        
        warpAPI.createWarp("player", player, homeName, player.getLocation());
        player.sendMessage("§aHome set!");
        return true;
    }
    
    private boolean delHomeCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cPlayers only!");
            return true;
        }
        
        Player player = (Player) sender;
        String homeName = args.length > 0 ? args[0] : "home";
        
        warpAPI.deleteWarp("player", player, homeName);
        player.sendMessage("§aHome deleted!");
        return true;
    }
}
```

## Best Practices

### Check Plugin Availability

```java
if (!hasEasyWarp()) {
    getLogger().warning("EasyWarp not found! Disabling integration.");
    return;
}
```

### Handle Exceptions

```java
try {
    warpAPI.createWarp("player", player, name, location);
} catch (Exception e) {
    getLogger().severe("Failed to create warp: " + e.getMessage());
    player.sendMessage("§cAn error occurred!");
}
```

### Use Async Operations

```java
Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
    List<Warp> warps = warpAPI.getWarps("player", player);
    // Process warps
    
    Bukkit.getScheduler().runTask(this, () -> {
        // Update UI on main thread
    });
});
```

### Cache API Instance

```java
// Good - cache once
private EasyWarpAPI warpAPI;

public void onEnable() {
    warpAPI = getEasyWarpAPI();
}

// Bad - lookup every time
public void someMethod() {
    EasyWarpAPI api = getEasyWarpAPI();  // Don't do this
}
```

## Troubleshooting

### API Returns Null

```java
if (warpAPI == null) {
    getLogger().warning("EasyWarp API not available!");
    return;
}
```

### Events Not Firing

Check event priority:

```java
@EventHandler(priority = EventPriority.HIGHEST)
public void onWarpCreate(WarpCreateEvent event) {
    // Handle event
}
```

### ClassNotFoundException

Make sure EasyWarp is in dependencies:

```xml
<scope>provided</scope>  <!-- Not compile -->
```

## Next Steps

- [View Full API Documentation](./api-overview.md)
- [Check Event System](./events.md)
- [See Example Plugins](../examples/basic-setup.md)