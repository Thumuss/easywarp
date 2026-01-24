# Contributing
Thank you for considering contributing to EasyWarp! This guide will help you get started.

## Ways to Contribute

### 1. Report Bugs

Found a bug? Report it on [GitHub Issues](https://github.com/your-repo/issues).

**Include:**
- Server version (`/version`)
- EasyWarp version
- Java version (`java -version`)
- Steps to reproduce
- Expected vs actual behavior
- Console errors/logs
- Configuration files

**Example:**
```
**Bug:** Warps don't save after restart

**Environment:**
- Server: Paper 1.20.1
- EasyWarp: v1.0.0
- Java: 16

**Steps:**
1. Create warp: `/pwarp set home`
2. Restart server
3. Check warps: `/pwarp list` - empty

**Expected:** Warp should persist
**Actual:** Warp disappeared

**Logs:**
[EasyWarp] [ERROR] Failed to save warp.yml: Permission denied

**Config:** (attach config.yml)
```

### 2. Suggest Features

Have an idea? Open a [feature request](https://github.com/your-repo/issues)!

**Include:**
- Clear description
- Use case
- Benefits
- Examples

**Example:**
```
**Feature:** Warp categories

**Description:**
Allow players to organize warps into categories
(e.g., homes, shops, farms)

**Use Case:**
Players with many warps need better organization

**Benefits:**
- Easier to find specific warps
- Better UX for large warp lists
- Optional feature (backwards compatible)

**Example:**
/pwarp category homes list
/pwarp category homes add home1
```

### 3. Improve Documentation

Documentation can always be better!

**Areas to improve:**
- Fix typos/grammar
- Add examples
- Clarify confusing sections
- Add translations
- Update outdated info

**How to contribute:**
1. Fork repository
2. Edit files in `docs/src/`
3. Test with `mdbook serve`
4. Submit pull request

### 4. Add Translations

Help translate EasyWarp to your language!

**Steps:**

1. **Copy English File:**
   ```bash
   cd src/main/resources/
   cp messages_en.yml messages_<code>.yml
   ```

2. **Translate Messages:**
   ```yaml
   # messages_es.yml (Spanish example)
   command:
     use: "No tienes permiso para usar este comando."
     delete: "No tienes permiso para eliminar warps."
   ```

3. **Test Translation:**
   - Change `locale: es` in config
   - Test all commands
   - Verify formatting

4. **Submit:**
   - Create pull request
   - Include language code and name

**Currently Available:**
- English (`en`)
- French (`fr`)

**Wanted:**
- Spanish (`es`)
- German (`de`)
- Portuguese (`pt`)
- Italian (`it`)
- Russian (`ru`)
- Chinese (`zh`)
- Japanese (`ja`)
- And more!

### 5. Write Code

Contribute features, fixes, or improvements!

## Development Setup

### Prerequisites

- **Java 16+**
- **Maven 3.6+**
- **Git**
- **IDE** (IntelliJ IDEA recommended)

### Clone Repository

```bash
git clone https://github.com/your-username/EasyWarp.git
cd EasyWarp
```

### Build Project

```bash
# Compile and package
mvn clean package

# Output: target/EasyWarp-1.0.0.jar
```

### Test Locally

```bash
# Copy to test server
cp target/EasyWarp-1.0.0.jar /path/to/server/plugins/

# Start server
cd /path/to/server/
java -jar spigot.jar
```

### IDE Setup

#### IntelliJ IDEA

1. **Import Project:**
   - File → Open
   - Select `pom.xml`
   - Import as Maven project

2. **Set JDK:**
   - File → Project Structure
   - Project SDK: Java 16

3. **Enable Maven Auto-Import:**
   - Settings → Build → Maven
   - Check "Import Maven projects automatically"

#### Eclipse

1. **Import Project:**
   - File → Import → Maven → Existing Maven Projects
   - Select EasyWarp folder

2. **Set JDK:**
   - Right-click project → Properties
   - Java Build Path → Libraries
   - Add JRE System Library (Java 16)

## Code Guidelines

### Style

Follow Java conventions:

```java
// Good
public class WarpManager {
    private static final String PREFIX = "§6[EasyWarp] §r";
    
    public void createWarp(Player player, String name) {
        // Method body
    }
}

// Bad
public class warpmanager {
    public static String prefix = "§6[EasyWarp] §r";
    
    public void CreateWarp(Player p, String n) {
        // Method body
    }
}
```

### Naming

- **Classes:** PascalCase (`WarpManager`, `CommandHandler`)
- **Methods:** camelCase (`createWarp`, `deleteWarp`)
- **Variables:** camelCase (`warpName`, `playerUUID`)
- **Constants:** UPPER_SNAKE_CASE (`MAX_WARPS`, `DEFAULT_MATERIAL`)

### Comments

```java
/**
 * Creates a new warp at the player's location.
 *
 * @param player The player creating the warp
 * @param name The warp name
 * @throws IllegalArgumentException if name is invalid
 */
public void createWarp(Player player, String name) {
    // Validate name
    if (name == null || name.isEmpty()) {
        throw new IllegalArgumentException("Warp name cannot be empty");
    }
    
    // Create warp
    // ...
}
```

### Error Handling

```java
// Good
try {
    saveWarps();
} catch (IOException e) {
    getLogger().severe("Failed to save warps: " + e.getMessage());
    e.printStackTrace();
}

// Bad
try {
    saveWarps();
} catch (IOException e) {
    // Silent failure
}
```

### Async Operations

```java
// Good - async file I/O
Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
    // File operations
    saveToFile();
    
    // Update UI on main thread
    Bukkit.getScheduler().runTask(plugin, () -> {
        player.sendMessage("Saved!");
    });
});

// Bad - blocking main thread
saveToFile();  // Causes lag!
```

## Pull Request Process

### 1. Fork Repository

Click "Fork" on GitHub.

### 2. Create Branch

```bash
# Create feature branch
git checkout -b feature/warp-categories

# Or bugfix branch
git checkout -b fix/saving-issue
```

**Branch naming:**
- `feature/description` - New features
- `fix/description` - Bug fixes
- `docs/description` - Documentation
- `refactor/description` - Code refactoring

### 3. Make Changes

```bash
# Edit files
# Test thoroughly
# Commit often

git add .
git commit -m "Add warp category system"
```

**Commit messages:**
```bash
# Good
git commit -m "Add warp category feature"
git commit -m "Fix warp saving on server restart"
git commit -m "Update French translation"

# Bad
git commit -m "changes"
git commit -m "fix"
git commit -m "update stuff"
```

### 4. Test Changes

```bash
# Build
mvn clean package

# Test on local server
# Test all affected features
# Test edge cases
```

**Testing checklist:**
- [ ] Code compiles without errors
- [ ] Feature works as intended
- [ ] No new bugs introduced
- [ ] Existing features still work
- [ ] Performance not degraded

### 5. Update Documentation

If your change affects:
- Commands → Update `docs/src/usage/commands.md`
- Config → Update `docs/src/configuration/config.md`
- API → Update `docs/src/developers/api-overview.md`

### 6. Push Changes

```bash
git push origin feature/warp-categories
```

### 7. Create Pull Request

On GitHub:
1. Click "New Pull Request"
2. Select your branch
3. Fill in description

**PR Template:**
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation
- [ ] Refactoring

## Testing
How was this tested?

## Checklist
- [ ] Code follows style guidelines
- [ ] Comments added where needed
- [ ] Documentation updated
- [ ] Tested thoroughly
- [ ] No breaking changes (or documented)

## Screenshots (if applicable)
```

### 8. Review Process

Maintainer will:
- Review code
- Test functionality
- Request changes if needed
- Merge when approved

**Be patient and responsive to feedback!**

## Code Review

### What We Look For

✅ **Good:**
- Clear, readable code
- Proper error handling
- Efficient algorithms
- Well-documented
- Follows guidelines
- Thoroughly tested

❌ **Bad:**
- Magic numbers
- Unclear variable names
- Poor performance
- No error handling
- Undocumented
- Untested

### Responding to Feedback

**Good responses:**
```
"Good point! I'll fix that."
"I updated the code, please review again."
"Can you clarify what you mean here?"
```

**Bad responses:**
```
"It works, why change it?"
"That's how I always do it."
*ignoring feedback*
```

## Development Tips

### Debugging

```java
// Use logging
getLogger().info("Creating warp: " + name);
getLogger().warning("Invalid material: " + material);
getLogger().severe("Failed to save: " + e.getMessage());

// Enable debug mode
if (config.getBoolean("debug")) {
    getLogger().info("[DEBUG] Warp data: " + warp.toString());
}
```

### Testing

```java
// Test edge cases
testWarpCreation("");           // Empty name
testWarpCreation(null);         // Null name
testWarpCreation("a".repeat(100)); // Very long name

// Test concurrency
// Multiple players creating warps simultaneously
```

### Performance

```java
// Good - cache expensive operations
private Map<String, Material> materialCache = new ConcurrentHashMap<>();

public Material getMaterial(String name) {
    return materialCache.computeIfAbsent(name, Material::getMaterial);
}

// Bad - repeated expensive operations
public Material getMaterial(String name) {
    return Material.getMaterial(name);  // Called repeatedly
}
```

## Community

### Code of Conduct

- Be respectful
- Be patient
- Be constructive
- No harassment
- No spam

### Communication

- **GitHub Issues:** Bug reports, feature requests
- **GitHub Discussions:** Questions, ideas, help
- **Discord:** Real-time chat (if available)
- **Email:** Private concerns

### Recognition

Contributors will be:
- Listed in CONTRIBUTORS.md
- Credited in release notes
- Thanked publicly

## Legal

### License

EasyWarp is licensed under [MIT License](https://opensource.org/licenses/MIT).

By contributing, you agree:
- Your contribution will be under MIT License
- You have rights to submit the contribution
- You grant perpetual license to use your contribution

### Copyright

Add yourself to copyright notices:

```java
/**
 * EasyWarp - Flexible teleportation plugin
 *
 * Copyright (c) 2024 Original Author
 * Copyright (c) 2024 Your Name <your.email@example.com>
 *
 * Licensed under MIT License
 */
```

## Questions?

Need help contributing?

- Open a [GitHub Discussion](https://github.com/your-repo/discussions)
- Join [Discord](https://discord.gg/your-server)
- Email: maintainer@example.com

## Thank You!

Every contribution helps make EasyWarp better. Thank you for your time and effort!

---

**Happy Contributing! 🎉**