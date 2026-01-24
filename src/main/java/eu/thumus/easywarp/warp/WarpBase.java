package eu.thumus.easywarp.warp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.thumus.easywarp.Main;
import eu.thumus.easywarp.api.events.WarpTeleportEvent;
import eu.thumus.easywarp.utils.ConfigGenerator;

public abstract class WarpBase implements Listener {

    protected Main plugin;
    public HashMap<String, Inventory> inv;
    public HashMap<String, ArrayList<Location>> lcs;
    protected FileConfiguration Fc;
    public File file;
    protected boolean usingPlayerUID = false;

    public static String MATERIAL_NAME;

    final private String name;
    final private String strKey;
    private String filename;
    
    // Performance caches
    private final Map<String, Material> materialCache = new ConcurrentHashMap<>();
    private final Map<String, World> worldCache = new ConcurrentHashMap<>();
    private final Map<String, Long> lastModified = new ConcurrentHashMap<>();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public WarpBase(final Main plugin, final String strKey, String name, boolean upuid, String filename) {
        this.name = name;
        this.strKey = strKey;
        this.filename = filename;
        this.usingPlayerUID = upuid;
        this.create(plugin, strKey, name, filename);
    }

    public void create(Main plugin, String strKey, String name, String filename) {
        this.plugin = plugin;
        this.filename = filename;
        Fc = ConfigGenerator.getOrCreateFC(filename);
        file = ConfigGenerator.getOrCreateFileToSave(filename);
        lcs = new HashMap<>();
        inv = new HashMap<>();
    }

    protected ConfigurationSection getConfigurationSectionIfNotExist(String strKey) {
        final ConfigurationSection section = Fc.getConfigurationSection(strKey);
        if (section != null) {
            return section;
        }
        Fc.createSection(strKey);
        try {
            ConfigGenerator.save(filename);
        } catch (IOException e) {
        }
        return Fc.getConfigurationSection(strKey);
    }

    public void softCreate(final String strKey) {
        // Check if we need to reload based on file modification time
        long currentModTime = file.lastModified();
        Long cachedModTime = lastModified.get(strKey);
        
        if (cachedModTime != null && cachedModTime == currentModTime && inv.containsKey(strKey)) {
            // No need to reload, inventory is up to date
            return;
        }
        
        lastModified.put(strKey, currentModTime);
        
        if (usingPlayerUID) softCreateUUID(strKey);
        else softCreateNoUUID(strKey);
    }

