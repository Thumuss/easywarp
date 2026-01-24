# Warp Systems

EasyWarp's power comes from its flexible warp system architecture. This guide explains how to configure multiple warp systems.

## Understanding Warp Systems

A **warp system** is an independent set of warps with its own:
- Command and aliases
- Data file
- Permissions
- GUI inventory
- Configuration

## System Types

### Per-Player Systems

Each player has their own separate warps.

```yaml
warps:
  homes:
    usingPlayerUID: true  # Key setting
    filename: homes.yml
    display: "My Homes"
    command: home
```

**Use Cases**:
- Personal homes
- Private bases
- Player shops
- Individual plots

**Storage**: `homes.yml` contains sections per player UUID

### Shared Systems

All players access the same set of warps.

```yaml
warps:
  public:
    usingPlayerUID: false  # Key setting
    filename: public-warps.yml
    display: "Public Warps"
    command: publicwarp
```

**Use Cases**:
- Spawn points
- Server shops
- Public farms
- Community areas
- Event locations

**Storage**: `public-warps.yml` contains one shared section

## Creating Custom Systems

### Example 1: VIP Warp System

```yaml
warps:
  vip:
    filename: vip-warps.yml
    display: "§6VIP Warps"
    usingPlayerUID: false
    command: vipwarp
    aliases: [vw, vip]
    description: "Access exclusive VIP locations"
    usage: |
      /vipwarp          - Open VIP warp menu
      /vw set <name>    - Create VIP warp (admin only)
    permissions:
      base: easywarp.vip
      use: true
      set: true
      delete: true
      defaultFor:
        use: false   # Requires permission
        set: false   # Admins only
        delete: false
```

Grant VIP players: `easywarp.vip.use`

### Example 2: Staff Warp System

```yaml
warps:
  staff:
    filename: staff-warps.yml
    display: "§cStaff Warps"
    usingPlayerUID: false
    command: staffwarp
    aliases: [sw]
    permissions:
      base: easywarp.staff
      use: true
      set: true
      delete: true
      edit: true
      defaultFor:
        use: false    # Staff only
        set: false
        delete: false
        edit: false
```

### Example 3: Plot Warp System

```yaml
warps:
  plots:
    filename: plot-warps.yml
    display: "Plot Warps"
    usingPlayerUID: true  # Each player's plots
    command: plot
    aliases: [p]
    permissions:
      base: easywarp.plot
      use: true
      set: true
      delete: true
      defaultFor:
        use: true     # Everyone
        set: true     # Everyone can set
        delete: true  # Everyone can delete own
```

### Example 4: Arena Warp System

```yaml
warps:
  arenas:
    filename: arena-warps.yml
    display: "§4PvP Arenas"
    usingPlayerUID: false
    command: arena
    aliases: [pvp]
    permissions:
      base: easywarp.arena
      use: true
      set: true
      delete: false  # Players can't delete arenas
      defaultFor:
        use: true    # Everyone can access
        set: false   # Admins only
```

## Multi-System Setup

Complete example with multiple systems:

```yaml
# Main Configuration
prefix: "§6[Warps] §r"
defaultMaterialName: COMPASS
locale: en

cooldown:
  enabled: true
  default: 3
  bypass-permission: easywarp.cooldown.bypass

# Warp Systems
warps:
  # Personal homes
  homes:
    filename: homes.yml
    display: "My Homes"
    usingPlayerUID: true
    command: home
    aliases: [h]
    permissions:
      base: easywarp.home
      use: true
      set: true
      delete: true
      defaultFor: {use: true, set: true, delete: true}
  
  # Global warps
  global:
    filename: global-warps.yml
    display: "Server Warps"
    usingPlayerUID: false
    command: warp
    aliases: [w]
    permissions:
      base: easywarp.global
      use: true
      set: true
      delete: true
      defaultFor: {use: true, set: false, delete: false}
  
  # VIP warps
  vip:
    filename: vip-warps.yml
    display: "§6VIP Warps"
    usingPlayerUID: false
    command: vipwarp
    aliases: [vw]
    permissions:
      base: easywarp.vip
      use: true
      set: true
      delete: false
      defaultFor: {use: false, set: false, delete: false}
  
  # Staff warps
  staff:
    filename: staff-warps.yml
    display: "§cStaff Warps"
    usingPlayerUID: false
    command: staffwarp
    aliases: [sw]
    permissions:
      base: easywarp.staff
      use: true
      set: true
      delete: true
      defaultFor: {use: false, set: false, delete: false}
```

