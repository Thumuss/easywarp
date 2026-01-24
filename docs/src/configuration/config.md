# Main Configuration

The `config.yml` file is the heart of EasyWarp. This guide explains every configuration option.

## Configuration File Location

```
plugins/EasyWarp/config.yml
```

## Full Configuration Reference

```yaml
# Prefix for all plugin messages
prefix: "Server -> §4"

# Default material used for warp icons when none is specified
# Must be a valid Minecraft material name
defaultMaterialName: DARK_OAK_BUTTON

# Language for messages (en, fr)
# Add more languages by creating messages_<code>.yml files
locale: fr

# Cooldown settings
cooldown:
  enabled: true                              # Enable/disable cooldown system
  default: 3                                 # Default cooldown in seconds
  bypass-permission: easywarp.cooldown.bypass  # Permission to bypass cooldowns

# Teleportation settings
teleportation:
  delay: 0                    # Delay before teleporting (0 = instant)
  cancel-on-move: false       # Cancel if player moves during delay

# Warp systems configuration
warps:
  # System name (used internally)
  players:
    # File where warp data is stored
    filename: warp.yml
    
    # Display name shown in inventory title
    display: Warp User
    
    # Whether each player has their own warps (true) or shared (false)
    usingPlayerUID: true
    
    # Command name to register
    command: pwarp
    
    # Command aliases
    aliases: [p, pw, pwp]
    
    # Command description
    description: "Permet de se tp à un warp que tu as set."
    
    # Usage help text (supports multi-line)
    usage: |
      /pwarp
      /pwp set Warp_Name
      /pw set Warp_Name stone
      /p delete Warp_Name
      /p edit Warp_Name chest
      /p reload
    
    # Permissions configuration
    permissions:
      base: easywarp.players     # Base permission node
      use: true                  # Enable use permission
      set: true                  # Enable set permission
      delete: true               # Enable delete permission
      edit: true                 # Enable edit permission
      reload: true               # Enable reload permission
      
      # Which permissions are granted by default
      defaultFor:
        use: true      # Everyone can teleport by default
        set: false     # Need permission to create warps
        delete: false  # Need permission to delete warps
        edit: false    # Need permission to edit warps
        reload: false  # Need permission to reload
  
  # Another warp system (global warps)
  ww:
    filename: default.yml
    display: Warp
    usingPlayerUID: false
    command: warp
    aliases: [w, wp]
    description: "Permet de se tp à une warp set par un admin"
    usage: |
      /warp
      /wp set Warp_Name
      /w set Warp_Name stone
      /w delete Warp_Name
      /w edit Warp_Name chest
      /w reload
    permissions:
      base: easywarp.ww
      use: true
      set: true
      delete: true
      edit: true
      reload: true
      defaultFor:
        use: true      # Everyone can use warps
        set: false     # Only admins can create
        delete: false
        edit: false
        reload: false
```

## Configuration Options Explained

### Global Settings

#### `prefix`
Message prefix shown before all plugin messages.

**Type**: String  
**Default**: `"Server -> §4"`  
**Supports**: Color codes (`§` or `&`)

```yaml
prefix: "&6[EasyWarp] &r"
```

#### `defaultMaterialName`
Material used for warp icons when not specified.

**Type**: String (Material name)  
**Default**: `DARK_OAK_BUTTON`  
**Valid Values**: Any Bukkit Material name

```yaml
defaultMaterialName: COMPASS
```

#### `locale`
Language code for messages.

**Type**: String  
**Default**: `fr`  
**Valid Values**: Any language code with a corresponding `messages_<code>.yml` file

```yaml
locale: en  # Uses messages_en.yml
```

### Cooldown Settings

#### `cooldown.enabled`
Enable or disable the cooldown system globally.

**Type**: Boolean  
**Default**: `true`

```yaml
cooldown:
  enabled: false  # No cooldowns
```

#### `cooldown.default`
Default cooldown duration in seconds.

**Type**: Integer  
**Default**: `3`

```yaml
cooldown:
  default: 10  # 10 second cooldown
```

#### `cooldown.bypass-permission`
Permission node to bypass cooldowns.

**Type**: String  
**Default**: `easywarp.cooldown.bypass`

```yaml
cooldown:
  bypass-permission: vip.nocooldown
```

### Teleportation Settings

#### `teleportation.delay`
Delay in seconds before teleporting.

**Type**: Integer  
**Default**: `0` (instant)

```yaml
teleportation:
  delay: 3  # 3 second delay
```

