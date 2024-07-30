package eu.thumus.easywarp;

import org.bukkit.*;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class WarpW implements Listener
{
    public Inventory inv;
    public ArrayList<Location> lcs;

    public WarpW(final Main plugin) {
        this.create(plugin);
    }

    public void create(final Main plugin) {
        final double size = plugin.getConfig().getConfigurationSection("warp").getValues(false).size();
        double result = Math.ceil(size / 9.0) * 9.0;
        if (result < 9) { result = (double) 9; }
        this.inv = Bukkit.createInventory((InventoryHolder)null, (int)result, "Warp");
        final FileConfiguration Fc = plugin.getConfig();
        this.lcs = new ArrayList<Location>();
        for (final Map.Entry<String, Object> entry : Fc.getConfigurationSection("warp").getValues(false).entrySet()) {
            final String key = entry.getKey();
            final MemorySection a = (MemorySection) entry.getValue();
            this.inv.addItem(this.createGuiItem(Material.getMaterial((String)a.get("item")), String.join(" ", key.split("_")), "§aTe t\u00e9l\u00e9porte \u00e0 " + key));
            final World wl = Bukkit.getWorld((String)a.get("world", "world"));
            final Location lc = new Location(wl, (double)(int)a.get("x", "0"), (double)(int)a.get("y", "0"), (double)(int)a.get("z", "0"), Float.parseFloat((String)a.get("yaw", "0")), Float.parseFloat((String)a.get("pitch", "0")));
            this.lcs.add(lc);
            System.out.println("Le Warp " + String.join(" ", key.split("_")) + " est load");

        }
        System.out.println("Menu warp load");
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
        if (e.getInventory() != this.inv) {
            return;
        }
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        final Player p = (Player)e.getWhoClicked();
        p.teleport((Location)this.lcs.get(e.getRawSlot()));
        p.playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 100.0f, 1.0f);
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == this.inv) {
            e.setCancelled(true);
        }
    }

    public void reload(final Main plugin) {
        this.create(plugin);
    }
}
