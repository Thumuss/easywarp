# New Features v1.0.0

This page documents all new features added in EasyWarp 1.0.0.

## 🎭 Public/Private/Shared Warps

Control your warp visibility with 3 access levels.

### Make a warp public

```bash
/pwarp public <name>
```

Makes your warp accessible to all players on the server.

**Permission**: `easywarp.warp.public`

**Example**:
```bash
/pwarp public Shop
# Now everyone can visit your Shop
```

### Make a warp private

```bash
/pwarp private <name>
```

Makes your warp private (only you can access it).

**Permission**: `easywarp.warp.use`

### Share with specific players

```bash
/pwarp share <name> <player>
```

Shares your warp with a specific player.

**Permission**: `easywarp.warp.share`

**Configuration**:
```yaml
warps:
  players:
    sharing:
      enabled: true
      allow-public: true
      max-shared-with: 10  # Maximum players per warp
```

**Example**:
```bash
/pwarp share Home Steve
/pwarp share Farm Alex
```

### Stop sharing

```bash
/pwarp unshare <name> <player>
```

### Visit a public warp

```bash
/pwarp visit <player> <warp>
```

Teleports to another player's public warp.

**Permission**: `easywarp.warp.visit`

**Examples**:
```bash
/pwarp visit Notch spawn
/pwarp visit Steve shop
```

---

## 💰 Economy Integration (Vault)

EasyWarp integrates with Vault to add costs to warps.

### Installation

