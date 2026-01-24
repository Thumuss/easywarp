# Messages & Localization

EasyWarp supports multi-language messages through YAML files. This guide shows you how to customize messages and add new languages.

## Message Files

Located in `plugins/EasyWarp/`:

```
EasyWarp/
├── messages_en.yml    # English
├── messages_fr.yml    # French
└── messages_<code>.yml # Your language
```

## Selecting Language

In `config.yml`:

```yaml
locale: en  # English
# or
locale: fr  # French
```

## Message Format

### messages_en.yml

```yaml
# Command error messages
command:
  use: "You don't have permission to use this command."
  delete: "You don't have permission to delete warps."
  set: "You don't have permission to create warps."
  reload: "You don't have permission to reload warps."
  edit: "You don't have permission to edit warps."
  disabled: "This command is currently disabled."
  no-args: "Please specify a warp name."
  invalid-material: "Invalid material: {0}. Using default."
  
# Success messages  
success:
  use: null  # null = no message
  delete: "Warp '{0}' has been deleted."
  set: "Warp '{0}' has been created at your location."
  reload: "Warps have been reloaded successfully."
  edit: "Warp '{0}' has been updated."
  teleport: "Teleporting to {0}..."

# Error messages
error:
  world-not-found: "The world '{0}' doesn't exist. Unable to teleport."
  warp-not-found: "Warp '{0}' doesn't exist."
  invalid-location: "Invalid warp location. Please contact an administrator."
  teleport-failed: "Failed to teleport. Please try again."
  config-error: "Error loading configuration: {0}"
  permission-error: "Error registering permissions: {0}"
  invalid-number: "Invalid number: {0}"

# Info messages
info:
  prefix: "§6[EasyWarp] §r"
  teleport-description: "§aTeleports you to {0}"
  warp-created-by: "§7Created by: §f{0}"
  warp-location: "§7Location: §f{0}, {1}, {2}"
  no-warps: "No warps available."
  no-results: "No warps found matching '{0}'."
```

## Placeholders

Use `{0}`, `{1}`, `{2}`, etc. for dynamic values:

```yaml
success:
  delete: "Warp '{0}' has been deleted."
  # {0} = warp name
  
error:
  warp-not-found: "Warp '{0}' doesn't exist."
  # {0} = warp name
  
info:
  warp-location: "§7Location: §f{0}, {1}, {2}"
  # {0} = x, {1} = y, {2} = z
```

## Color Codes

Use `§` or `&` for colors:

```yaml
# Using § (recommended)
success:
  set: "§aWarp created successfully!"

# Using & (also works)
success:
  set: "&aWarp created successfully!"
```

### Color Reference

| Code | Color | Code | Color |
|------|-------|------|-------|
| §0 | Black | §8 | Dark Gray |
| §1 | Dark Blue | §9 | Blue |
| §2 | Dark Green | §a | Green |
| §3 | Dark Aqua | §b | Aqua |
| §4 | Dark Red | §c | Red |
| §5 | Dark Purple | §d | Light Purple |
| §6 | Gold | §e | Yellow |
| §7 | Gray | §f | White |

### Formatting Codes

| Code | Effect |
|------|--------|
| §l | **Bold** |
| §m | ~~Strikethrough~~ |
| §n | Underline |
| §o | *Italic* |
| §r | Reset |

## Creating Custom Language

### 1. Copy Existing File

```bash
cd plugins/EasyWarp/
cp messages_en.yml messages_es.yml
```

### 2. Translate Messages

Edit `messages_es.yml`:

```yaml
command:
  use: "No tienes permiso para usar este comando."
  delete: "No tienes permiso para eliminar warps."
  set: "No tienes permiso para crear warps."
  # ... translate all messages
```

### 3. Activate Language

In `config.yml`:

```yaml
locale: es
```

### 4. Reload

```
/pwarp reload
```

## Customization Examples

### Minimal Messages

```yaml
success:
  use: null  # No message on teleport
  delete: null  # No message on delete
  set: "§aCreated!"  # Short message
```

### Verbose Messages

```yaml
success:
  delete: "§aSuccess! §7The warp §e'{0}'§7 has been permanently deleted from your warp list."
  set: "§aWarp created! §7You can now teleport to §e'{0}'§7 using §f/pwarp§7."
```

