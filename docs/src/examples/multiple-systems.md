# Multiple Warp Systems
Learn how to set up multiple independent warp systems for different purposes.

## What Are Multiple Systems?

Each system has:
- Own command (`/pwarp`, `/warp`, `/admin`, etc.)
- Own storage file (`player.yml`, `global.yml`, etc.)
- Own permissions (`easywarp.player.*`, `easywarp.global.*`)
- Own cooldowns and delays
- Independent warp lists

## Example: 3-System Setup

### Configuration

```yaml
warp-systems:
  # System 1: Player personal warps
  player:
    name: "Player Warps"
    command: "pwarp"
    permission: "easywarp.player"
    material: "COMPASS"
    cooldown:
      enabled: true
      time: 60
    teleportation:
      delay: 5
      cancel-on-move: true
      
  # System 2: Server global warps  
  global:
    name: "Global Warps"
    command: "warp"
    permission: "easywarp.global"
    material: "ENDER_PEARL"
    cooldown:
      enabled: true
      time: 30
    teleportation:
      delay: 3
      cancel-on-move: true
      
  # System 3: Admin instant warps
  admin:
    name: "Admin Warps"
    command: "awarp"
    permission: "easywarp.admin"
    material: "NETHER_STAR"
    cooldown:
      enabled: false
    teleportation:
      delay: 0
      cancel-on-move: false
```

### Usage

```bash
# Player warps (anyone)
/pwarp set home
/pwarp home

# Global warps (anyone uses, admins create)
/warp spawn
/warp shop

# Admin warps (admins only)
/awarp staff
/awarp maintenance
```

## Use Case Examples

### Survival Server

```yaml
warp-systems:
  home:
    name: "Homes"
    command: "home"
    permission: "easywarp.home"
    material: "RED_BED"
    cooldown:
      time: 120  # 2 min cooldown
    teleportation:
      delay: 10  # 10 sec delay
      
  spawn:
    name: "Spawn Points"
    command: "spawn"
    permission: "easywarp.spawn"
    material: "BEACON"
    cooldown:
      enabled: false  # No cooldown
    teleportation:
      delay: 0  # Instant
      
  shop:
    name: "Player Shops"
    command: "shop"
    permission: "easywarp.shop"
    material: "EMERALD"
    cooldown:
      time: 60
    teleportation:
      delay: 5
```

Commands:
```bash
/home set base
/home base
/spawn main
/shop set myshop
/shop myshop
```

### Faction Server

```yaml
warp-systems:
  faction:
    name: "Faction Warps"
    command: "f warp"
    permission: "easywarp.faction"
    material: "BANNER"
    cooldown:
      time: 300  # 5 min
    teleportation:
      delay: 10
      cancel-on-move: true
      
  public:
    name: "Public Warps"
    command: "pwarp"
    permission: "easywarp.public"
    material: "COMPASS"
    cooldown:
      time: 60
    teleportation:
      delay: 5
      
  warzone:
    name: "Warzone Warps"
    command: "warzone"
    permission: "easywarp.warzone"
    material: "DIAMOND_SWORD"
    cooldown:
      time: 180  # 3 min
    teleportation:
      delay: 15  # Long delay
      cancel-on-move: true
```

### Creative Server

```yaml
warp-systems:
  plot:
    name: "Plots"
    command: "plot"
    permission: "easywarp.plot"
    material: "GRASS_BLOCK"
    cooldown:
      enabled: false
    teleportation:
      delay: 0
      
  build:
    name: "Build Warps"
    command: "build"
    permission: "easywarp.build"
    material: "BRICKS"
    cooldown:
      enabled: false
    teleportation:
      delay: 0
      
  showcase:
    name: "Showcases"
    command: "showcase"
    permission: "easywarp.showcase"
    material: "PAINTING"
    cooldown:
      enabled: false
    teleportation:
      delay: 2  # Slight delay for immersion
```

### Minigame Hub

```yaml
warp-systems:
  lobby:
    name: "Lobbies"
    command: "lobby"
    permission: "easywarp.lobby"
    material: "NETHER_STAR"
    cooldown:
      enabled: false
    teleportation:
      delay: 0
      
  game:
    name: "Games"
    command: "game"
    permission: "easywarp.game"
    material: "DIAMOND_SWORD"
    cooldown:
      time: 30
    teleportation:
      delay: 3
      
  event:
    name: "Events"
    command: "event"
    permission: "easywarp.event"
    material: "FIREWORK_ROCKET"
    cooldown:
      enabled: false
    teleportation:
      delay: 0
```

## Tiered System (VIP Ranks)

### Configuration

```yaml
warp-systems:
  basic:
    name: "Basic Warps"
    command: "bwarp"
    permission: "easywarp.basic"
    material: "WOODEN_PICKAXE"
    max-warps: 3
    cooldown:
      time: 180  # 3 min
    teleportation:
      delay: 10
      
  premium:
    name: "Premium Warps"
    command: "pwarp"
    permission: "easywarp.premium"
    material: "IRON_PICKAXE"
    max-warps: 10
    cooldown:
      time: 90  # 1.5 min
    teleportation:
      delay: 5
      
  elite:
    name: "Elite Warps"
    command: "ewarp"
    permission: "easywarp.elite"
    material: "DIAMOND_PICKAXE"
    max-warps: 25
    cooldown:
      time: 30
    teleportation:
      delay: 2
      
  ultimate:
    name: "Ultimate Warps"
    command: "uwarp"
    permission: "easywarp.ultimate"
    material: "NETHERITE_PICKAXE"
    max-warps: -1  # Unlimited
    cooldown:
      enabled: false
    teleportation:
      delay: 0
```

