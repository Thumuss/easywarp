# Installation

## Requirements

- **Minecraft Server**: Spigot, Paper, or any compatible fork
- **Minecraft Version**: 1.13 or higher (tested up to 1.21)
- **Java**: Version 16 or higher

## Download

1. Download the latest `EasyWarp-1.0.0.jar` from the [releases page](https://github.com/Thumuss/EasyWarp/releases)

## Installation Steps

### 1. Install the Plugin

1. Stop your Minecraft server if it's running
2. Place `EasyWarp-1.0.0.jar` in your server's `plugins/` folder
3. Start your server

### 2. First Launch

On first launch, EasyWarp will automatically generate:

```
plugins/
└── EasyWarp/
    ├── config.yml          # Main configuration
    ├── messages_en.yml     # English messages
    ├── messages_fr.yml     # French messages
    ├── warp.yml           # Player warp data
    └── default.yml        # Global warp data
```

### 3. Verify Installation

Check your server console for:

```
[EasyWarp] Plugin loaded with 2 warp systems
[EasyWarp] Registered command: /pwarp with aliases: [p, pw, pwp]
[EasyWarp] Registered command: /warp with aliases: [w, wp]
```

If you see these messages, EasyWarp is successfully installed! 🎉

## Default Configuration

By default, EasyWarp comes with two warp systems:

### Player Warps (`/pwarp`)
- Each player has their own set of warps
- Accessible via `/pwarp`, `/p`, `/pw`, or `/pwp`
- Default permission allows everyone to use warps

### Global Warps (`/warp`)
- Shared warps for all players
- Accessible via `/warp`, `/w`, or `/wp`
- Set/delete/edit requires special permissions

## Next Steps

- [Quick Start Guide](./quick-start.md) - Learn the basics
- [Configuration Guide](../configuration/config.md) - Customize your setup
- [Commands Reference](../usage/commands.md) - See all available commands

## Updating

To update EasyWarp:

1. Stop your server
2. Backup your `plugins/EasyWarp/` folder
3. Replace the old jar with the new version
4. Start your server
5. Check the changelog for any breaking changes

> **Note**: Your configuration files are never overwritten during updates.

## Troubleshooting

If you encounter issues during installation, check the [Troubleshooting Guide](../support/troubleshooting.md).
