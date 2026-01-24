# Global Warps
Global warps are server-wide waypoints accessible to all players. Perfect for spawn points, shops, arenas, minigames, and public facilities.

## Overview

**Global Warps** vs **Player Warps**:

| Feature | Global Warps | Player Warps |
|---------|--------------|--------------|
| Visibility | All players | Only owner |
| Who creates | Admins only | Any player |
| Use case | Public locations | Personal waypoints |
| Permission | Elevated | Basic |

## Quick Start (Admins)

```bash
# Create global warp
/warp set spawn

# Players use it
/warp spawn

# List all global warps
/warp list

# Delete global warp
/warp delete spawn
```

## Creating Global Warps

### Requirements

Requires permission:
```yaml
easywarp.global.set
```

### Basic Creation

Stand at the location, then:

```bash
/warp set <name>
```

Examples:
```bash
/warp set spawn
/warp set shop
/warp set pvp
/warp set events
```

### With Custom Material

```bash
/warp set <name> <material>
```

Examples:
```bash
/warp set spawn BEACON
/warp set shop EMERALD
/warp set pvp DIAMOND_SWORD
/warp set events FIREWORK_ROCKET
```

## Using Global Warps

### Teleport (All Players)

```bash
/warp <name>
```

Any player can use global warps:

```bash
/warp spawn
/warp shop
/warp pvp
```

### List Global Warps

```bash
/warp list
```

Shows all available global warps with pagination.

### Search Global Warps

```bash
/warp search <query>
```

Find warps:
```bash
/warp search shop
/warp search pvp
/warp search mini
```

### View Warp Info

```bash
/warp info <name>
```

Shows details:
- Name
- Location
- Material
- Created by (if tracked)

## Managing Global Warps

### Edit Warp Location

Update warp to your current position:

```bash
/warp edit <name> location
```

### Edit Warp Material

Change the icon:

```bash
/warp edit <name> material <material>
```

Examples:
```bash
/warp edit spawn material BEACON
/warp edit shop material DIAMOND
/warp edit pvp material NETHERITE_SWORD
```

### Edit Display Name

Change GUI name:

```bash
/warp edit <name> name "Display Name"
```

Examples:
```bash
/warp edit spawn name "Server Spawn"
/warp edit shop name "Shopping District"
/warp edit pvp name "PvP Arena ⚔"
```

### Delete Warp

Remove global warp:

```bash
/warp delete <name>
```

## Common Use Cases

### Server Essentials

```bash
/warp set spawn          # Main spawn
/warp set tutorial       # New player guide
/warp set rules          # Rule board
/warp set help           # Help center
```

### Economy & Trading

```bash
/warp set spawn EMERALD
/warp set admin_shop DIAMOND
/warp set player_shops CHEST
/warp set auction GOLD_INGOT
/warp set bank GOLD_BLOCK
```

### PvP & Combat

```bash
/warp set pvp DIAMOND_SWORD
/warp set arena SHIELD
/warp set duel CROSSBOW
/warp set koth BEACON
```

### Minigames

```bash
/warp set spleef DIAMOND_SHOVEL
/warp set parkour LEATHER_BOOTS
/warp set dropper FEATHER
/warp set skywars ENDER_PEARL
/warp set bedwars RED_BED
```

### Resource Areas

```bash
/warp set mines IRON_PICKAXE
/warp set nether NETHERRACK
/warp set end END_STONE
/warp set resource_world GRASS_BLOCK
```

### Social & Events

```bash
/warp set events FIREWORK_ROCKET
/warp set party CAKE
/warp set cinema PAINTING
/warp set concert NOTE_BLOCK
```

## Server Setup Examples

### Survival Server

```bash
# Essential warps
/warp set spawn BEACON
/warp set shop EMERALD
/warp set wild COMPASS
/warp set resource_world GRASS_BLOCK

# Facilities
/warp set nether_hub NETHERRACK
/warp set end_portal END_PORTAL_FRAME
/warp set trading_hall VILLAGER_SPAWN_EGG
/warp set farm_world WHEAT
```

### Faction/PvP Server

```bash
# Combat warps
/warp set warzone DIAMOND_SWORD
/warp set koth BEACON
/warp set outpost BANNER
/warp set arena SHIELD

# Safe zones
/warp set spawn GRASS_BLOCK
/warp set shop EMERALD
/warp set safezone OAK_FENCE
```

### Creative Server

```bash
# Plot worlds
/warp set plots GRASS_BLOCK
/warp set creative COMMAND_BLOCK
/warp set building BRICKS

# Showcases
/warp set showcase PAINTING
/warp set gallery ITEM_FRAME
/warp set competition FIREWORK_ROCKET
```

### Minigame Hub

```bash
# Hub
/warp set lobby NETHER_STAR

# Games
/warp set skywars ENDER_PEARL
/warp set bedwars RED_BED
/warp set spleef DIAMOND_SHOVEL
/warp set parkour LEATHER_BOOTS
/warp set dropper FEATHER
/warp set pvp DIAMOND_SWORD
/warp set tnt_run TNT
/warp set hide_seek LEATHER_CHESTPLATE
```

## Permissions

### Player Permissions

```yaml
easywarp.global.use    # Use global warps
```

