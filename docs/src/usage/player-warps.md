# Player Warps
Player warps are personal waypoints that each player can create and manage for themselves. Perfect for homes, bases, shops, and favorite locations.

## Quick Start

```bash
# Create a warp at your location
/pwarp set home

# Teleport to your warp
/pwarp home

# List your warps
/pwarp list

# Delete a warp
/pwarp delete home
```

## Creating Warps

### Basic Creation

Stand where you want the warp, then:

```bash
/pwarp set <name>
```

Examples:
```bash
/pwarp set home
/pwarp set base
/pwarp set shop
/pwarp set farm
```

### Naming Rules

- Alphanumeric only (a-z, A-Z, 0-9, underscore, hyphen)
- No spaces
- Case-insensitive

```bash
# Good names
/pwarp set my_home
/pwarp set base-2
/pwarp set spawn1

# Bad names
/pwarp set my home      # No spaces
/pwarp set home!        # No special chars
/pwarp set "The Base"   # No quotes needed
```

### Warp Limits

Configure per-player limits:

```yaml
# In config.yml
warp-systems:
  player:
    max-warps-per-player: 10
```

## Using Warps

### Teleport to Warp

```bash
/pwarp <name>
```

Opens GUI showing warp location, then teleports.

### List Your Warps

```bash
/pwarp list
```

Shows paginated list of all your warps.

### Search Warps

```bash
/pwarp search <query>
```

Find warps by name:

```bash
/pwarp search home
/pwarp search base
/pwarp search shop
```

### View Warp Info

```bash
/pwarp info <name>
```

Shows:
- Warp name
- Location (world, x, y, z)
- Creation date
- Material/icon

## Managing Warps

### Edit Warp

Update location, material, or display name:

```bash
/pwarp edit <name> location
```

Updates warp to your current location.

```bash
/pwarp edit <name> material DIAMOND
```

Changes icon material.

```bash
/pwarp edit <name> name "My Home"
```

Changes display name in GUI.

### Delete Warp

```bash
/pwarp delete <name>
```

Permanently removes the warp.

### Reload Warps

Admin command to reload configuration:

```bash
/pwarp reload
```

## GUI Interface

When you open a warp, you see:

```
┌─────────────────────────────┐
│     [Icon] Warp Name        │
│                             │
│  Location: world, x, y, z   │
│  Click to teleport          │
│                             │
│  [Cancel]        [Teleport] │
└─────────────────────────────┘
```

### GUI Customization

In `warp.yml`, each warp stores:

```yaml
warps:
  home:
    name: "My Home"
    material: "RED_BED"
    world: "world"
    x: 100.5
    y: 64.0
    z: -200.5
    yaw: 90.0
    pitch: 0.0
```

## Common Use Cases

### Home System

```bash
# Main base
/pwarp set home

# Multiple homes
/pwarp set home1
/pwarp set home2
/pwarp set home3
```

### Resource Locations

```bash
/pwarp set iron_farm
/pwarp set gold_farm
/pwarp set mob_grinder
/pwarp set tree_farm
```

### Player Shops

```bash
/pwarp set my_shop
/pwarp set market
/pwarp set trade_center
```

### Building Projects

```bash
/pwarp set castle
/pwarp set house_plot
/pwarp set mega_build
```

### Exploration Points

```bash
/pwarp set village1
/pwarp set dungeon
/pwarp set nether_portal
/pwarp set end_portal
```

## Advanced Features

### Cross-World Warps

Warps work across dimensions:

```bash
# Create in overworld
/pwarp set base

# Works from nether
/pwarp base  # Teleports back to overworld
```

### Cooldown System

Configure delays between warps:

```yaml
warp-systems:
  player:
    cooldown:
      enabled: true
      time: 30  # 30 second cooldown
```

### Teleport Delay

Add casting time:

```yaml
warp-systems:
  player:
    teleportation:
      delay: 5  # 5 second delay
      cancel-on-move: true
```

### Material Icons

Customize warp icons:

