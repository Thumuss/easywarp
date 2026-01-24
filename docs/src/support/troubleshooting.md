# Troubleshooting
Solutions to common problems and error messages.

## Plugin Won't Load

### Symptoms

- Plugin not in `/plugins` list
- Error in console on startup
- "Plugin disabled due to error"

### Solutions

#### Check Server Version

EasyWarp requires **Minecraft 1.13+**

```bash
# Check version
/version

# If < 1.13, upgrade server
```

#### Check Java Version

EasyWarp requires **Java 16+**

```bash
# Check Java version
java -version

# Should show: java version "16" or higher
```

#### Check for Errors

Look in console/logs:

```bash
# View latest log
cat logs/latest.log | grep EasyWarp

# Common errors:
# - "Unsupported API version"
# - "NoClassDefFoundError"
# - "Could not load plugin"
```

#### Verify File Integrity

```bash
# Check JAR file
ls -lh plugins/EasyWarp.jar

# Should be 100+ KB
# If smaller, redownload
```

#### Check Dependencies

If using development version:
```bash
# Ensure Bukkit/Spigot API available
# Rebuild if necessary
mvn clean package
```

## Commands Not Working

### `/pwarp` doesn't work

#### Check Plugin Enabled

```bash
/plugins

# Should show: EasyWarp (green)
# If red, check console for errors
```

#### Check Permissions

```bash
# LuckPerms
/lp user <player> permission check easywarp.player.use

# Should show: true
# If false, grant permission
/lp group default permission set easywarp.player.use true
```

#### Check Command Registration

Look in console at startup:

```
[EasyWarp] Registered command: /pwarp
```

If missing, check config.yml:
```yaml
warp-systems:
  player:
    command: "pwarp"  # Must match
```

#### Check for Conflicts

Another plugin may have `/pwarp`:

```bash
# Try full command
/easywarp:pwarp

# If works, conflict exists
# Change command in config.yml
```

### Tab Completion Not Working

#### Server Version

Tab completion requires **1.13+**

```bash
/version
# Must be 1.13 or higher
```

#### Reload Plugin

```bash
/pwarp reload
# Refreshes tab completion
```

#### Check Console

Look for tab completion errors:
```
[EasyWarp] [ERROR] Failed to register tab completer
```

## Permissions Issues

### Players Can't Use Warps

#### Verify Permission Plugin

```bash
# Check if LuckPerms/PermissionsEx installed
/plugins

# LuckPerms should be green
```

#### Check Permissions

```bash
# LuckPerms - view player permissions
/lp user <player> permission info

# Should include:
# - easywarp.player.use
# - easywarp.player.set
```

#### Grant Permissions

```bash
# Give to all players
/lp group default permission set easywarp.player.* true

# Or specific permissions
/lp group default permission set easywarp.player.use true
/lp group default permission set easywarp.player.set true
```

#### Check Inheritance

```bash
# View group hierarchy
/lp group default parent info

# Ensure inheritance chain is correct
```

### Permissions Work for Some, Not Others

#### Check User Groups

```bash
# View user's groups
/lp user <player> info

# Check which groups they're in
```

#### Check Negative Permissions

```bash
# Remove negative permissions
/lp user <player> permission unset easywarp.player.use false

# Set positive
/lp user <player> permission set easywarp.player.use true
```

#### Check World-Specific Permissions

```bash
# May have world context
/lp user <player> permission info

# Look for: world=world_nether
# Grant for all worlds if needed
```

## Warps Not Saving

### Symptoms

- Warps disappear after restart
- `/pwarp list` shows empty
- "Error saving warp" in console

### Solutions

#### Check File Permissions

```bash
# View permissions
ls -la plugins/EasyWarp/

# Should show read/write for owner
# If not, fix permissions
chmod 755 plugins/EasyWarp/
chmod 644 plugins/EasyWarp/*.yml
```

#### Check Disk Space

