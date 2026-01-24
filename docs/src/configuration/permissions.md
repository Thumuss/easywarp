# Permissions
EasyWarp automatically generates permissions based on your configuration. This guide explains how permissions work and how to configure them.

## Understanding Permission System

### Auto-Generated Permissions

For each warp system, EasyWarp creates:

```
easywarp.<base>.*           # Parent permission (all actions)
easywarp.<base>.use         # Open GUI / teleport
easywarp.<base>.set         # Create warps
easywarp.<base>.delete      # Delete warps
easywarp.<base>.edit        # Edit warp icons
easywarp.<base>.reload      # Reload configuration
```

### Configuration

Permissions are defined in `config.yml`:

```yaml
warps:
  players:
    permissions:
      base: easywarp.players  # Base permission node
      use: true               # Enable this action
      set: true
      delete: true
      edit: true
      reload: true
      defaultFor:
        use: true      # Who gets by default
        set: false
        delete: false
        edit: false
        reload: false
```

## Permission Nodes Reference

### Player Warps (Default Config)

| Permission | Description | Default |
|------------|-------------|---------|
| `easywarp.players.*` | All player warp permissions | OP |
| `easywarp.players.use` | Open warp menu & teleport | Everyone |
| `easywarp.players.set` | Create new warps | OP |
| `easywarp.players.delete` | Delete warps | OP |
| `easywarp.players.edit` | Edit warp icons | OP |
| `easywarp.players.reload` | Reload configuration | OP |

### Global Warps (Default Config)

| Permission | Description | Default |
|------------|-------------|---------|
| `easywarp.ww.*` | All global warp permissions | OP |
| `easywarp.ww.use` | Open warp menu & teleport | Everyone |
| `easywarp.ww.set` | Create global warps | OP |
| `easywarp.ww.delete` | Delete global warps | OP |
| `easywarp.ww.edit` | Edit warp icons | OP |
| `easywarp.ww.reload` | Reload configuration | OP |

### Special Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `easywarp.cooldown.bypass` | Bypass cooldown system | OP |
| `easywarp.*` | All EasyWarp permissions | OP |

## Setting Up Permissions

### Using LuckPerms

```bash
# Grant all player warp permissions
lp group default permission set easywarp.players.* true

# Grant use-only access
lp group default permission set easywarp.players.use true

# Grant VIP warp access
lp group vip permission set easywarp.vip.* true

# Grant cooldown bypass
lp user Thumus permission set easywarp.cooldown.bypass true
```

### Using PermissionsEx

```bash
pex group default add easywarp.players.*
pex group vip add easywarp.vip.*
pex user Thumus add easywarp.cooldown.bypass
```

## Common Setups

### Survival Server

```yaml
groups:
  default:
    permissions:
      - easywarp.players.*    # Full home access
      - easywarp.ww.use       # Can use spawn warps
  
  moderator:
    permissions:
      - easywarp.ww.set       # Can create spawn warps
      - easywarp.ww.delete
  
  admin:
    permissions:
      - easywarp.*            # Everything
```

## Next Steps

- [Configure Messages](./messages.md)
- [Set Up Cooldowns](./cooldowns.md)