```bash
/pwarp edit home material RED_BED
/pwarp edit shop material EMERALD
/pwarp edit farm material WHEAT
```

## Permissions

### Basic Access

```yaml
easywarp.player.use    # Use player warps
easywarp.player.set    # Create warps
easywarp.player.delete # Delete own warps
```

### Admin Permissions

```yaml
easywarp.player.edit   # Edit warps
easywarp.player.reload # Reload config
easywarp.player.admin  # Delete any warp
```

### Bypass Permissions

```yaml
easywarp.player.cooldown.bypass  # Skip cooldown
easywarp.player.delay.bypass     # Instant teleport
easywarp.player.limit.bypass     # No warp limit
```

## Tips & Tricks

### Organize by Prefix

```bash
/pwarp set home_main
/pwarp set home_nether
/pwarp set home_end

/pwarp set farm_iron
/pwarp set farm_gold
/pwarp set farm_mob
```

Then search by prefix:
```bash
/pwarp search home
/pwarp search farm
```

### Share Coordinates

While you can't share warps directly, share coordinates:

```bash
/pwarp info base
# Shows: Location: world, 100, 64, -200
# Tell friends: "Meet me at 100, 64, -200!"
```

### Backup Important Warps

Note down coordinates of important warps:

```bash
/pwarp info home
# Write down: world, 100, 64, -200
```

### Use Descriptive Names

```bash
# Vague (bad)
/pwarp set base1
/pwarp set place2

# Descriptive (good)
/pwarp set main_base
/pwarp set ocean_monument
```

### Delete Unused Warps

Keep your list clean:

```bash
/pwarp list
# See old/unused warps
/pwarp delete old_base
/pwarp delete temporary
```

## Examples

### New Player Journey

```bash
# Day 1: Create spawn warp
/pwarp set spawn

# Day 2: Found base location
/pwarp set home

# Week 1: Built farms
/pwarp set iron_farm
/pwarp set wheat_farm

# Week 2: Exploring
/pwarp set village
/pwarp set nether_hub
```

### Survival Server

```bash
# Essential warps
/pwarp set home         # Main base
/pwarp set backup       # Secondary base
/pwarp set nether       # Nether portal
/pwarp set end          # End portal

# Resource gathering
/pwarp set mines        # Mining area
/pwarp set forest       # Tree farm
/pwarp set ocean        # Fishing spot

# Special locations
/pwarp set shop         # Player shop
/pwarp set trading      # Trading hall
```

### Creative Build Server

```bash
# Current projects
/pwarp set castle       # Medieval build
/pwarp set city         # Modern city
/pwarp set spaceship    # Sci-fi build

# Reference locations
/pwarp set inspiration  # Example builds
/pwarp set supplies     # Material storage
/pwarp set testing      # Test area
```

## Troubleshooting

### "Warp doesn't exist"

- Check spelling: `/pwarp list`
- Warps are case-insensitive
- May have been deleted

### "You don't have permission"

Check permissions:
- `easywarp.player.use` - to teleport
- `easywarp.player.set` - to create

### "World doesn't exist"

- World may have been deleted
- Dimension may be disabled
- Contact admin to fix warp

### "Max warps reached"

- Delete unused warps
- Ask admin for limit increase
- Get bypass permission

### "Please wait X seconds"

- Cooldown is active
- Wait for timer
- Get bypass permission

## Best Practices

### Use Logical Names

```bash
# Category system
home_main, home_backup
farm_iron, farm_gold
shop_main, shop_storage
```

### Don't Hoard Warps

Delete what you don't use:
- Old bases
- Temporary locations
- Abandoned projects

### Update Locations

When you move, update warps:

```bash
/pwarp edit home location
```

### Check Before Deleting

```bash
# View info first
/pwarp info old_base

# Then delete
/pwarp delete old_base
```

## Next Steps

- [View All Commands](./commands.md)
- [Learn About Global Warps](./global-warps.md)
- [Configure Cooldowns](../configuration/cooldowns.md)