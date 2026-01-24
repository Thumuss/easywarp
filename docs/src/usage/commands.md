# Commands Reference

EasyWarp provides a comprehensive set of commands for managing warps. All commands support tab completion.

## Command Syntax

Commands are defined in `config.yml`. By default:

- **Player Warps**: `/pwarp` (aliases: `/p`, `/pw`, `/pwp`)
- **Global Warps**: `/warp` (aliases: `/w`, `/wp`)

> This guide uses `/pwarp` and `/warp` but your server may use different commands.

## Player Warp Commands

### Open Warp Menu

```
/pwarp
```

Opens the GUI inventory with all your warps. Click an item to teleport.

**Permission**: `easywarp.players.use` (default: everyone)

---

### Teleport to a Warp

```
/pwarp <name>
```

Teleports you to the specified warp.

**Arguments**:
- `<name>` - Warp name to teleport to

**Examples**:
```
/pwarp Home
/pwarp My_Farm
/pwarp Shop
/pwarp Nether_Portal
```

**Permission**: `easywarp.players.use`

**Tab Completion**: Shows your existing warp names

**Notes**:
- Opens a confirmation GUI before teleporting
- Subject to cooldown if configured
- May have a delay if configured
- Cancelled if you move during delay (if enabled)

---

### Set a Warp

```
/pwarp set <name> [material]
```

Creates a new warp at your current location.

**Arguments**:
- `<name>` - Warp name (required, use `_` for spaces)
- `[material]` - Icon material (optional, default: `DARK_OAK_BUTTON`)

**Examples**:
```
/pwarp set Home
/pwarp set My_Farm emerald
/pwarp set Shop chest
/pwarp set Nether_Portal obsidian
```

**Permission**: `easywarp.players.set`

**Notes**:
- Warp names are case-sensitive
- Use underscores for multi-word names
- Material names must be valid Bukkit materials

---

### Delete a Warp

```
/pwarp delete <name>
```

Removes a warp permanently.

**Arguments**:
- `<name>` - Warp name to delete

**Example**:
```
/pwarp delete Old_Base
```

**Permission**: `easywarp.players.delete`

**Tab Completion**: Shows your existing warp names

---

### Edit a Warp

```
/pwarp edit <name> <material>
```

Changes the icon material of an existing warp.

**Arguments**:
- `<name>` - Warp name
- `<material>` - New material for the icon

**Example**:
```
/pwarp edit Home bed
/pwarp edit Farm diamond_hoe
```

**Permission**: `easywarp.players.edit`

**Tab Completion**: Shows warp names and materials

---

### List Warps

```
/pwarp list [page]
```

Displays a paginated list of all your warps (10 per page).

**Arguments**:
- `[page]` - Page number (optional, default: 1)

**Example**:
```
/pwarp list
/pwarp list 2
```

**Permission**: `easywarp.players.use`

**Output**:
```
────── Warps List (Page 1/3) ──────
• Home [BED] (world)
• Farm [EMERALD] (world)
• Nether_Portal [OBSIDIAN] (world_nether)
• Shop [CHEST] (world)
...
[<< Prev] Total: 25 warps [Next >>]
```

---

### Search Warps

```
/pwarp search <term>
```

Searches for warps matching the search term.

**Arguments**:
- `<term>` - Search query (case-insensitive)

**Example**:
```
/pwarp search farm
/pwarp search nether
```

**Permission**: `easywarp.players.use`

**Output**:
```
──── Search Results for 'farm' ────
Found 3 warp(s):
• Farm [EMERALD] (world)
• Farm_2 [WHEAT] (world)
• Ice_Farm [ICE] (world)
```

---

### Warp Info

```
/pwarp info <name>
```

Shows detailed information about a warp.

**Arguments**:
- `<name>` - Warp name

**Example**:
```
/pwarp info Home
```

**Permission**: `easywarp.players.use`

**Output**:
```
──────── Warp Info ────────
Name: Home
World: world
Location: x=100.0 y=64.0 z=200.0
Icon: BED
```

---

### Reload Warps

```
/pwarp reload
```

Reloads the warp configuration and data from disk.

**Permission**: `easywarp.players.reload`