### Themed Messages

```yaml
# Fantasy theme
success:
  set: "§5✨ Magical waypoint '{0}' created!"
  delete: "§c⚔ Waypoint '{0}' destroyed!"
  
# Technical theme
success:
  set: "§b[SYSTEM] Coordinates saved: '{0}'"
  delete: "§c[SYSTEM] Coordinates erased: '{0}'"
```

### Custom Prefix

```yaml
info:
  prefix: "§6[§eWarps§6] §r"
  # or
  prefix: "§8[§7W§8] §r"
  # or
  prefix: "§b» §r"
```

## Message Keys Reference

### Command Messages

| Key | Usage | Placeholders |
|-----|-------|--------------|
| `command.use` | No permission to use | None |
| `command.set` | No permission to set | None |
| `command.delete` | No permission to delete | None |
| `command.edit` | No permission to edit | None |
| `command.reload` | No permission to reload | None |
| `command.disabled` | Command disabled | None |
| `command.no-args` | Missing arguments | None |
| `command.invalid-material` | Invalid material | {0} = material name |

### Success Messages

| Key | Usage | Placeholders |
|-----|-------|--------------|
| `success.use` | On warp open (can be null) | None |
| `success.set` | Warp created | {0} = warp name |
| `success.delete` | Warp deleted | {0} = warp name |
| `success.edit` | Warp edited | {0} = warp name |
| `success.reload` | Config reloaded | None |

### Error Messages

| Key | Usage | Placeholders |
|-----|-------|--------------|
| `error.world-not-found` | World doesn't exist | {0} = world name |
| `error.warp-not-found` | Warp doesn't exist | {0} = warp name |
| `error.invalid-location` | Bad location data | None |
| `error.teleport-failed` | Teleport failed | None |
| `error.config-error` | Config error | {0} = error message |
| `error.invalid-number` | Invalid number | {0} = provided value |

### Info Messages

| Key | Usage | Placeholders |
|-----|-------|--------------|
| `info.prefix` | Message prefix | None |
| `info.no-warps` | No warps available | None |
| `info.no-results` | Search found nothing | {0} = search term |

## Best Practices

### Keep it Consistent

```yaml
# Good - consistent style
success:
  set: "Warp '{0}' created."
  delete: "Warp '{0}' deleted."
  edit: "Warp '{0}' updated."

# Bad - inconsistent
success:
  set: "Created warp: {0}!"
  delete: "Successfully removed {0}"
  edit: "{0} has been edited"
```

### Use Color Wisely

```yaml
# Good - readable
success:
  set: "§aWarp '{0}' created."
error:
  warp-not-found: "§cWarp '{0}' not found."

# Bad - too colorful
success:
  set: "§a§lW§b§la§c§lr§d§lp§e§l '{0}' §a§lc§b§lr§c§le§d§la§e§lt§a§led!"
```

### Null for Silent

```yaml
# Don't show message
success:
  use: null

# Empty string still shows prefix
success:
  use: ""
```

## Fallback System

If a message key is missing:

1. Checks current language file
2. Falls back to English (`messages_en.yml`)
3. Uses hardcoded default if neither exists

## Reloading Messages

Messages are loaded on:
- Server start
- `/pwarp reload`
- `/warp reload`
- Plugin reload

Changes require reload to take effect.

## Troubleshooting

### Messages Not Changing

1. Check file name: `messages_<code>.yml`
2. Verify locale in config.yml
3. Reload: `/pwarp reload`
4. Check console for errors

### Placeholders Not Working

```yaml
# Wrong
success:
  delete: "Warp {warp} deleted."

# Correct
success:
  delete: "Warp {0} deleted."
```

### Colors Not Showing

```yaml
# Use § not &
success:
  set: "§aCreated!"  # ✓

# Or enable & support in your server
```

## Contributing Languages

Want to add your language to EasyWarp?

1. Create `messages_<code>.yml`
2. Translate all keys from English
3. Test thoroughly
4. Submit pull request on GitHub

## Next Steps

- [Configure Cooldowns](./cooldowns.md)
- [View All Config Options](./config.md)
- [See Usage Examples](../usage/commands.md)
