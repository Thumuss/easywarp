# Quick Start

This guide will get you up and running with EasyWarp in 5 minutes.

## For Players

### Using Warps

1. **Open the warp menu**:
   ```
   /pwarp
   ```
   This opens your personal warp inventory.

2. **Create a warp**:
   ```
   /pwarp set Home
   ```
   Creates a warp named "Home" at your current location.

3. **Create a warp with custom icon**:
   ```
   /pwarp set Farm emerald
   ```
   Creates a warp with an emerald icon.

4. **Delete a warp**:
   ```
   /pwarp delete Home
   ```

5. **List all your warps**:
   ```
   /pwarp list
   ```

6. **Search for warps**:
   ```
   /pwarp search farm
   ```

7. **View warp details**:
   ```
   /pwarp info Home
   ```

### Tab Completion

EasyWarp provides smart tab completion:
- `/pwarp <TAB>` → Shows available actions
- `/pwarp delete <TAB>` → Shows your warp names
- `/pwarp set MyWarp <TAB>` → Shows material names

## For Administrators

### Global Warps

Create warps that all players can use:

```
/warp set Spawn
/warp set Shop diamond
/warp set PvP netherite_sword
```

### Permissions

Give players permission to manage global warps:

```yaml
# In your permissions plugin
permissions:
  - easywarp.ww.set      # Create global warps
  - easywarp.ww.delete   # Delete global warps
  - easywarp.ww.edit     # Edit global warps
```

### Reload Configuration

After editing config files:

```
/pwarp reload
/warp reload
```

## Common Tasks

### Change Warp Icon

```
/pwarp edit Home chest
```

### View All Player Warps

```
/pwarp list
/pwarp list 2    # Page 2
```

### Search for Specific Warps

```
/warp search spawn
```

### Get Warp Information

```
/warp info Spawn
```

## GUI Usage

When you open a warp menu (`/pwarp` or `/warp`), you'll see:

- **Warp Icons**: Click any item to teleport
- **Warp Names**: Shown as item display names
- **Descriptions**: Hover over items to see location info
- **Automatic Updates**: GUI updates automatically when warps change

## Tips & Tricks

### Material Names

Use Minecraft material names for custom icons:
- Common: `chest`, `diamond`, `emerald`, `gold_block`
- Tools: `diamond_pickaxe`, `iron_sword`, `bow`
- Decorative: `beacon`, `end_crystal`, `nether_star`

Full list: [Spigot Material List](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)

### Cooldowns

If you see "You must wait X seconds", you're on cooldown:
- Default cooldown: 3 seconds
- Bypass with permission: `easywarp.cooldown.bypass`
- Configure in `config.yml`

### Teleport Delays

If teleportation has a delay:
- Stay still during countdown
- Moving cancels teleportation (if configured)
- Configure in `config.yml`

## Next Steps

- Learn about [Configuration](../configuration/config.md)
- Explore [Advanced Features](../usage/advanced-features.md)
- Set up [Custom Permissions](../configuration/permissions.md)

## Need Help?

- Check the [FAQ](../support/faq.md)
- Read [Troubleshooting](../support/troubleshooting.md)
- Report issues on [GitHub](https://github.com/Thumuss/EasyWarp/issues)
