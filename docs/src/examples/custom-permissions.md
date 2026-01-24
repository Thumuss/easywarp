# Custom Permissions
Configure fine-grained access control with custom permission setups.

## Permission Structure

EasyWarp uses hierarchical permissions:

```
easywarp.{system}.{action}
```

Examples:
- `easywarp.player.use` - Use player warps
- `easywarp.global.set` - Create global warps
- `easywarp.admin.delete` - Delete admin warps

## Basic Permissions

### Player System

```yaml
easywarp.player.use        # Use player warps
easywarp.player.set        # Create warps
easywarp.player.delete     # Delete own warps
easywarp.player.edit       # Edit own warps
easywarp.player.reload     # Reload config
easywarp.player.list       # List warps
easywarp.player.info       # View warp info
easywarp.player.search     # Search warps
```

### Global System

```yaml
easywarp.global.use        # Use global warps
easywarp.global.set        # Create global warps
easywarp.global.delete     # Delete global warps
easywarp.global.edit       # Edit global warps
easywarp.global.reload     # Reload config
```

### Bypass Permissions

```yaml
easywarp.player.cooldown.bypass   # No cooldown
easywarp.player.delay.bypass      # Instant teleport
easywarp.player.limit.bypass      # No warp limit
```

## Permission Groups

### Default Players

Give basic access:

```bash
# LuckPerms
/lp group default permission set easywarp.player.use true
/lp group default permission set easywarp.player.set true
/lp group default permission set easywarp.player.delete true
/lp group default permission set easywarp.player.list true
/lp group default permission set easywarp.player.info true
/lp group default permission set easywarp.player.search true

# PermissionsEx
/pex group default add easywarp.player.use
/pex group default add easywarp.player.set
/pex group default add easywarp.player.delete
```

### VIP Players

Add bypass permissions:

```bash
# LuckPerms
/lp group vip permission set easywarp.player.* true
/lp group vip permission set easywarp.player.cooldown.bypass true
/lp group vip permission set easywarp.player.limit.bypass true

# PermissionsEx
/pex group vip add easywarp.player.*
/pex group vip add easywarp.player.cooldown.bypass
```

### Moderators

Can manage but not bypass:

```bash
# LuckPerms
/lp group mod permission set easywarp.player.* true
/lp group mod permission set easywarp.global.use true
/lp group mod permission set easywarp.global.delete true

# PermissionsEx
/pex group mod add easywarp.player.*
/pex group mod add easywarp.global.use
/pex group mod add easywarp.global.delete
```

### Administrators

Full access:

```bash
# LuckPerms
/lp group admin permission set easywarp.* true

# PermissionsEx
/pex group admin add easywarp.*
```

## Advanced Setups

### Read-Only Global Warps

Players can use but not create:

```bash
# Default group
/lp group default permission set easywarp.global.use true

# Admin group can create
/lp group admin permission set easywarp.global.set true
/lp group admin permission set easywarp.global.delete true
```

### Limited Personal Warps

Limit number of warps:

```bash
# Default: 5 warps
/lp group default permission set easywarp.player.limit.5 true

# VIP: 15 warps
/lp group vip permission set easywarp.player.limit.15 true

# Premium: Unlimited
/lp group premium permission set easywarp.player.limit.bypass true
```

### Tiered Cooldowns

Different cooldown times:

```yaml
# config.yml
warp-systems:
  player:
    cooldown:
      enabled: true
      time: 60  # Default 60s
      vip-time: 30  # VIP 30s
      premium-time: 15  # Premium 15s
```

```bash
# Default players: 60s cooldown
/lp group default permission set easywarp.player.use true

# VIP: 30s cooldown
/lp group vip permission set easywarp.player.cooldown.vip true

# Premium: No cooldown
/lp group premium permission set easywarp.player.cooldown.bypass true
```

### World-Specific Permissions

```yaml
# LuckPerms contexts
/lp group default permission set easywarp.player.use true world=world
/lp group default permission set easywarp.player.use false world=world_nether
/lp group vip permission set easywarp.player.use true world=world_nether
```

### Staff Hierarchy

```bash
# Helper: View only
/lp group helper permission set easywarp.player.list true
/lp group helper permission set easywarp.player.info true
/lp group helper permission set easywarp.global.list true

# Mod: Can delete problematic warps
/lp group mod parent set helper
/lp group mod permission set easywarp.player.delete true
/lp group mod permission set easywarp.global.delete true

# Admin: Full control
/lp group admin parent set mod
/lp group admin permission set easywarp.* true
```

## Permission Examples

### Survival Server

```bash
# All players
/lp group default permission set easywarp.player.* true
/lp group default permission set easywarp.player.limit.10 true
/lp group default permission set easywarp.global.use true

# Donors
/lp group donor permission set easywarp.player.limit.25 true
/lp group donor permission set easywarp.player.cooldown.bypass true
/lp group donor permission set easywarp.player.delay.bypass true

# Staff
/lp group staff permission set easywarp.* true
```

### Creative Server

```bash
# All players - no limits
/lp group default permission set easywarp.player.* true
/lp group default permission set easywarp.player.limit.bypass true
/lp group default permission set easywarp.player.cooldown.bypass true
/lp group default permission set easywarp.global.use true

# Builders - can create showcases
/lp group builder permission set easywarp.global.set true
/lp group builder permission set easywarp.global.delete true
```

### Minigame Hub

