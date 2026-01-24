# Frequently Asked Questions

Common questions and answers about EasyWarp.

## General Questions

### What is EasyWarp?

EasyWarp is a lightweight, flexible teleportation plugin for Minecraft servers (Bukkit/Spigot/Paper). It allows players to create and manage personal waypoints, and admins to create server-wide warp points.

### What versions does it support?

- **Minecraft**: 1.13 to 1.21+
- **Server**: Bukkit, Spigot, Paper
- **Java**: 16+

### Is it free?

Yes, EasyWarp is completely free and open-source.

### Can I use it on my server?

Yes! EasyWarp can be used on any type of server:
- Survival
- Creative
- PvP
- Minigame hub
- Faction
- etc.

## Installation

### How do I install EasyWarp?

1. Download EasyWarp.jar
2. Place in `plugins/` folder
3. Restart server
4. Configure as needed

See [Installation Guide](../getting-started/installation.md) for details.

### Do I need any dependencies?

No required dependencies! Optional:
- **LuckPerms** / **PermissionsEx** for permissions
- **Vault** for economy integration (custom plugins)

### Can I use it with other teleport plugins?

Yes, but avoid command conflicts. Use unique command names in config.yml.

## Usage

### How do I create a warp?

```bash
# Player warp
/pwarp set <name>

# Global warp (admin)
/warp set <name>
```

### How do I teleport to a warp?

```bash
# Player warp
/pwarp <name>

# Global warp
/warp <name>
```

### How do I delete a warp?

```bash
/pwarp delete <name>
/warp delete <name>
```

### Can I have multiple homes?

Yes! Create multiple warps:
```bash
/pwarp set home1
/pwarp set home2
/pwarp set home3
```

### What's the difference between `/pwarp` and `/warp`?

- **`/pwarp`**: Personal warps (only you see them)
- **`/warp`**: Global warps (everyone sees them)

Each is independent and configurable.

## Permissions

### What permissions do I need?

**For players:**
```yaml
easywarp.player.use     # Use warps
easywarp.player.set     # Create warps
easywarp.player.delete  # Delete warps
```

**For admins:**
```yaml
easywarp.* # Full access
```

See [Permissions Guide](../configuration/permissions.md) for full list.

### How do I give permissions?

**LuckPerms:**
```bash
/lp group default permission set easywarp.player.use true
```

**PermissionsEx:**
```bash
/pex group default add easywarp.player.use
```

### Why don't my permissions work?

Check:
1. Permission plugin installed
2. Correct permission nodes
3. Player in correct group
4. `/lp user <player> permission info`

## Configuration

### Where is the config file?

`plugins/EasyWarp/config.yml`

### How do I change the cooldown?

In `config.yml`:
```yaml
cooldown:
  enabled: true
  time: 30  # seconds
```

See [Cooldown Configuration](../configuration/cooldowns.md).

### How do I change the delay?

In `config.yml`:
```yaml
teleportation:
  delay: 3  # seconds
  cancel-on-move: true
```

### How do I add more warp systems?

Add to `config.yml`:
```yaml
warp-systems:
  custom:
    name: "Custom System"
    command: "customwarp"
    permission: "easywarp.custom"
    material: "DIAMOND"
```

See [Multiple Systems](../examples/multiple-systems.md).

### How do I change the language?

In `config.yml`:
```yaml
locale: fr  # For French
```

Then edit `messages_fr.yml` as needed.

## Features

### Can players share warps?

No, player warps are private. But you can:
1. Create global warps (admins)
2. Share coordinates manually
3. Use multiple systems for different purposes

### Is there a warp limit?

Configurable per-system:
```yaml
warp-systems:
  player:
    max-warps-per-player: 10  # 10 warps max
```

Or use permissions:
```bash
/lp group default permission set easywarp.player.limit.10 true
```

### Can I set cooldowns?

Yes! Configure in `config.yml`:
```yaml
cooldown:
  enabled: true
  time: 30  # 30 second cooldown
```

### Do warps work across worlds?

Yes! Warps work across:
- Overworld
- Nether
- End
- Custom worlds

### Can I customize warp icons?

Yes!
```bash
/pwarp edit home material RED_BED
/warp edit spawn material BEACON
```

### Is there a GUI?

Yes, when you use a warp:
```bash
/pwarp home  # Opens GUI → Click to teleport
```

### Can I search warps?

Yes!
```bash
/pwarp search home
/warp search shop
```

### Is there pagination for long lists?

Yes!
```bash
/pwarp list      # Page 1
/pwarp list 2    # Page 2
```

## Performance

### Does it cause lag?

No! EasyWarp is optimized:
- Async file I/O
- Efficient caching
- Minimal memory usage