#### `teleportation.cancel-on-move`
Cancel teleportation if player moves during delay.

**Type**: Boolean  
**Default**: `false`

```yaml
teleportation:
  cancel-on-move: true  # Cancel if player moves
```

### Warp Systems

Each entry under `warps:` creates a new warp system.

#### System Name
The key used internally. Must be unique.

```yaml
warps:
  my_system:  # Internal name
    # Configuration...
```

#### `filename`
File where warp data is stored (relative to plugin folder).

**Type**: String  
**Required**: Yes

```yaml
filename: my-warps.yml  # Creates plugins/EasyWarp/my-warps.yml
```

#### `display`
Name shown in the inventory GUI title.

**Type**: String  
**Required**: Yes  
**Supports**: Color codes

```yaml
display: "&6VIP Warps"
```

#### `usingPlayerUID`
Whether warps are per-player or shared.

**Type**: Boolean  
**Required**: Yes

```yaml
usingPlayerUID: true   # Each player has own warps
usingPlayerUID: false  # Warps shared by all players
```

#### `command`
Main command name to register.

**Type**: String  
**Required**: Yes  
**Note**: Must be unique across all plugins

```yaml
command: mywarp
```

#### `aliases`
Alternative names for the command.

**Type**: List of Strings  
**Required**: No (can be empty)

```yaml
aliases: [mw, warp2]
```

#### `description`
Command description shown in `/help`.

**Type**: String  
**Required**: No

```yaml
description: "My custom warp system"
```

#### `usage`
Help text for command usage.

**Type**: String (supports multi-line)  
**Required**: No

```yaml
usage: |
  /mywarp
  /mywarp set <name>
  /mywarp delete <name>
```

#### `permissions`
Permission configuration for this warp system.

##### `base`
Base permission node prefix.

**Type**: String  
**Required**: Yes

```yaml
permissions:
  base: easywarp.custom
```

This creates permissions like:
- `easywarp.custom.use`
- `easywarp.custom.set`
- etc.

##### Action Permissions

Enable/disable specific actions:

```yaml
permissions:
  use: true      # Enable /warp (open GUI/teleport)
  set: true      # Enable /warp set
  delete: true   # Enable /warp delete
  edit: true     # Enable /warp edit
  reload: true   # Enable /warp reload
```

Set to `false` to disable an action completely.

##### `defaultFor`

Configure which actions are available by default:

```yaml
permissions:
  defaultFor:
    use: true      # Everyone can teleport
    set: false     # Needs permission
    delete: false  # Needs permission
    edit: false    # Needs permission
    reload: false  # Needs permission
```

## Examples

### Personal Warps Only

```yaml
warps:
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
      defaultFor:
        use: true
        set: true    # Everyone can set homes
        delete: true # Everyone can delete homes
```

### Admin-Only Warps

```yaml
warps:
  admin:
    filename: admin-warps.yml
    display: "§cAdmin Warps"
    usingPlayerUID: false
    command: awarp
    permissions:
      base: easywarp.admin
      use: true
      set: true
      delete: true
      defaultFor:
        use: false    # Need permission to use
        set: false    # Need permission to set
        delete: false # Need permission to delete
```

### VIP Warps

```yaml
warps:
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
      delete: false  # VIPs can't delete
      defaultFor:
        use: false   # Need VIP permission
        set: false   # Need VIP permission
```

Then grant: `easywarp.vip.use` and `easywarp.vip.set` to VIP group.

## Reloading Configuration

After editing `config.yml`:

```
/pwarp reload
/warp reload
```

Or restart the server.

## Common Mistakes

### ❌ Don't Do This

```yaml
# Two systems with same command
warps:
  system1:
    command: warp
  system2:
    command: warp  # ERROR: Duplicate command!
```

```yaml
# Missing required field
warps:
  broken:
    display: "Test"
    # Missing: filename, usingPlayerUID, command
```

```yaml
# Invalid material
defaultMaterialName: NOT_A_REAL_MATERIAL
```

### ✅ Do This Instead

```yaml
# Unique commands
warps:
  system1:
    command: warp
  system2:
    command: warp2  # Different command
```

```yaml
# All required fields
warps:
  complete:
    filename: data.yml
    display: "Warps"
    usingPlayerUID: true
    command: mywarp
```

```yaml
# Valid material
defaultMaterialName: COMPASS
```

## Next Steps

- [Configure Permissions](./permissions.md)
- [Set up Messages](./messages.md)
- [Configure Cooldowns](./cooldowns.md)
