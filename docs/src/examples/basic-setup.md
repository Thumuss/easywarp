# Basic Setup
Get EasyWarp running on your server in minutes with this step-by-step guide.

## Fresh Installation

### 1. Download

Download EasyWarp.jar from:
- GitHub Releases
- SpigotMC
- Your build from Maven

### 2. Install

```bash
# Copy to plugins folder
cp EasyWarp.jar /server/plugins/

# Or drag-and-drop in file manager
```

### 3. Start Server

```bash
java -jar spigot.jar
```

Watch console for:
```
[EasyWarp] Enabling EasyWarp v1.0.0
[EasyWarp] Loaded 2 warp systems
[EasyWarp] Registered command: /pwarp
[EasyWarp] Registered command: /warp
[EasyWarp] EasyWarp enabled successfully!
```

### 4. Verify

```bash
# In-game
/pwarp
/warp

# Should show GUI or usage message
```

## Initial Configuration

### Files Created

```
plugins/EasyWarp/
├── config.yml           # Main configuration
├── player.yml           # Player warps storage
├── global.yml           # Global warps storage
├── messages_en.yml      # English messages
└── messages_fr.yml      # French messages
```

### Default Config

`config.yml` is auto-generated:

```yaml
locale: en

cooldown:
  enabled: true
  time: 30

teleportation:
  delay: 3
  cancel-on-move: true

warp-systems:
  player:
    name: "Player Warps"
    command: "pwarp"
    permission: "easywarp.player"
    material: "COMPASS"
    
  global:
    name: "Global Warps"
    command: "warp"
    permission: "easywarp.global"
    material: "ENDER_PEARL"
```

## Setting Permissions

### Using LuckPerms

```bash
# Give all players access to player warps
/lp group default permission set easywarp.player.use true
/lp group default permission set easywarp.player.set true
/lp group default permission set easywarp.player.delete true

# Give admins access to global warps
/lp group admin permission set easywarp.global.* true
```

### Using PermissionsEx

```bash
# Default players
/pex group default add easywarp.player.use
/pex group default add easywarp.player.set
/pex group default add easywarp.player.delete

# Admins
/pex group admin add easywarp.global.*
```

### Permissions.yml (Bukkit)

Edit `permissions.yml`:

```yaml
groups:
  default:
    permissions:
      easywarp.player.use: true
      easywarp.player.set: true
      easywarp.player.delete: true
      
  admin:
    permissions:
      easywarp.global.*: true
```

## Creating First Warps

### Player Warp

As any player:

```bash
# Go to your base
/pwarp set home

# Teleport back
/pwarp home
```

### Global Warp

As admin:

```bash
# Go to spawn
/warp set spawn

# Players can now use
/warp spawn
```

## Testing

### Test Player Warps

```bash
# Create
/pwarp set test

# Use
/pwarp test

# List
/pwarp list

# Delete
/pwarp delete test
```

### Test Global Warps

```bash
# Create (admin)
/warp set test

# Use (anyone)
/warp test

# Delete (admin)
/warp delete test
```

### Test Tab Completion

```bash
/pwarp <TAB>
# Should show: set, delete, edit, list, info, search, reload

/pwarp delete <TAB>
# Should show your warps
```

## Common First Configurations

### Disable Cooldowns

For creative/practice server:

```yaml
cooldown:
  enabled: false
```

### Instant Teleports

```yaml
teleportation:
  delay: 0
  cancel-on-move: false
```

### Change Language

```yaml
locale: fr  # For French
```

### Increase Cooldown

For survival server:

```yaml
cooldown:
  enabled: true
  time: 120  # 2 minutes
```

## Quick Start Commands

### For Players

```bash
/pwarp set home          # Create home warp
/pwarp set base          # Create base warp
/pwarp set shop          # Create shop warp
/pwarp list              # List your warps
/pwarp home              # Teleport to home
```

### For Admins

```bash
/warp set spawn          # Create spawn point
/warp set shop           # Create shop warp
/warp set pvp            # Create PvP arena
/warp list               # List global warps
/pwarp reload            # Reload config
```

## Verification Checklist

- [ ] Plugin enabled without errors
- [ ] Commands work (`/pwarp`, `/warp`)
- [ ] Permissions set correctly
- [ ] Players can create warps
- [ ] Players can teleport
- [ ] Tab completion works
- [ ] Config file generated
- [ ] Messages display correctly

## Troubleshooting

### Plugin Won't Load

Check:
- Server version (1.13+)
- Java version (16+)
- No conflicting plugins
- Console errors

### Commands Don't Work

Check:
- Plugin enabled: `/plugins`
- Spelling: `/pwarp` not `/playerWarp`
- Permissions set
- Not blocked by another plugin

### Permissions Don't Work

Check:
- Permission plugin installed
- Correct permission nodes
- Group inheritance
- User in correct group

### Warps Don't Save

Check:
- File permissions (read/write)
- Disk space
- Console errors
- Try `/pwarp reload`

## Next Steps

- [Configure Multiple Systems](./multiple-systems.md)
- [Set Up Custom Permissions](./custom-permissions.md)
- [Read Full Configuration Guide](../configuration/config.md)

## Example: Complete Setup

```bash
# 1. Install plugin
cp EasyWarp.jar /server/plugins/

# 2. Start server
java -jar spigot.jar

# 3. Set permissions (in-game)
/lp group default permission set easywarp.player.* true
/lp group admin permission set easywarp.global.* true

# 4. Create spawn warp (admin)
/warp set spawn

# 5. Players can now use
/pwarp set home
/pwarp home
/warp spawn

# Done!
```

## Video Tutorial

*(If available, link to video tutorial)*

## Support

Need help?
- [FAQ](../support/faq.md)
- [Troubleshooting](../support/troubleshooting.md)
- [Discord Server](https://discord.gg/your-server)
- [GitHub Issues](https://github.com/your-repo/issues)