### How many warps can I have?

Thousands! No practical limit. Performance stays good.

### Does it slow down server startup?

No, loads very quickly even with many warps.

## Troubleshooting

### Commands don't work

Check:
1. Plugin enabled: `/plugins`
2. Correct spelling: `/pwarp` not `/playerwarp`
3. Permissions set
4. No command conflicts

### Warps don't save

Check:
1. File permissions (read/write)
2. Disk space
3. Console for errors
4. Try `/pwarp reload`

### "World doesn't exist" error

The warp's world was deleted or renamed. Options:
1. Restore the world
2. Delete the warp
3. Edit warp file manually

### Players can't use warps

Check permissions:
```bash
/lp group default permission set easywarp.player.use true
```

### Cooldown not working

Check:
1. Enabled in config: `cooldown.enabled: true`
2. Time > 0: `cooldown.time: 30`
3. No bypass permission

### Tab completion not working

Check:
1. Server version (1.13+)
2. Plugin enabled
3. Restart server

## Comparison

### EasyWarp vs EssentialsX?

| Feature | EasyWarp | EssentialsX |
|---------|----------|-------------|
| Lightweight | ✅ Yes | ❌ Large |
| Multiple systems | ✅ Yes | ❌ No |
| Configurable | ✅ Highly | ⚠️ Limited |
| Performance | ✅ Fast | ⚠️ Slower |
| Features | ⚠️ Warps only | ✅ Many features |

Choose EasyWarp if you only need warps and want performance.

### EasyWarp vs other warp plugins?

See [Why EasyWarp?](../getting-started/why-easywarp.md) for detailed comparison.

## Development

### Is there an API?

Yes! Full API for developers.

See [API Documentation](../developers/api-overview.md).

### Can I integrate with my plugin?

Yes! Listen to events or use the API:

```java
EasyWarpAPI api = EasyWarp.getAPI();
Warp warp = api.getWarp("player", player, "home");
api.teleport(player, warp);
```

See [Integration Examples](../developers/integration.md).

### Can I contribute?

Yes! Contributions welcome:
- Bug reports
- Feature requests
- Pull requests
- Translations

See [Contributing Guide](./contributing.md).

## Advanced

### Can I have different cooldowns per system?

Yes!
```yaml
warp-systems:
  player:
    cooldown:
      time: 60
  global:
    cooldown:
      time: 30
```

### Can I disable movement cancellation?

Yes!
```yaml
teleportation:
  cancel-on-move: false
```

### Can I make instant teleports?

Yes!
```yaml
teleportation:
  delay: 0
cooldown:
  enabled: false
```

### Can I limit warps per player?

Yes!
```yaml
warp-systems:
  player:
    max-warps-per-player: 10
```

Or via permissions:
```bash
/lp group default permission set easywarp.player.limit.10 true
```

### Can I backup my warps?

Yes! Warp files are in `plugins/EasyWarp/`:
```bash
player.yml
global.yml
```

Just copy these files to backup.

## Support

### Where can I get help?

- [Troubleshooting Guide](./troubleshooting.md)
- [GitHub Issues](https://github.com/your-repo/issues)
- [Discord Server](https://discord.gg/your-server)
- [SpigotMC Thread](https://spigotmc.org/resources/your-resource)

### I found a bug!

Report it on [GitHub Issues](https://github.com/your-repo/issues) with:
1. Server version
2. EasyWarp version
3. Steps to reproduce
4. Console errors
5. Configuration files

### I have a feature request!

Suggest it on [GitHub Issues](https://github.com/your-repo/issues)!

### Can I get support in my language?

Currently supported:
- English
- French

Want to add yours? See [Contributing](./contributing.md)!

## Common Issues

### "Plugin disabled due to error"

Check:
1. Server version (1.13+)
2. Java version (16+)
3. Console for specific error
4. Dependencies installed

### "Could not load config.yml"

Your config.yml has syntax errors. Check:
1. YAML syntax (spaces, not tabs)
2. Valid values
3. Backup and regenerate if needed

### "Command conflicts with another plugin"

Change command name in config.yml:
```yaml
warp-systems:
  player:
    command: "customname"  # Not "pwarp"
```

### "Warps reset after restart"

Check file permissions:
```bash
ls -la plugins/EasyWarp/
# Should show read/write access
```

### "Tab completion shows wrong names"

Reload the plugin:
```bash
/pwarp reload
```

## Next Steps

- [Read Troubleshooting Guide](./troubleshooting.md)
- [View All Commands](../usage/commands.md)
- [Check Configuration Options](../configuration/config.md)
- [Contribute to the Project](./contributing.md)