```bash
# All players
/lp group default permission set easywarp.lobby.use true
/lp group default permission set easywarp.game.use true
/lp group default permission set easywarp.lobby.cooldown.bypass true

# VIP
/lp group vip permission set easywarp.vip.use true
/lp group vip permission set easywarp.game.cooldown.bypass true

# Staff
/lp group staff permission set easywarp.* true
```

## Temporary Permissions

### Timed Perks

```bash
# Give VIP perks for 30 days
/lp user Steve permission settemp easywarp.player.cooldown.bypass true 30d
/lp user Steve permission settemp easywarp.player.limit.bypass true 30d

# Trial period (7 days)
/lp user Alex permission settemp easywarp.premium.* true 7d
```

### Event Permissions

```bash
# During event - everyone gets VIP perks
/lp group default permission settemp easywarp.player.cooldown.bypass true 2h
/lp group default permission settemp easywarp.player.delay.bypass true 2h

# After event - auto-revokes
```

## Per-Warp Permissions

### Custom Warp Access

Require specific permission for certain warps:

```yaml
# In your custom plugin
public void onWarpUse(WarpTeleportEvent event) {
    String warpName = event.getWarpName();
    Player player = event.getPlayer();
    
    // VIP-only warps
    if (warpName.startsWith("vip_")) {
        if (!player.hasPermission("easywarp.vip.access")) {
            event.setCancelled(true);
            player.sendMessage("§cThis warp is VIP-only!");
        }
    }
    
    // Staff-only warps
    if (warpName.startsWith("staff_")) {
        if (!player.hasPermission("easywarp.staff.access")) {
            event.setCancelled(true);
            player.sendMessage("§cThis warp is staff-only!");
        }
    }
}
```

## Inheritance Hierarchy

### Example Setup

```bash
# Create hierarchy
/lp creategroup member
/lp creategroup vip
/lp creategroup elite
/lp creategroup admin

# Set inheritance
/lp group vip parent set member
/lp group elite parent set vip
/lp group admin parent set elite

# Member permissions
/lp group member permission set easywarp.player.use true
/lp group member permission set easywarp.player.set true
/lp group member permission set easywarp.player.limit.5 true

# VIP inherits member + adds
/lp group vip permission set easywarp.player.limit.15 true
/lp group vip permission set easywarp.player.cooldown.bypass true

# Elite inherits vip + adds
/lp group elite permission set easywarp.player.limit.bypass true
/lp group elite permission set easywarp.player.delay.bypass true

# Admin inherits elite + adds
/lp group admin permission set easywarp.* true
```

## Meta Permissions

### Set Warp Limits via Meta

```bash
# Using meta values
/lp group default meta set easywarp.max-warps 10
/lp group vip meta set easywarp.max-warps 25
/lp group premium meta set easywarp.max-warps 50
```

```java
// In your plugin
public int getMaxWarps(Player player) {
    String meta = luckPerms.getUserManager()
        .getUser(player.getUniqueId())
        .getCachedData()
        .getMetaData()
        .getMetaValue("easywarp.max-warps");
        
    return meta != null ? Integer.parseInt(meta) : 10;
}
```

## Best Practices

### Start Restrictive

```bash
# Give minimal permissions first
/lp group default permission set easywarp.player.use true
/lp group default permission set easywarp.player.set true

# Add more as needed
```

### Use Wildcards Carefully

```bash
# Good - specific system
/lp group admin permission set easywarp.player.* true

# Careful - all systems
/lp group admin permission set easywarp.* true
```

### Document Your Setup

Keep a reference:

```
Permission Hierarchy:
1. default: Basic player warps (5 warps, 60s cooldown)
2. vip: More warps (15 warps, no cooldown)
3. elite: Unlimited warps, instant teleport
4. mod: Can delete warps, view all
5. admin: Full access
```

### Test Thoroughly

```bash
# Test as each group
/lp user TestPlayer group set default
# Try commands

/lp user TestPlayer group set vip
# Try commands

# etc.
```

## Troubleshooting

### Permission Not Working

Check:
```bash
# View player permissions
/lp user Steve permission info

# Check inheritance
/lp group default parent info

# Test specific permission
/lp user Steve permission check easywarp.player.use
```

### Too Many Permissions

Consolidate with wildcards:

```bash
# Instead of
/lp group default permission set easywarp.player.use true
/lp group default permission set easywarp.player.set true
/lp group default permission set easywarp.player.delete true
/lp group default permission set easywarp.player.list true

# Use
/lp group default permission set easywarp.player.* true
```

### Conflicting Permissions

Check negations:

```bash
# Remove negative permissions
/lp user Steve permission unset easywarp.player.use false

# Set positive
/lp user Steve permission set easywarp.player.use true
```

## Security Considerations

### Protect Admin Commands

```bash
# Don't give to regular players
easywarp.*.reload
easywarp.*.edit
easywarp.*.admin
```

### Limit Warp Creation

```bash
# Prevent spam
/lp group default permission set easywarp.player.limit.10 true
```

### Restrict Global Warps

```bash
# Only admins can create
/lp group admin permission set easywarp.global.set true

# Everyone can use
/lp group default permission set easywarp.global.use true
```

## Next Steps

- [View All Permissions](../configuration/permissions.md)
- [Set Up Multiple Systems](./multiple-systems.md)
- [Read Configuration Guide](../configuration/config.md)