1. Install [Vault](https://www.spigotmc.org/resources/vault.34315/)
2. Install an economy plugin (e.g., EssentialsX)
3. Configure EasyWarp

### Configuration

```yaml
economy:
  enabled: true
  cost-per-warp-creation: 100    # Cost to create a warp
  cost-per-teleport: 10          # Cost per teleportation
  refund-on-delete: 50           # Refund on deletion
```

### Cost bypass

Grant the `easywarp.bypass.cost` permission to ignore economic costs.

**Example**:
```yaml
groups:
  vip:
    permissions:
      - easywarp.bypass.cost
```

### Economy messages

Players see costs before taking action:
- "Creation cost: $100"
- "Teleportation cost: $10"
- "Refund: $50"

---

## 📊 Configurable Warp Limits

Define how many warps each group can create.

### Configuration

```yaml
warp-limits:
  default: 5        # Default limit
  vip: 15          # VIP limit
  premium: 50      # Premium limit
  unlimited: -1    # Unlimited (-1)
```

### Permissions

Assign permissions by group:

```yaml
groups:
  default:
    permissions:
      - easywarp.limit.default    # 5 warps
  
  vip:
    permissions:
      - easywarp.limit.vip        # 15 warps
  
  premium:
    permissions:
      - easywarp.limit.premium    # 50 warps
  
  admin:
    permissions:
      - easywarp.limit.unlimited  # Unlimited
```

### Messages

Players see their limit:
- "You have 3/5 warps"
- "You have 12/15 warps"
- "You've reached your warp limit (5)"

---

## ⏰ Temporary Warps

Create warps that automatically expire after a defined duration.

### Configuration

```yaml
temporary-warps:
  enabled: true
  max-duration: 86400      # Maximum duration in seconds (24h)
  cleanup-interval: 300    # Cleanup interval (5min)
```

### Usage

Temporary warps are configured via warp metadata (API or YAML config).

**Manual configuration example**:
```yaml
players:
  player-uuid:
    event_warp:
      expires-at: 1674567890000  # Unix timestamp in ms
```

### Notifications

Players receive a notification 5 minutes before expiration:

> "Your warp Event will expire in 5 minutes!"

### Automatic cleanup

An automatic cleanup system removes expired warps every 5 minutes.

---

## 🌍 Per-World Configuration

Define different delays and cooldowns per world.

### Configuration

```yaml
world-settings:
  world_nether:
    teleportation:
      delay: 10                # 10 second delay in Nether
      cancel-on-move: true
    cooldown:
      default: 120            # 2 minute cooldown
  
  world_the_end:
    teleportation:
      delay: 15                # 15 seconds in End
    cooldown:
      default: 180            # 3 minute cooldown
```

### Setting inheritance

If a world is not configured, it uses default settings:

```yaml
teleportation:
  delay: 5
  cancel-on-move: false

cooldown:
  default: 3
```

---

## 🔐 Protection and Confirmations

Add confirmations for dangerous actions.

### Configuration

```yaml
protection:
  prevent-warp-overwrite: true      # Prevents accidental overwrite
  require-confirmation-delete: true  # Confirmation for deletion
  require-confirmation-public: true  # Confirmation to make public
  confirmation-timeout: 10          # Timeout in seconds
```

### Usage

When performing a protected action, the player must confirm:

```
> /pwarp delete Home
Are you sure you want to delete warp 'Home'?
Type /warp confirm to proceed or /warp cancel to abort.
This will expire in 10 seconds.
```

**Commands**:
- `/warp confirm` - Confirms the action
- `/warp cancel` - Cancels the action

---

## 🎨 Custom Icons and Lore

Customize the appearance of your warps in the GUI.

### Custom lore

Warps can have a custom description (list of lines).

**Manual configuration** (in warp.yml):
```yaml
players:
  uuid:
    home:
      lore:
        - "§aMy main house"
        - "§7Coordinates: X:100 Y:64 Z:200"
        - "§eBuilt on 01/15/2025"
```

### Custom Model Data

Support for custom textures via resource pack.

```yaml
home:
  custom-model-data: 1234
```

---

## 🎯 Conditional Warps

Restrict warp access based on various criteria.

### Condition types

#### Permission
```yaml
conditions:
  permission: "my.plugin.vip"
```

#### Specific world
```yaml
conditions:
  world: "world_nether"
```

#### World type
```yaml
conditions:
  worldtype: "NETHER"  # NORMAL, NETHER, THE_END
```

#### Minimum/maximum level
```yaml
conditions:
  minlevel: 10
  maxlevel: 50
```

#### Game mode
```yaml
conditions:
  gamemode: "SURVIVAL"
```

#### Minimum health
```yaml
conditions:
  health: 10.0
```

#### Player count
```yaml
conditions:
  minplayers: 5
  maxplayers: 20
```

#### Time of day
```yaml
conditions:
  time: "day"  # or "night"
```

#### Weather
```yaml
conditions:
  weather: "clear"  # or "storm"
```

### Error messages

When conditions are not met:
- "You don't meet the requirements: Missing permission: vip"
- "You don't meet the requirements: Minimum level required: 10"
- "You don't meet the requirements: Can only be used during: day"

### Bypass

Permission: `easywarp.bypass.conditions`

---

## 🔔 Notification System

Receive notifications for various events.

### Configuration

```yaml
notifications:
  cooldown-ready: true        # Cooldown finished
  warp-expiring-soon: true    # Warp about to expire (5min)
  warp-shared: true           # Warp shared with you
  warp-updated: true          # Warp updated
```

### Notification types

#### Cooldown ready
> "Your cooldown is ready! You can now teleport to Home."

#### Warp expiring
> "Your warp Event will expire in 5 minutes!"

#### Warp shared
> "Steve has shared their warp Shop with you!"

#### Warp updated
> "Your warp Home has been updated."

Each notification is accompanied by a sound to draw attention.

---

## 🔌 PlaceholderAPI

Use EasyWarp placeholders in other plugins.

### Installation

1. Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
2. EasyWarp will automatically detect PlaceholderAPI

### Configuration

```yaml
placeholderapi:
  enabled: true
```

### Available placeholders

| Placeholder | Description | Example |
|------------|-------------|---------|
| `%easywarp_count%` | Player's warp count | `3` |
| `%easywarp_max%` | Warp limit | `5` |
| `%easywarp_remaining%` | Remaining warps | `2` |
| `%easywarp_cooldown%` | Cooldown in seconds | `0` |
| `%easywarp_last_warp%` | Last used warp | `Home` |
| `%easywarp_most_used%` | Most used warp | `Shop` |
| `%easywarp_total_uses%` | Total teleportations | `142` |

### Usage examples

**In a scoreboard**:
```yaml
lines:
  - "Warps: %easywarp_count%/%easywarp_max%"
  - "Cooldown: %easywarp_cooldown%s"
```

**In a GUI menu**:
```yaml
display-name: "&6Warps &7(%easywarp_count%/%easywarp_max%)"
```

---

## 🔄 Additional Commands

New commands to facilitate warp management.

### Rename a warp

```bash
/pwarp rename <old_name> <new_name>
```

**Permission**: `easywarp.warp.rename`

**Example**:
```bash
/pwarp rename House Home
/pwarp rename old_shop Shop_2025
```

### Copy a warp

```bash
/pwarp copy <source> <destination>
```

Creates an exact copy of an existing warp.

**Permission**: `easywarp.warp.copy`

**Example**:
```bash
/pwarp copy Home Home_Backup
/pwarp copy Shop Shop_Old
```

### Nearest warp

```bash
/pwarp nearest
```

Shows and teleports to the nearest warp from your position.

**Permission**: `easywarp.warp.use`

**Result**:
```
Nearest warp: Home (42m away)
```

### Random warp

```bash
/pwarp random
```

Teleports to a randomly chosen warp.

**Permission**: `easywarp.warp.use`

### Favorites

```bash
/pwarp favorite <name>
```

Adds or removes a warp from your favorites. Favorites appear first in the GUI.

**Permission**: `easywarp.warp.favorite`

**Example**:
```bash
/pwarp favorite Home
# Home is now in your favorites ⭐
```

### TPA (Teleport Ask)

Request permission before teleporting to another player's warp.

```bash
/pwarp tpa <player> <warp>
```

The player receives:
> "Steve wants to teleport to your warp Shop. Use /warp tpaccept or /warp tpdeny"

**Response commands**:
- `/warp tpaccept` - Accepts the request
- `/warp tpdeny` - Denies the request

**Permission**: `easywarp.warp.tpa`

**Timeout**: 30 seconds by default

---

## 📝 Permissions Summary

All new permissions:

```yaml
permissions:
  easywarp.warp.share: true          # Share warps
  easywarp.warp.public: true         # Make warps public
  easywarp.warp.rename: true         # Rename warps
  easywarp.warp.copy: true           # Copy warps
  easywarp.warp.visit: true          # Visit public warps
  easywarp.warp.favorite: true       # Mark as favorites
  easywarp.warp.tpa: true            # Teleport requests
  easywarp.limit.default: true       # Default limit (5)
  easywarp.limit.vip: false          # VIP limit (15)
  easywarp.limit.premium: false      # Premium limit (50)
  easywarp.limit.unlimited: false    # Unlimited
  easywarp.bypass.cooldown: false    # Bypass cooldown
  easywarp.bypass.delay: false       # Bypass delay
  easywarp.bypass.cost: false        # Bypass costs
  easywarp.bypass.conditions: false  # Bypass conditions
```

---

## 🎓 Complete Configuration Examples

See example pages:
- [Advanced Configuration](../examples/advanced-config.md)
- [Multi-Group Setup](../examples/multi-group-setup.md)
- [Economy Integration](../examples/economy-integration.md)
