package eu.thumus.easywarp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.thumus.easywarp.utils.ConfigGenerator;
import eu.thumus.easywarp.warp.WarpPerso;
import eu.thumus.easywarp.warp.WarpW;

public final class Main extends JavaPlugin {

    public WarpW wr2;
    public WarpPerso wr3;
    private Commands cmds;
    @SuppressWarnings("NonConstantLogger")
    public Logger console;
    public FileConfiguration warpConfig; 

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        console = getLogger();
        warpConfig = ConfigGenerator.createConfig(this, "warp.yml", "warp");
        console.log(Level.INFO, String.format("warpConfig %b", warpConfig == null));
        this.wr2 = new WarpW(this);
        this.wr3 = new WarpPerso(this);
        cmds = new Commands(this);
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(this.wr2, this);
        pm.registerEvents(this.wr3, this);
        console.log(Level.INFO, "Le plugin s'est allumé");
    }

    @Override
    public void onDisable() {
        if (console != null) {
            console.log(Level.INFO, "Goodbye!!");
        }
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        try {
            return cmds.onCommand(sender, command, label, args);
        } catch (IOException ex) {
            return false;
        }
    }
    /*
    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't execute this command ! You aren't a Player !");
            return false;
        }
        final Player pl = (Player) sender;
        if (command.getName().equalsIgnoreCase("warp")) {
            if (args.length != 0) {
                if (args[0].contains("set")) {
                    if (args.length < 2) {
                        return false;
                    }
                    if (!pl.isOp()) {
                        pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                        return false;
                    }
                    String item;
                    if (args.length > 2) {
                        item = args[2].toUpperCase();
                        if (Material.getMaterial(item) == null) {
                            item = "DARK_OAK_BUTTON";
                        }
                    } else {
                        item = "DARK_OAK_BUTTON";
                    }
                    final Location ls = pl.getLocation();
                    final String path = "warp." + args[1];
                    this.getConfig().set((path) + ".x", (int) ls.getX());
                    this.getConfig().set((path) + ".y", (int) ls.getY());
                    this.getConfig().set((path) + ".z", (int) ls.getZ());
                    this.getConfig().set((path) + ".yaw", (String.valueOf(String.valueOf(ls.getYaw())) + "f"));
                    this.getConfig().set((path) + ".pitch", (String.valueOf(String.valueOf(ls.getPitch())) + "f"));
                    this.getConfig().set((path) + ".item", item);
                    this.getConfig().set((path) + ".world", pl.getWorld().getName());
                    this.saveConfig();
                    this.wr2.create(this);
                    return true;
                } else if (args[0].contains("reload")) {
                    if (!pl.isOp()) {
                        pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                        return false;
                    }
                    this.wr2.create(this);
                } else if (args[0].contains("delete")) {
                    if (!pl.isOp()) {
                        pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                        return false;
                    }
                    if (args.length < 2) {
                        return false;
                    }
                    final String path = "warp." + args[1];
                    this.getConfig().set(String.valueOf(path), null);
                    this.saveConfig();
                    this.wr2.create(this);
                    pl.sendMessage("<Server> §4Warp supprimé");
                    return true;
                }
            }
            pl.openInventory(this.wr2.inv);
            return true;
        } else if (command.getName().equalsIgnoreCase("pwarp")) {

            if (args.length != 0) {
                if (args[0].contains("set")) {
                    if (args.length < 2) {
                        return false;
                    }
                    String item;
                    if (args.length > 2) {
                        item = args[2].toUpperCase();
                        if (Material.getMaterial(item) == null) {
                            item = "DARK_OAK_BUTTON";
                        }
                    } else {
                        item = "DARK_OAK_BUTTON";
                    }
                    final Location ls = pl.getLocation();
                    final String path = "players." + pl.getUniqueId().toString() + "." + args[1];
                    getConfig().set((path) + ".x", (double) ls.getX());
                    getConfig().set((path) + ".v", (int) 2);
                    getConfig().set((path) + ".y", (double) ls.getY());
                    getConfig().set((path) + ".z", (double) ls.getZ());
                    getConfig().set((path) + ".yaw", (String.valueOf(String.valueOf(ls.getYaw())) + "f"));
                    getConfig().set((path) + ".pitch", (String.valueOf(String.valueOf(ls.getPitch())) + "f"));
                    getConfig().set((path) + ".item", item);
                    getConfig().set((path) + ".world", pl.getWorld().getName());
                    saveConfig();
                    wr3.softCreate(pl.getUniqueId().toString());
                    pl.sendMessage("Server → §4Warp ajoutée !");
                    return true;
                } else if (args[0].contains("reload")) {
                    this.wr3.create(this);
                    pl.sendMessage("Server → §4Warp reload");
                    return true;
                } else if (args[0].contains("delete")) {
                    if (args.length < 2) {
                        return false;
                    }
                    final String path = "players." + pl.getUniqueId().toString() + "." + args[1];
                    this.getConfig().set(String.valueOf(path), null);
                    this.saveConfig();
                    this.wr3.create(this);
                    pl.sendMessage("Server → §4Warp perso supprimé");
                    return true;
                } else if (args[0].contains("open")) {
                    if (!pl.isOp()) {
                        pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                        return false;
                    }
                    Inventory inv = this.wr3.inv.get(this.getServer().getPlayer(args[1]).getUniqueId().toString());
                    if (inv == null) {
                        pl.sendMessage("Server → §4Ce joueur n'existe pas !");
                        return false;
                    }
                    pl.openInventory(inv);
                }
            }

            if (this.wr3.inv.containsKey(pl.getUniqueId().toString())) {
                pl.openInventory((Inventory) this.wr3.inv.get(pl.getUniqueId().toString()));
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("ec")) {
            pl.openInventory(pl.getEnderChest());
            return true;
        }
        else if (command.getName().equalsIgnoreCase("wb")) {
            pl.openWorkbench(null, true);
            return true;
        } else if (command.getName().equalsIgnoreCase("spec")) {
            if (!pl.isOp()) {
                pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                return false;
            }
            pl.sendMessage("Server → Set pickup to " + !pl.getCanPickupItems());
            pl.setCanPickupItems(!pl.getCanPickupItems());
            return true;
        } else if (command.getName().equalsIgnoreCase("rename")) {
            if (args.length < 1) {
                return false;
            }
            if (!pl.isOp()) {
                pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                return false;
            }
            int num = pl.getInventory().getHeldItemSlot();
            ItemStack item = pl.getInventory().getItem(num);
            if (item == null) {
                return false;
            }
            ItemMeta a = item.getItemMeta();
            a.setDisplayName(String.join(" ", args));
            item.setItemMeta(a);
            return true;
        } else if (command.getName().equalsIgnoreCase("setlore")) {
            if (args.length < 1) {
                return false;
            }
            if (!pl.isOp()) {
                pl.sendMessage("Server → §4Tu as besoin d'être admin !");
                return false;
            }
            int num = pl.getInventory().getHeldItemSlot();
            ItemStack item = pl.getInventory().getItem(num);
            if (item == null) {
                pl.sendMessage("Server → §4samarchpa");
                return false;
            }
            ItemMeta a = item.getItemMeta();
            a.setLore(Arrays.asList(String.join(" ", args).split(";")));
            item.setItemMeta(a);
            return true;
        }
        return false;*/
}
