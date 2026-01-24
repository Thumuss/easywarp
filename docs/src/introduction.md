# EasyWarp

**A flexible, powerful, and user-friendly warp plugin for Minecraft 1.13-1.21**

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/minecraft-1.13--1.21-green)
![License](https://img.shields.io/badge/license-MIT-yellow)

## Welcome to EasyWarp

EasyWarp is a modern Minecraft plugin that revolutionizes the way players manage and use warps. Built with flexibility and performance in mind, it provides a dynamic, configuration-driven approach to warp management.

## Key Features

### 🎯 Dynamic Configuration
- **Config-Driven Commands**: All warp systems are defined in `config.yml` - no hardcoded limitations
- **Unlimited Warp Systems**: Create as many different warp systems as you need (player warps, admin warps, VIP warps, etc.)
- **Custom Commands & Aliases**: Define your own command names and aliases for each warp system

### 🌍 Multi-Language Support
- Built-in English and French translations
- Easy to add more languages via simple YAML files
- Placeholder support for dynamic messages

### 🚀 Performance Optimized
- **Asynchronous File Operations**: All I/O operations run async to prevent server lag
- **Smart Caching**: Material and world lookups are cached for better performance
- **File Change Detection**: Inventories only reload when actually modified

### 🎨 Advanced Features
- **GUI Inventory System**: Beautiful and intuitive warp selection
- **Teleportation Cooldowns**: Configurable per-system cooldowns with bypass permissions
- **Teleportation Delays**: Optional delay before teleporting with move cancellation
- **Tab Completion**: Smart auto-completion for commands, warp names, and materials
- **Event System**: Full API with cancellable events for developers
- **Command Suite**: list, info, search commands for easy warp management

### 🔒 Flexible Permissions
- **Automatic Permission Generation**: Permissions are created from config
- **Granular Control**: Separate permissions for use, set, delete, edit, reload
- **Default Permissions**: Configure which actions are available by default

### 🔧 Developer Friendly
- **Public API**: Easy integration with other plugins
- **Custom Events**: WarpCreateEvent, WarpDeleteEvent, WarpTeleportEvent
- **Well Documented**: Comprehensive JavaDoc and examples

## What Makes EasyWarp Different?

Unlike traditional warp plugins that have fixed functionality, EasyWarp is designed to adapt to YOUR server's needs:

- **No Code Changes Required**: Want a new warp system? Just edit the config
- **True Multi-System**: Have separate warps for players, staff, VIPs, etc., each with their own inventory
- **Modern Architecture**: Uses latest Bukkit APIs and best practices
- **Active Development**: Regular updates and community-driven improvements

## Quick Example

```yaml
# Define a player warp system in config.yml
warps:
  players:
    filename: warp.yml
    display: Personal Warps
    usingPlayerUID: true
    command: pwarp
    aliases: [p, pw]
```

That's it! The plugin automatically:
- Registers the `/pwarp` command with aliases
- Creates the necessary permissions
- Generates the GUI inventory
- Handles all warp operations

## Get Started

Ready to enhance your server with EasyWarp? Jump to the [Installation Guide](./getting-started/installation.md) to get started!

## Support & Community

- **GitHub**: [Report issues](https://github.com/Thumuss/EasyWarp/issues)
- **Website**: [thumus.eu](https://thumus.eu)
- **Author**: Thumus

---

*Built with ❤️ for the Minecraft community*
