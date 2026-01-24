# Advanced Features
Learn about EasyWarp's advanced features and power-user capabilities.

## Tab Completion

EasyWarp provides intelligent tab completion for all commands.

### Command Actions

Press TAB after command:

```bash
/pwarp <TAB>
# Shows: set, delete, edit, list, info, search, reload
```

### Warp Names

Press TAB after action:

```bash
/pwarp <TAB>
# Shows: home, base, shop, farm (your warps)

/pwarp delete <TAB>
# Shows: home, base, shop (warps you can delete)
```

### Material Names

Press TAB when editing materials:

```bash
/pwarp edit home material <TAB>
# Shows: DIAMOND, EMERALD, BEACON, COMPASS, etc.
```

### Smart Filtering

Start typing to filter:

```bash
/pwarp edit home material DIA<TAB>
# Shows: DIAMOND, DIAMOND_BLOCK, DIAMOND_ORE, etc.
```

## Performance Optimization

### Caching System

EasyWarp caches frequently accessed data:

**Material Cache:**
- Caches Bukkit materials
- Reduces lookup time
- Thread-safe ConcurrentHashMap

**World Cache:**
- Caches world objects
- Faster teleportation
- Updates on world load/unload

**File Modification Tracking:**
- Checks file timestamp
- Only reloads when changed
- Reduces disk I/O

### Async Operations

All file operations are asynchronous:

```java
// Non-blocking file writes
warpSet()  // Async
warpDelete()  // Async
warpEdit()  // Async
reload()  // Async
```

Benefits:
- No server lag during file I/O
- Smooth player experience
- Handles large warp files

### Cooldown Management

Efficient cooldown tracking:

```java
CooldownManager:
- Per-player tracking
- Per-system tracking
- Automatic cleanup
- Memory-efficient
```

## Dynamic Command Registration

EasyWarp registers commands dynamically at runtime.

### How It Works

1. Reads `config.yml` warp systems
2. Registers each command name
3. Handles aliases automatically
4. Supports runtime changes

### Benefits

- No plugin.yml editing needed
- Easy to add new systems
- Flexible configuration
- Hot-reloadable

### Example

```yaml
warp-systems:
  custom:
    command: "mycmd"  # Creates /mycmd
    name: "Custom System"
```

Automatically creates:
- `/mycmd <warp>`
- `/mycmd set <name>`
- `/mycmd delete <name>`
- `/mycmd list`
- etc.

## Multi-System Architecture

Run multiple independent warp systems.

### Separate Storage

Each system has its own file:

```
plugins/EasyWarp/
├── player.yml      # Player warp system
├── global.yml      # Global warp system
├── admin.yml       # Admin warp system
└── custom.yml      # Custom system
```

### Independent Settings

Each system configured separately:

```yaml
warp-systems:
  player:
    cooldown:
      time: 60
    teleportation:
      delay: 5
      
  admin:
    cooldown:
      time: 0  # No cooldown
    teleportation:
      delay: 0  # Instant
```

### Separate Permissions

Fine-grained access control:

```yaml
# Player system
easywarp.player.use
easywarp.player.set

# Admin system
easywarp.admin.use
easywarp.admin.set
```

## Cross-World Teleportation

Warps work across dimensions seamlessly.

### Supported Worlds

- Overworld (`world`)
- Nether (`world_nether`)
- End (`world_the_end`)
- Custom worlds (plugins like Multiverse)

### How It Works

```yaml
# Warp in overworld
world: "world"
x: 100
y: 64
z: 100

# Works from anywhere
/pwarp home
# Teleports from nether → overworld
# Teleports from end → overworld
# Teleports from custom world → overworld
```

### World Validation

EasyWarp validates worlds before teleporting:

1. Checks world exists
2. Checks world is loaded
3. Shows error if unavailable

## Search & Filter

Powerful search capabilities.

### Fuzzy Search

Finds partial matches:

```bash
/pwarp search hom
# Finds: home, home2, my_home, old_home
```

### Case-Insensitive

```bash
/pwarp search HOME
/pwarp search home
/pwarp search HoMe
# All find same results
```

### Highlighting

Search results highlight matched text:

```
Results for 'shop':
- my_shop (highlighted)
- shop_main (highlighted)
- player_shops (highlighted)
```

## Pagination

Large lists are paginated automatically.

### Command Usage

```bash
/pwarp list      # Page 1
/pwarp list 2    # Page 2
/pwarp list 3    # Page 3
```

### Configuration

```yaml
items-per-page: 10  # Warps per page
```

### Navigation

```
Page 1/3:
- home
- base
- shop
...

[Previous] [Next]
```

## Event System

EasyWarp fires events for developers.

### Available Events

**WarpCreateEvent:**
- Fired when warp is created
- Cancellable
- Access to warp data

**WarpDeleteEvent:**
- Fired when warp is deleted
- Cancellable
- Access to warp name

**WarpTeleportEvent:**
- Fired before teleportation
- Cancellable
- Access to player and location

**WarpEditEvent:**
- Fired when warp is edited
- Cancellable
- Access to old and new data

