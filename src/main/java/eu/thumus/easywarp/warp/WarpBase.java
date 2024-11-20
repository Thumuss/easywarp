package eu.thumus.easywarp.warp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import eu.thumus.easywarp.utils.ConfigGenerator;

public abstract class WarpBase implements Listener {

    protected Main plugin;
    public HashMap<String, Inventory> inv;
    public HashMap<String, ArrayList<Location>> lcs;
    protected FileConfiguration Fc;
    public File file;
    protected  boolean usingPlayerUID = false;

    public static final String MATERIAL_NAME = "DARK_OAK_BUTTON";

    final private String name;
    final private String strKeyBase;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public WarpBase(final Main plugin, final String strKey, String name) {
        this.create(plugin, strKey, name);
        this.name = name;
        strKeyBase = strKey;
    }

    public void create(Main plugin, String strKey, String name) {
        this.plugin = plugin;
        Fc = plugin.warpConfig;
        file = ConfigGenerator.SF.get("warp");
        lcs = new HashMap<>();
        inv = new HashMap<>();
    }

    public void softCreate(final String strKey) {
        Fc = plugin.warpConfig;
        final ConfigurationSection playerStr = Fc.getConfigurationSection(strKey);
        if (playerStr == null) {
            Fc.createSection(strKey);
            try {
                Fc.save(file);
            } catch (IOException e) {
            }
        }
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
            invs.addItem(createItem(a, key));
            lcss.add(loadLocation(a));
        }
        this.inv.put(strKey, invs);
        this.lcs.put(strKey, lcss);
    }

    private ItemStack[] createItem(MemorySection memory, String key) {
        String materialName = (String) memory.get("item", MATERIAL_NAME);
        if (materialName == null) {
            materialName = MATERIAL_NAME;
        }
        return new ItemStack[]{this.createGuiItem(Material.getMaterial(materialName), String.join(" ", key.split("_")), "§aTe t\u00e9l\u00e9porte \u00e0 " + key)};
    }

    private Location loadLocation(MemorySection memory) {
        String worldName = (String) memory.get("world", "world");
        if (worldName == null) {
            worldName = "world";
        }
        final World wl = Bukkit.getWorld(worldName);
        return new Location(wl, memory.getDouble("x"), memory.getDouble("y"), memory.getDouble("z"), Float.parseFloat((String) memory.get("yaw", "1.0f")), Float.parseFloat((String) memory.get("pitch", "0.0f")));
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
        
        String key = usingPlayerUID ? p.getUniqueId().toString() : strKeyBase ;
        Inventory invs = this.inv.get(key);

        if (usingPlayerUID && e.getClickedInventory() != invs) {
            return;
        }

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        p.teleport((Location) this.lcs.get(key).get(e.getRawSlot()));
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100.0f, 1.0f);
    }

    public void reload(final Main plugin) {
        this.create(plugin, strKeyBase, name);
    }
}