### Permissions

```bash
# Free players
/lp group default permission set easywarp.basic.* true

# VIP rank ($5)
/lp group vip permission set easywarp.premium.* true

# Elite rank ($15)
/lp group elite permission set easywarp.elite.* true

# Ultimate rank ($30)
/lp group ultimate permission set easywarp.ultimate.* true
```

## Category System

Organize by purpose:

```yaml
warp-systems:
  home:
    name: "Homes"
    command: "home"
    permission: "easywarp.home"
    
  farm:
    name: "Farms"
    command: "farm"
    permission: "easywarp.farm"
    
  mine:
    name: "Mines"
    command: "mine"
    permission: "easywarp.mine"
    
  shop:
    name: "Shops"
    command: "shop"
    permission: "easywarp.shop"
    
  friend:
    name: "Friends"
    command: "friend"
    permission: "easywarp.friend"
```

Usage:
```bash
/home set main
/farm set wheat_farm
/mine set diamond_mine
/shop set myshop
/friend set alex_base
```

## Advanced: Dynamic Systems

### World-Specific Systems

```yaml
warp-systems:
  overworld:
    name: "Overworld Warps"
    command: "owarp"
    permission: "easywarp.overworld"
    material: "GRASS_BLOCK"
    allowed-worlds:
      - world
      
  nether:
    name: "Nether Warps"
    command: "nwarp"
    permission: "easywarp.nether"
    material: "NETHERRACK"
    allowed-worlds:
      - world_nether
      
  end:
    name: "End Warps"
    command: "ewarp"
    permission: "easywarp.end"
    material: "END_STONE"
    allowed-worlds:
      - world_the_end
```

### Permission-Based Access

```yaml
warp-systems:
  member:
    name: "Member Warps"
    command: "mwarp"
    permission: "easywarp.member"
    required-permission: "server.member"
    
  trusted:
    name: "Trusted Warps"
    command: "twarp"
    permission: "easywarp.trusted"
    required-permission: "server.trusted"
    
  staff:
    name: "Staff Warps"
    command: "swarp"
    permission: "easywarp.staff"
    required-permission: "server.staff"
```

## Best Practices

### Clear Naming

```bash
# Good - descriptive
/home, /shop, /farm, /mine

# Bad - confusing
/warp1, /w2, /system3
```

### Logical Organization

Group related systems:
```yaml
# Player systems
- home
- shop
- farm

# Server systems
- spawn
- warp
- event

# Staff systems
- admin
- staff
- mod
```

### Consistent Settings

Similar systems should have similar settings:

```yaml
# All player systems
cooldown:
  time: 60
teleportation:
  delay: 5

# All admin systems  
cooldown:
  enabled: false
teleportation:
  delay: 0
```

### Document Your Setup

Keep a reference:

```
System Overview:
- /pwarp: Player warps (60s cooldown, 5s delay)
- /warp: Global warps (30s cooldown, 3s delay)
- /awarp: Admin warps (no cooldown, instant)

Permissions:
- default: easywarp.player.*
- admin: easywarp.global.*, easywarp.admin.*
```

## Migrating to Multiple Systems

### From Single System

Before:
```yaml
warp-systems:
  player:
    command: "warp"
```

After:
```yaml
warp-systems:
  player:
    command: "pwarp"  # Change command
    
  global:
    command: "warp"   # Add new system
```

### Data Migration

1. Stop server
2. Backup `warp.yml`
3. Copy to new system:
   ```bash
   cp player.yml global.yml
   ```
4. Edit `global.yml` (remove player warps)
5. Edit `player.yml` (remove global warps)
6. Start server

## Troubleshooting

### Commands Conflict

```
Error: Command /warp already registered
```

Solution: Use unique command names:
```yaml
command: "gwarp"  # Not "warp"
```

### Permissions Not Working

Check hierarchy:
```bash
# Give specific systems
/lp group default permission set easywarp.player.* true
/lp group default permission set easywarp.global.use true

# Don't give wildcard
# easywarp.* gives access to ALL systems
```

### Wrong System Used

Players using wrong command:

Solution: Clear documentation
```
Use /pwarp for personal warps
Use /warp for global warps  
Use /awarp for admin warps
```

## Examples in Action

### Player Experience

```bash
# Create personal warps
/pwarp set home
/pwarp set farm
/pwarp set shop

# Use global warps
/warp spawn
/warp pvp

# Staff can use admin warps
/awarp staff_room
```

### Admin Management

```bash
# Manage player system
/pwarp reload

# Manage global system
/warp set spawn
/warp delete old_warp

# Manage admin system
/awarp set maintenance
```

## Next Steps

- [Set Up Custom Permissions](./custom-permissions.md)
- [Read Advanced Features](../usage/advanced-features.md)
- [View Full Configuration](../configuration/config.md)