```bash
# Check available space
df -h

# Need at least a few MB free
```

#### Check for Errors

Look in console:
```
[EasyWarp] [ERROR] Failed to save warp.yml: Permission denied
[EasyWarp] [ERROR] Failed to save warp.yml: Disk full
```

#### Manual File Check

```bash
# View warp file
cat plugins/EasyWarp/player.yml

# Should contain YAML data
# If empty or corrupted, restore from backup
```

#### Force Save

```bash
# Reload to force save
/pwarp reload

# Check if file updated
ls -l plugins/EasyWarp/player.yml
```

## Teleportation Issues

### "World doesn't exist"

#### Check World Name

```bash
# List worlds
/mv list  # MultiVerse
# or
/worlds  # Bukkit

# Note exact world names
```

#### Edit Warp File

If world was renamed:

```bash
# Stop server
# Edit plugins/EasyWarp/player.yml

# Change:
world: "old_world_name"
# To:
world: "new_world_name"

# Start server
```

#### Recreate Warp

```bash
# Delete old warp
/pwarp delete broken_warp

# Create new one
/pwarp set new_warp
```

### "Invalid location"

#### Warp Data Corrupted

Check warp file:

```yaml
warps:
  home:
    x: 100.5     # Should be numbers
    y: 64.0
    z: -200.5
    world: "world"  # Should be string
```

If corrupted, restore from backup or delete.

#### World Not Loaded

```bash
# Check if world is loaded
/worlds

# Load world if needed
/mv load <worldname>
```

### Teleport Cancels Immediately

#### Movement Detection Too Sensitive

In config.yml:
```yaml
teleportation:
  cancel-on-move: false  # Disable movement check
```

#### Player in Combat

If using combat tag plugin:
- Combat tag may block teleports
- Wait for combat to end
- Or use bypass permission

## Configuration Problems

### Config Won't Load

#### YAML Syntax Error

Common issues:
```yaml
# Bad - wrong indentation
cooldown:
enabled: true

# Good - correct indentation
cooldown:
  enabled: true

# Bad - tabs instead of spaces
cooldown:
	enabled: true

# Good - spaces only
cooldown:
  enabled: true
```

#### Validate YAML

Use online YAML validator:
- https://www.yamllint.com/
- Paste your config.yml
- Fix errors shown

#### Regenerate Config

```bash
# Stop server
# Backup old config
cp plugins/EasyWarp/config.yml plugins/EasyWarp/config.yml.backup

# Delete old config
rm plugins/EasyWarp/config.yml

# Start server (generates new)
```

### Changes Don't Apply

#### Didn't Reload

After editing config:
```bash
/pwarp reload
```

#### Edited Wrong File

Check file location:
```bash
# Correct location
plugins/EasyWarp/config.yml

# Not these:
EasyWarp/config.yml
config.yml
server-folder/config.yml
```

#### Syntax Error

Check console for:
```
[EasyWarp] [ERROR] Failed to load config.yml: ...
```

Fix the error mentioned.

## Cooldown Issues

### Cooldown Not Working

#### Check Enabled

In config.yml:
```yaml
cooldown:
  enabled: true  # Must be true
  time: 30       # Must be > 0
```

#### Check Bypass Permission

Player may have bypass:
```bash
# Check permission
/lp user <player> permission check easywarp.player.cooldown.bypass

# If true, they bypass cooldown
# Remove if unwanted
/lp user <player> permission unset easywarp.player.cooldown.bypass
```

#### Reload Config

```bash
/pwarp reload
```

### Cooldown Too Long/Short

#### Adjust Time

In config.yml:
```yaml
cooldown:
  time: 60  # Change to desired seconds
```

#### Different Times Per System

```yaml
warp-systems:
  player:
    cooldown:
      time: 60  # Player warps: 60s
      
  global:
    cooldown:
      time: 30  # Global warps: 30s
```

## Performance Issues

### Server Lag When Using Warps

#### Enable Async I/O