    public void softCreateUUID(final String strKey) {
        for (final Map.Entry<String, Object> entry : Fc.getConfigurationSection(strKey).getValues(false).entrySet()) {
            softCreate(entry.getKey());
        }
    }
    public void softCreateNoUUID(final String strKey) {
        final ConfigurationSection playerStr = getConfigurationSectionIfNotExist(strKey);
        Map<String, Object> values = playerStr == null ? new HashMap<>() : playerStr.getValues(false);
        double size = values.size();
        double result = Math.ceil(size / 9.0) * 9.0;
        if (result < 9) {
            result = (double) 9;
        }
        createInv((int) result, values, name, strKey);
    }

    
    protected void createInv(int size, Map<String, Object> values, String name, String strKey) {
        final ArrayList<Location> lcss = new ArrayList<>();
        Inventory invs = Bukkit.createInventory(null, size, name);
        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            final String key = entry.getKey();
            final MemorySection a = (MemorySection) entry.getValue();
            
            Location location = loadLocation(a);
            if (location == null) {
                plugin.console.warning("Skipping warp '" + key + "' due to invalid location");
                continue;
            }
            
            invs.addItem(createItem(a, key));
            lcss.add(location);
        }
        this.inv.put(strKey, invs);
        this.lcs.put(strKey, lcss);
    }

    private ItemStack[] createItem(MemorySection memory, String key) {
        String materialName = (String) memory.get("item", MATERIAL_NAME);
        if (materialName == null) {
            materialName = MATERIAL_NAME;
        }
        
        // Use cached material or lookup and cache
        Material material = materialCache.computeIfAbsent(materialName, name -> {
            Material mat = Material.getMaterial(name);
            if (mat == null) {
                plugin.console.warning("Invalid material '" + name + "', using default");
                return Material.getMaterial(MATERIAL_NAME);
            }
            return mat;
        });
        
        return new ItemStack[]{this.createGuiItem(material, String.join(" ", key.split("_")), "§aTe téléporte à " + key)};
    }

    private Location loadLocation(MemorySection memory) {
        String worldName = (String) memory.get("world", "world");
        if (worldName == null) {
            worldName = "world";
        }
        
        // Use cached world or lookup and cache
        World wl = worldCache.computeIfAbsent(worldName, name -> {
            World world = Bukkit.getWorld(name);
            if (world == null) {
                plugin.console.warning("World '" + name + "' not found for warp location. Skipping warp.");
            }
            return world;
        });
        
        // Validate world exists
        if (wl == null) {
            return null;
        }
        
        try {
            double x = memory.getDouble("x");
            double y = memory.getDouble("y");
            double z = memory.getDouble("z");
            float yaw = Float.parseFloat((String) memory.get("yaw", "1.0f"));
            float pitch = Float.parseFloat((String) memory.get("pitch", "0.0f"));
            
            return new Location(wl, x, y, z, yaw, pitch);
        } catch (NumberFormatException | NullPointerException e) {
            plugin.console.warning("Invalid warp location data: " + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return null;
        }
        meta.setDisplayName(name);
        meta.setLore((List) Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();

        String key = usingPlayerUID ? p.getUniqueId().toString() : strKey;
        Inventory invs = this.inv.get(key);

        if (usingPlayerUID && e.getClickedInventory() != invs) {
            return;
        }

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        // Validate slot index
        int slot = e.getRawSlot();
        ArrayList<Location> locations = this.lcs.get(key);
        
        if (locations == null || slot < 0 || slot >= locations.size()) {
            plugin.console.warning("Invalid warp slot clicked: " + slot);
            p.sendMessage("§cInvalid warp. Please contact an administrator.");
            return;
        }
        
        Location targetLocation = locations.get(slot);
        if (targetLocation == null || targetLocation.getWorld() == null) {
            plugin.console.warning("Null or invalid location for warp at slot: " + slot);
            p.sendMessage("§cThis warp is invalid. Please contact an administrator.");
            return;
        }
        
        // Get warp name from item display name
        String warpName = clickedItem.getItemMeta() != null && clickedItem.getItemMeta().getDisplayName() != null
            ? clickedItem.getItemMeta().getDisplayName()
            : "unknown";
        
        // Check cooldown
        boolean cooldownEnabled = plugin.getConfig().getBoolean("cooldown.enabled", true);
        String bypassPermission = plugin.getConfig().getString("cooldown.bypass-permission", "easywarp.cooldown.bypass");
        
        if (cooldownEnabled && !p.hasPermission(bypassPermission)) {
            if (plugin.getCooldownManager().hasCooldown(p, strKey)) {
                int remaining = plugin.getCooldownManager().getRemainingCooldown(p, strKey);
                p.sendMessage("§cYou must wait " + remaining + " second(s) before teleporting again.");
                return;
            }
        }
        
        // Fire teleport event
        WarpTeleportEvent teleportEvent = new WarpTeleportEvent(p, warpName, targetLocation, strKey);
        Bukkit.getServer().getPluginManager().callEvent(teleportEvent);
        
        if (teleportEvent.isCancelled()) {
            return;
        }
        
        // Check for teleportation delay
        int delay = plugin.getConfig().getInt("teleportation.delay", 0);
        
        if (delay > 0) {
            Location startLocation = p.getLocation().clone();
            boolean cancelOnMove = plugin.getConfig().getBoolean("teleportation.cancel-on-move", false);
            
            p.sendMessage("§eTeleporting in " + delay + " second(s)...");
            
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                // Check if player moved (if cancel-on-move is enabled)
                if (cancelOnMove && startLocation.distance(p.getLocation()) > 0.5) {
                    p.sendMessage("§cTeleportation cancelled! You moved.");
                    return;
                }
                
                performTeleport(p, targetLocation, strKey, cooldownEnabled, bypassPermission);
            }, delay * 20L); // Convert seconds to ticks
        } else {
            // Instant teleportation
            performTeleport(p, targetLocation, strKey, cooldownEnabled, bypassPermission);
        }
    }
    
    private void performTeleport(Player p, Location targetLocation, String warpType, boolean cooldownEnabled, String bypassPermission) {
        // Teleport with error handling
        try {
            if (p.teleport(targetLocation)) {
                p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100.0f, 1.0f);
                
                // Set cooldown after successful teleportation
                if (cooldownEnabled && !p.hasPermission(bypassPermission)) {
                    plugin.getCooldownManager().setCooldown(p, warpType);
                }
            } else {
                p.sendMessage("§cTeleportation failed. Please try again.");
            }
        } catch (Exception ex) {
            plugin.console.warning("Failed to teleport player " + p.getName() + ": " + ex.getMessage());
            p.sendMessage("§cTeleportation failed. Please contact an administrator.");
        }
    }

    public void reload(final Main plugin) {
        // Clear caches on reload
        materialCache.clear();
        worldCache.clear();
        lastModified.clear();
        
        this.create(plugin, strKey, name, filename);
    }
    
    /**
     * Clear performance caches
     */
    public void clearCaches() {
        materialCache.clear();
        worldCache.clear();
        lastModified.clear();
    }
    
    /**
     * Get the FileConfiguration for this warp
     * @return The FileConfiguration
     */
    public FileConfiguration getFileConfiguration() {
        return Fc;
    }
}
