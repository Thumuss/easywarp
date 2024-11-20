package eu.thumus.easywarp.warp;

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
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.thumus.easywarp.Main;

public abstract class WarpBase implements Listener {

    protected Main plugin;
    protected HashMap<String, Inventory> inv;
    protected HashMap<String, ArrayList<Location>> lcs;
    protected FileConfiguration Fc;

    public static final String MATERIAL_NAME = "DARK_OAK_BUTTON";

    public WarpBase(final Main plugin) {
        this.create(plugin);
    }

    public void create(Main plugin) {
        this.plugin = plugin;
        Fc = plugin.getConfig();
        lcs = new HashMap<>();
        inv = new HashMap<>();
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
        // Check if player is not the holder and there are no blocks that can hold this inv
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory() != this.inv.get(p.getUniqueId().toString())) {
            return;
        }
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        p.teleport((Location) this.lcs.get(p.getUniqueId().toString()).get(e.getRawSlot()));
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100.0f, 1.0f);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getInventory() == this.inv.get(p.getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

    public void reload(final Main plugin) {
        this.create(plugin);
    }
}