Should be enabled by default, check code:
```java
// WarpCommandsBase.java
Bukkit.getScheduler().runTaskAsynchronously(...)
```

#### Reduce Warp Limits

In config.yml:
```yaml
warp-systems:
  player:
    max-warps-per-player: 10  # Reduce if needed
```

#### Check for File Corruption

Large corrupted files cause lag:
```bash
# Check file size
ls -lh plugins/EasyWarp/*.yml

# If abnormally large (>1MB), investigate
```

### Memory Usage High

#### Normal Usage

EasyWarp uses minimal memory (<50MB typically).

#### Check for Leaks

If memory grows continuously:
1. Report bug on GitHub
2. Include heap dump
3. Server details

## GUI Issues

### GUI Doesn't Open

#### Check Material

Invalid material causes GUI failure:

```yaml
# In warp file
warps:
  home:
    material: "INVALID_ITEM"  # Bad
```

Fix by editing:
```yaml
    material: "COMPASS"  # Good
```

#### Check World Loaded

If warp's world not loaded, GUI may fail.

#### Console Errors

Check for:
```
[EasyWarp] [ERROR] Failed to open GUI for player ...
```

### GUI Shows Wrong Info

#### Reload Warps

```bash
/pwarp reload
```

#### Cache Issue

Clear cache by restarting server.

## Error Messages

### "You don't have permission"

Grant permission:
```bash
/lp group default permission set easywarp.player.use true
```

### "Warp doesn't exist"

Warp was deleted or never existed:
```bash
# List warps
/pwarp list

# Create if needed
/pwarp set <name>
```

### "World doesn't exist"

World was deleted or renamed. See [Teleportation Issues](#world-doesnt-exist).

### "Invalid material"

Material name wrong:
```bash
# Use valid 1.13+ material
/pwarp edit home material COMPASS

# Not numeric ID
/pwarp edit home material 345  # ✗ Wrong
```

### "Please wait X seconds"

Cooldown active. Wait or use bypass permission.

### "Teleport cancelled"

You moved during delay. Stand still or disable `cancel-on-move`.

## Debug Mode

### Enable Debug Logging

In config.yml:
```yaml
debug: true
```

### View Debug Output

Check console for:
```
[EasyWarp] [DEBUG] ...
```

Shows detailed execution info.

### Disable Debug

Set back to false:
```yaml
debug: false
```

## Still Need Help?

### Gather Information

Before asking for help, collect:

1. **Server Info:**
   - `/version` output
   - Java version (`java -version`)
   
2. **Plugin Info:**
   - EasyWarp version
   - `/plugins` list
   
3. **Error Details:**
   - Exact error message
   - Console logs
   - Steps to reproduce
   
4. **Configuration:**
   - config.yml content
   - Relevant permissions
   
5. **Attempts:**
   - What you tried
   - Results

### Get Support

- [FAQ](./faq.md)
- [GitHub Issues](https://github.com/your-repo/issues)
- [Discord Server](https://discord.gg/your-server)
- [SpigotMC Forums](https://spigotmc.org/resources/your-resource)

### Reporting Bugs

Include:
1. Server version
2. EasyWarp version
3. Steps to reproduce
4. Expected behavior
5. Actual behavior
6. Console errors
7. Configuration files

## Common Solutions Summary

| Problem | Quick Fix |
|---------|-----------|
| Commands don't work | Check permissions |
| Warps don't save | Check file permissions |
| "World doesn't exist" | Check world name/load world |
| Cooldown not working | Check config enabled |
| Config won't load | Validate YAML syntax |
| GUI doesn't open | Check material names |
| Tab completion broken | Reload plugin |
| Permission denied | Grant easywarp.player.* |

## Next Steps

- [Read FAQ](./faq.md)
- [View Configuration Guide](../configuration/config.md)
- [Check Permissions Reference](../configuration/permissions.md)
- [Contribute Fix](./contributing.md)