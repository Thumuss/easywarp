package eu.thumus.easywarp;

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
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class WarpPerso implements Listener
{
    public HashMap<String, Inventory> inv;
    public HashMap<String, ArrayList<Location>> lcs;
    public Main plugin;

    public WarpPerso(final Main plugin) {
        this.create(plugin);
    }

    public void create(final Main plugin) {
        this.plugin = plugin;
        final FileConfiguration Fc = plugin.getConfig();
        this.lcs = new HashMap<>();
        this.inv = new HashMap<>();
        for (final Map.Entry<String, Object> entry : Fc.getConfigurationSection("players").getValues(false).entrySet()) {
            this.softCreate(entry.getKey());
            System.out.println("Les Warps perso de " + String.join(" ", entry.getKey().split("_")) + " sont load");
        }
        System.out.println("Menu warp perso load");
    }

    
    public void softCreate(final String strKey) {
        final FileConfiguration Fc = this.plugin.getConfig();
        final ConfigurationSection players = Fc.getConfigurationSection("players");
        if (players == null) return;
        final ConfigurationSection playerStr = players.getConfigurationSection(strKey);
        if (playerStr == null) return;
        Map<String, Object> values = playerStr.getValues(false);
        double size = values.size();
        double result = Math.ceil(size / 9.0) * 9.0;
        if (result < 9) { result = (double) 9; }
        final ArrayList<Location> lcss = new ArrayList<>();
        Inventory invs = Bukkit.createInventory(null, (int)result, "Warp perso");
        for (final Map.Entry<String, Object> entry : values.entrySet()) {
            final String key = entry.getKey();
            final MemorySection a = (MemorySection) entry.getValue();
            invs.addItem(new ItemStack[] { this.createGuiItem(Material.getMaterial((String)a.get("item", "DARK_OAK_BUTTON")), String.join(" ", key.split("_")), "§aTe t\u00e9l\u00e9porte \u00e0 " + key) });
            final World wl = Bukkit.getWorld((String)a.get("world", "world"));

            if(a.get("v") != null) {
                final Location lc = new Location(wl, a.getDouble("x"), a.getDouble("y"), a.getDouble("z"), Float.parseFloat((String) a.get("yaw", "1.0f")), Float.parseFloat((String) a.get("pitch", "0.0f")));
                lcss.add(lc);
            } else {
                final Location lc = new Location(wl, (double)(int)a.get("x", "0"), (double)(int)a.get("y", "0"), (double)(int)a.get("z", "0"), Float.parseFloat((String)a.get("yaw", "0")), Float.parseFloat((String)a.get("pitch", "0")));
                lcss.add(lc);
            }
        }
        this.inv.put(strKey, invs);
        this.lcs.put(strKey, lcss);
    }
    
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore((List)Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getInventory() != this.inv.get(p.getUniqueId().toString())) {
            return;
        }
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        p.teleport((Location)this.lcs.get(p.getUniqueId().toString()).get(e.getRawSlot()));
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100.0f, 1.0f);
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        final Player p = (Player)e.getWhoClicked();
        if (e.getInventory() == this.inv.get(p.getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

    public void reload(final Main plugin) {
        this.create(plugin);
    }
}