**Use Cases**:
- After manual file edits
- After config changes
- To fix GUI issues

---

## Global Warp Commands

Global warps use the same commands but with `/warp`:

```
/warp                        # Open GUI
/warp set <name> [material]  # Create warp
/warp delete <name>          # Delete warp
/warp edit <name> <material> # Edit warp
/warp list [page]            # List warps
/warp search <term>          # Search warps
/warp info <name>            # Warp details
/warp reload                 # Reload
```

**Key Differences**:
- Permissions use `easywarp.ww` prefix
- Warps are shared between all players
- Typically restricted to admins

## Command Permissions

### Player Warps

| Command | Permission | Default |
|---------|------------|---------|
| `/pwarp` | `easywarp.players.use` | Everyone |
| `/pwarp set` | `easywarp.players.set` | OP only |
| `/pwarp delete` | `easywarp.players.delete` | OP only |
| `/pwarp edit` | `easywarp.players.edit` | OP only |
| `/pwarp list` | `easywarp.players.use` | Everyone |
| `/pwarp search` | `easywarp.players.use` | Everyone |
| `/pwarp info` | `easywarp.players.use` | Everyone |
| `/pwarp reload` | `easywarp.players.reload` | OP only |

### Global Warps

| Command | Permission | Default |
|---------|------------|---------|
| `/warp` | `easywarp.ww.use` | Everyone |
| `/warp set` | `easywarp.ww.set` | OP only |
| `/warp delete` | `easywarp.ww.delete` | OP only |
| `/warp edit` | `easywarp.ww.edit` | OP only |
| `/warp list` | `easywarp.ww.use` | Everyone |
| `/warp search` | `easywarp.ww.use` | Everyone |
| `/warp info` | `easywarp.ww.use` | Everyone |
| `/warp reload` | `easywarp.ww.reload` | OP only |

## Tab Completion

EasyWarp provides intelligent tab completion:

### Action Completion
```
/pwarp <TAB>
→ delete, edit, set, reload, use, list, info, search
```

### Warp Name Completion
```
/pwarp delete <TAB>
→ Shows your existing warp names
```

### Material Completion
```
/pwarp set MyWarp <TAB>
→ chest, diamond, emerald, stone, ...
```

```
/pwarp edit MyWarp <TAB>
→ bed, compass, ender_pearl, ...
```

## Command Aliases

You can use shorter commands:

```bash
# These are equivalent:
/pwarp
/p
/pw
/pwp

# These are equivalent:
/warp
/w
/wp
```

Aliases are configured per warp system in `config.yml`:

```yaml
warps:
  players:
    command: pwarp
    aliases: [p, pw, pwp]
```

## Error Messages

### Common Errors

**"You don't have permission to use this command."**
- You lack the required permission
- Ask an admin for permission

**"Please specify a warp name."**
- Missing warp name argument
- Example: `/pwarp set Home`

**"Warp 'Name' doesn't exist."**
- Warp not found
- Check spelling with `/pwarp list`

**"Invalid material: XXXX"**
- Material name doesn't exist
- Use tab completion for valid materials

**"You must wait X seconds before teleporting again."**
- Cooldown active
- Wait or ask for bypass permission

## Tips & Tricks

### Quick Teleport
Open the GUI and click a warp - faster than typing commands!

### Organize by Icon
Use meaningful icons:
- 🏠 `bed` for homes
- 🌾 `wheat` for farms  
- 💎 `diamond` for valuable locations
- 🗡️ `iron_sword` for PvP areas

### Use Descriptive Names
```bash
# Good names:
/pwarp set Main_Base
/pwarp set Gold_Farm
/pwarp set End_Portal

# Less clear:
/pwarp set warp1
/pwarp set loc
```

### Search is Your Friend
With many warps, use search:
```bash
/pwarp search farm    # Find all farm warps
/pwarp search end     # Find all End warps
```

### List for Overview
Check what you have:
```bash
/pwarp list
```

See the icons, worlds, and names at a glance.

## Next Steps

- [Learn about Permissions](../configuration/permissions.md)
- [Advanced Features](./advanced-features.md)
- [Set up Multiple Systems](../examples/multiple-systems.md)
