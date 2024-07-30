package eu.thumus.easywarp;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public final class GUICreator implements Listener
{
    public Inventory inv;
    private final Main main;

    public Inventory[] invs;
    
    public GUICreator(final Main me) {
        this.main = me;
        this.main.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this.main);
        // this.loadGUI();
        this.create(me);
    }


    public void create(final Main me) {
        this.inv = Bukkit.createInventory((InventoryHolder) null, 9, "Menu");
    }
    
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
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
        this.doWhat(p, clickedItem);
    }
    
    public void doWhat(final Player p, final ItemStack item) {
    }
    
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() == this.inv) {
            e.setCancelled(true);
        }
    }
}