## System Permissions

Each system generates permissions automatically:

```
easywarp.<base>.*           # All permissions
easywarp.<base>.use         # Use/teleport
easywarp.<base>.set         # Create warps
easywarp.<base>.delete      # Delete warps
easywarp.<base>.edit        # Edit warps
easywarp.<base>.reload      # Reload system
```

Example permission setup:

```yaml
# In your permissions plugin
groups:
  default:
    permissions:
      - easywarp.home.*      # Full home access
      - easywarp.global.use  # Can use global warps
  
  vip:
    permissions:
      - easywarp.vip.*       # Full VIP warp access
  
  moderator:
    permissions:
      - easywarp.staff.use   # Can use staff warps
      - easywarp.global.*    # Can manage global warps
  
  admin:
    permissions:
      - easywarp.*           # All systems
```

## Best Practices

### Naming Conventions

Use clear, descriptive names:

```yaml
✓ Good:
  homes:       # Clear purpose
  vip:         # Easy to remember
  staff:       # Obvious
  
✗ Avoid:
  system1:     # Unclear
  warp2:       # Not descriptive
  test:        # Confusing
```

### Command Selection

Choose unique, memorable commands:

```yaml
✓ Good commands:
  home, h
  warp, w
  vipwarp, vw
  staffwarp, sw
  
✗ Avoid:
  w (conflicts)
  x (not memorable)
  asdf (random)
```

### File Organization

Keep related warps in appropriate systems:

- **homes.yml** - Player homes
- **global-warps.yml** - Server-wide locations
- **vip-warps.yml** - VIP exclusive
- **staff-warps.yml** - Staff only
- **event-warps.yml** - Temporary events

### Permission Strategy

Balance accessibility:

```yaml
# Public systems - easy access
defaultFor:
  use: true   # Everyone
  set: true   # Everyone

# Restricted systems - controlled access
defaultFor:
  use: false  # Permission required
  set: false  # Admins only

# Mixed approach
defaultFor:
  use: true   # Everyone can use
  set: false  # Only staff can create
```

## Common Scenarios

### Survival Server

```yaml
warps:
  homes:     # Player homes (/home)
    usingPlayerUID: true
    defaultFor: {use: true, set: true, delete: true}
  
  spawn:     # Server spawns (/spawn)
    usingPlayerUID: false
    defaultFor: {use: true, set: false, delete: false}
  
  shops:     # Player shops (/shop)
    usingPlayerUID: true
    defaultFor: {use: true, set: true, delete: true}
```

### Creative Server

```yaml
warps:
  plots:     # Build plots (/plot)
    usingPlayerUID: true
    defaultFor: {use: true, set: true, delete: true}
  
  showcase:  # Showcase builds (/showcase)
    usingPlayerUID: false
    defaultFor: {use: true, set: false, delete: false}
  
  resources: # Resource worlds (/resource)
    usingPlayerUID: false
    defaultFor: {use: true, set: false, delete: false}
```

### Minigame Server

```yaml
warps:
  games:     # Game arenas (/game)
    usingPlayerUID: false
    defaultFor: {use: true, set: false, delete: false}
  
  lobbies:   # Server lobbies (/lobby)
    usingPlayerUID: false
    defaultFor: {use: true, set: false, delete: false}
```

## Limitations

- Each command must be unique
- System names must be valid YAML keys
- Recommend max 10-15 systems for clarity
- Consider command name length

## Next Steps

- [Configure Permissions](./permissions.md)
- [Set Up Messages](./messages.md)
- [View Examples](../examples/multiple-systems.md)