### Example Usage

```java
@EventHandler
public void onWarpCreate(WarpCreateEvent event) {
    Player player = event.getPlayer();
    String warpName = event.getWarpName();
    
    // Custom logic
    if (warpName.contains("banned")) {
        event.setCancelled(true);
    }
}
```

## API Integration

Integrate EasyWarp with your plugins.

### Get Warp Data

```java
EasyWarpAPI api = EasyWarp.getAPI();

// Get warp
Warp warp = api.getWarp("player", player, "home");

// Get all warps
List<Warp> warps = api.getWarps("player", player);

// Check existence
boolean exists = api.hasWarp("player", player, "home");
```

### Create Warps Programmatically

```java
// Create warp
api.createWarp(
    "player",
    player,
    "home",
    player.getLocation()
);

// Delete warp
api.deleteWarp("player", player, "home");
```

### Teleport Players

```java
// Teleport to warp
api.teleport(player, warp);

// With delay
api.teleportWithDelay(player, warp, 5);

// With cooldown check
if (api.checkCooldown(player, "player")) {
    api.teleport(player, warp);
}
```

## Configuration Reloading

Hot-reload configuration without restart.

### Command

```bash
/pwarp reload
```

### What Reloads

- ✅ config.yml settings
- ✅ Message files
- ✅ Warp data files
- ✅ Permission settings
- ✅ Cooldown settings
- ❌ Command registration (requires restart)

### When to Reload

After changing:
- Messages
- Cooldowns
- Delays
- Permissions
- Warp data

### When to Restart

After changing:
- Command names
- Adding new systems
- Changing system IDs

## Permission Groups

Set up tiered access.

### Bronze Tier

```yaml
# 3 warps, 60s cooldown
permissions:
  - easywarp.player.use
  - easywarp.player.set
  - easywarp.player.limit.3
```

### Silver Tier

```yaml
# 10 warps, 30s cooldown
permissions:
  - easywarp.player.use
  - easywarp.player.set
  - easywarp.player.limit.10
```

### Gold Tier

```yaml
# Unlimited warps, no cooldown
permissions:
  - easywarp.player.use
  - easywarp.player.set
  - easywarp.player.limit.bypass
  - easywarp.player.cooldown.bypass
```

## Custom Material Icons

Full material customization.

### In GUI

Each warp can have unique icon:

```bash
/pwarp edit home material RED_BED
/pwarp edit shop material EMERALD
/pwarp edit farm material WHEAT
/pwarp edit mine material IRON_PICKAXE
```

### All Materials Supported

Any Minecraft 1.13+ material:

```bash
# Blocks
DIAMOND_BLOCK, EMERALD_BLOCK, GOLD_BLOCK

# Items
DIAMOND_SWORD, BOW, ARROW

# Special
PLAYER_HEAD, DRAGON_EGG, BEACON

# Decorative
PAINTING, ITEM_FRAME, ARMOR_STAND
```

### Default Material

Set per-system:

```yaml
warp-systems:
  player:
    material: "COMPASS"  # Default for all warps
```

## Location Precision

Exact location storage.

### Stored Data

```yaml
warps:
  home:
    x: 100.5           # Decimal precision
    y: 64.0
    z: -200.5
    yaw: 90.0          # Player rotation
    pitch: 0.0         # Look angle
    world: "world"     # Exact world name
```

### Teleport Accuracy

- Exact block position
- Exact player rotation
- Exact look direction
- No rounding errors

## Cooldown Bypass

Advanced cooldown management.

### Global Bypass

```yaml
easywarp.cooldown.bypass  # All systems
```

### Per-System Bypass

```yaml
easywarp.player.cooldown.bypass   # Player system only
easywarp.global.cooldown.bypass   # Global system only
```

### Temporary Bypass

Use permission plugins with timed permissions:

```bash
# LuckPerms - 1 hour bypass
/lp user Steve permission settemp easywarp.cooldown.bypass true 1h
```

## Silent Commands

Execute commands silently.

### Success Messages

Set to `null` to disable:

```yaml
success:
  use: null     # No message on teleport
  delete: null  # No message on delete
```

### Custom Silencing

Per-player mute:
- Use scoreboard tags
- Check in message send
- Implement in custom plugin

## Debugging

Enable debug mode for troubleshooting.

### Enable Debug

In `config.yml`:

```yaml
debug: true
```

### Debug Output

```
[EasyWarp] [DEBUG] Loading warp system: player
[EasyWarp] [DEBUG] Registering command: /pwarp
[EasyWarp] [DEBUG] Loading warps from: player.yml
[EasyWarp] [DEBUG] Loaded 15 warps for system: player
[EasyWarp] [DEBUG] Player Steve teleported to warp: home
[EasyWarp] [DEBUG] Cooldown set for Steve: 60 seconds
```

### Troubleshooting

Check logs for:
- File loading errors
- Permission issues
- World validation failures
- Material parse errors
- Command registration problems

## Next Steps

- [View API Documentation](../developers/api-overview.md)
- [See Integration Examples](../developers/integration.md)
- [Check FAQ](../support/faq.md)