Give to all players:
```bash
/lp group default permission set easywarp.global.use true
```

### Admin Permissions

```yaml
easywarp.global.set     # Create warps
easywarp.global.edit    # Edit warps
easywarp.global.delete  # Delete warps
easywarp.global.reload  # Reload config
```

Give to admin team:
```bash
/lp group admin permission set easywarp.global.* true
```

### Bypass Permissions

```yaml
easywarp.global.cooldown.bypass  # No cooldown
easywarp.global.delay.bypass     # Instant teleport
```

## Configuration

### System Settings

In `config.yml`:

```yaml
warp-systems:
  global:
    name: "Global Warps"
    command: "warp"
    permission: "easywarp.global"
    material: "ENDER_PEARL"
    
    # Cooldowns
    cooldown:
      enabled: true
      time: 30  # seconds
      
    # Teleport delay
    teleportation:
      delay: 3
      cancel-on-move: true
```

### File Location

Global warps stored in:
```
plugins/EasyWarp/warp.yml
```

### Example warp.yml

```yaml
warps:
  spawn:
    name: "Server Spawn"
    material: "BEACON"
    world: "world"
    x: 0.5
    y: 64.0
    z: 0.5
    yaw: 0.0
    pitch: 0.0
    
  shop:
    name: "Shopping District"
    material: "EMERALD"
    world: "world"
    x: 100.5
    y: 64.0
    z: 100.5
    yaw: 90.0
    pitch: 0.0
```

## Advanced Setup

### Multiple Global Systems

Create different categories:

```yaml
warp-systems:
  spawn:
    name: "Spawn Points"
    command: "spawn"
    permission: "easywarp.spawn"
    cooldown:
      enabled: false  # No cooldown for spawn
      
  minigame:
    name: "Minigames"
    command: "game"
    permission: "easywarp.game"
    cooldown:
      enabled: true
      time: 60
      
  event:
    name: "Events"
    command: "event"
    permission: "easywarp.event"
    cooldown:
      enabled: false
```

Usage:
```bash
/spawn main
/game skywars
/event drop_party
```

### Per-Warp Permissions

Create specific permissions for each warp:

In plugin code or using conditional permissions:

```yaml
# Only VIPs can access
/warp vip_lounge
# Requires: easywarp.global.warp.vip_lounge

# Only staff
/warp staff_room
# Requires: easywarp.global.warp.staff_room
```

### Command Aliases

In plugin.yml:

```yaml
commands:
  warp:
    aliases: [globalwarp, gwarp, w]
```

Then players can use:
```bash
/warp spawn
/gwarp spawn
/w spawn
```

## Best Practices

### Clear Naming

```bash
# Good - descriptive
/warp set spawn
/warp set main_shop
/warp set pvp_arena

# Bad - vague
/warp set warp1
/warp set place
/warp set thing
```

### Appropriate Materials

```bash
# Match purpose
/warp set spawn GRASS_BLOCK      # Natural spawn
/warp set shop EMERALD           # Trading
/warp set pvp DIAMOND_SWORD      # Combat
/warp set parkour LEATHER_BOOTS  # Movement
```

### Organize by Category

```bash
# Prefix system
/warp set shop_main
/warp set shop_admin
/warp set shop_player

/warp set game_skywars
/warp set game_bedwars
/warp set game_spleef
```

### Document Locations

Keep a list:
```
spawn: Main spawn (0, 64, 0)
shop: Shopping district (100, 64, 100)
pvp: PvP arena (-50, 70, -50)
```

### Regular Maintenance

- Remove unused warps
- Update outdated locations
- Check for broken worlds
- Test all warps work

## Tips & Tricks

### Quick Setup Script

```bash
# Setup new server
/warp set spawn BEACON
/warp set shop EMERALD
/warp set wild COMPASS
/warp set nether NETHERRACK
/warp set end END_STONE
```

### Thematic Icons

```bash
# Medieval theme
/warp set castle STONE_BRICKS
/warp set tavern OAK_DOOR
/warp set market HAY_BLOCK

# Sci-fi theme
/warp set starship IRON_BLOCK
/warp set lab BEACON
/warp set reactor REDSTONE_BLOCK
```

### Seasonal Updates

```bash
# Winter event
/warp edit spawn material SNOW_BLOCK
/warp edit spawn name "Winter Spawn ❄"

# Halloween
/warp edit spawn material JACK_O_LANTERN
/warp edit spawn name "Spooky Spawn 🎃"
```

## Troubleshooting

### Players Can't Use Warps

Check permission:
```bash
/lp group default permission set easywarp.global.use true
```

### Can't Create Warps

Need admin permission:
```bash
/lp user <admin> permission set easywarp.global.set true
```

### Warp Goes to Wrong Location

Update location:
```bash
/warp edit <name> location
```

### Material Not Showing

Check material name:
```bash
# Must be valid 1.13+ material
/warp edit spawn material BEACON

# Not valid
/warp edit spawn material beacon  # lowercase
/warp edit spawn material 138     # numeric ID
```

## Next Steps

- [View All Commands](./commands.md)
- [Learn About Player Warps](./player-warps.md)
- [Configure Multiple Systems](../configuration/warp-systems.md)
- [Set Up Permissions](../configuration/permissions.md)