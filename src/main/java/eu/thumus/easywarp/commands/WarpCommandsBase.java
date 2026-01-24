package eu.thumus.easywarp.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import eu.thumus.easywarp.Main;
import eu.thumus.easywarp.api.events.WarpCreateEvent;
import eu.thumus.easywarp.api.events.WarpDeleteEvent;
import eu.thumus.easywarp.data.config.PermissionConfig;
import eu.thumus.easywarp.data.config.WarpConfig;
import eu.thumus.easywarp.utils.ConfigGenerator;
import eu.thumus.easywarp.utils.MessageManager;
import eu.thumus.easywarp.warp.WarpBase;

public class WarpCommandsBase {

    final private String prefix;
    final private Main main;
    final private FileConfiguration customConfig;
    private WarpBase wb;
    final private String filename;
    private PermissionConfig permissionConfig;
    private MessageManager messageManager;
    private String warpType;

    public WarpCommandsBase(Main mn, WarpBase wpb, WarpConfig warpConfig, String fn) {
        main = mn;
        wb = wpb;
        this.permissionConfig = warpConfig.getPermissionConfig();
        this.messageManager = mn.getMessageManager();
        this.warpType = warpConfig.getName();
        prefix = mn.getConfig().getString("prefix", "Server -> §4");
        filename = fn;
        customConfig = ConfigGenerator.getOrCreateFC(filename);
    }

    public boolean warpCmd(final Player pl, final Command command, final String label, String[] args, String key) throws IOException {
        if (args.length == 0) {
            return warpUseCmd(pl, new String[0], key);
        }
        final String arg0 = args[0];
        final String[] newArgs = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);

        return switch (arg0) {
            case "delete" ->
                warpDeleteCmd(pl, newArgs, key);
            case "edit" ->
                warpEditCmd(pl, newArgs, key);
            case "set" ->
                warpSetCmd(pl, newArgs, key);
            case "reload" ->
                warpReloadCmd(pl, newArgs, key);
            case "use" ->
                warpUseCmd(pl, newArgs, key);
            case "list" ->
                warpListCmd(pl, newArgs, key);
            case "info" ->
                warpInfoCmd(pl, newArgs, key);
            case "search" ->
                warpSearchCmd(pl, newArgs, key);
            default ->
                warpUseCmd(pl, newArgs, key);
        };
    }

    public boolean warpUseCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!permissionConfig.isEnabled("use")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String usePermission = permissionConfig.getPermission("use");
        if (!permissionConfig.isDefaultFor("use") && !pl.hasPermission(usePermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.use");
            return false;
        }
        Inventory invs = wb.inv.get(key);
        if (invs == null) {
            try {
                customConfig.createSection(key);
                ConfigGenerator.save(filename);
                wb.softCreate(key);
                invs = wb.inv.get(key);
            } catch (IOException e) {
                main.console.log(Level.SEVERE, "Failed to create warp inventory for key: " + key, e);
                messageManager.sendMessageWithPrefix(pl, "error.config-error", e.getMessage());
                return false;
            }
        }
        pl.openInventory(invs);
        if (messageManager.hasMessage("success.use")) {
            messageManager.sendMessageWithPrefix(pl, "success.use");
        }
        return true;
    }

    public boolean warpDeleteCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!permissionConfig.isEnabled("delete")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String deletePermission = permissionConfig.getPermission("delete");
        if (!permissionConfig.isDefaultFor("delete") && !pl.hasPermission(deletePermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.delete");
            return false;
        }
        
        // Validation: check if args provided
        if (args.length == 0) {
            messageManager.sendMessageWithPrefix(pl, "command.no-args");
            return false;
        }
        
        final String warpName = args[0];
        final String path = key + "." + warpName;
        
        // Check if warp exists
        if (!customConfig.contains(path)) {
            messageManager.sendMessageWithPrefix(pl, "error.warp-not-found", warpName);
            return false;
        }
        
        // Fire event
        WarpDeleteEvent event = new WarpDeleteEvent(pl, warpName, warpType);
        Bukkit.getPluginManager().callEvent(event);
        
        if (event.isCancelled()) {
            return false;
        }
        
        // Perform async file I/O
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                customConfig.set(String.valueOf(path), null);
                ConfigGenerator.save(filename);
                
                // Reload inventory on main thread
                Bukkit.getScheduler().runTask(main, () -> {
                    wb.softCreate(key);
                    messageManager.sendMessageWithPrefix(pl, "success.delete", warpName);
                });
            } catch (IOException e) {
                main.console.log(Level.SEVERE, "Failed to delete warp: " + warpName, e);
                Bukkit.getScheduler().runTask(main, () -> {
                    messageManager.sendMessageWithPrefix(pl, "error.config-error", e.getMessage());
                });
            }
        });
        
        return true;
    }

    public void sendMessage(Player pl, String msg) {
        pl.sendMessage(prefix + msg);
    }

    public boolean warpSetCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!permissionConfig.isEnabled("set")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String setPermission = permissionConfig.getPermission("set");
        if (!permissionConfig.isDefaultFor("set") && !pl.hasPermission(setPermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.set");
            return false;
        }
        
        // Validation: check if args provided
        if (args.length == 0) {
            messageManager.sendMessageWithPrefix(pl, "command.no-args");
            return false;
        }
        
        final String warpName = args[0];
        final String item;
        if (args.length > 1) {
            String tempItem = args[1].toUpperCase();
            if (Material.getMaterial(tempItem) == null) {
                messageManager.sendMessageWithPrefix(pl, "command.invalid-material", args[1]);
                item = WarpBase.MATERIAL_NAME;
            } else {
                item = tempItem;
            }
        } else {
            item = WarpBase.MATERIAL_NAME;
        }
        
        final Location ls = pl.getLocation();
        
        // Validate world
        if (ls.getWorld() == null) {
            messageManager.sendMessageWithPrefix(pl, "error.invalid-location");
            return false;
        }
        
        // Fire event
        Material iconMaterial = Material.getMaterial(item);
        WarpCreateEvent event = new WarpCreateEvent(pl, warpName, ls, iconMaterial, warpType);
        Bukkit.getPluginManager().callEvent(event);
        
        if (event.isCancelled()) {
            return false;
        }
        
        final String path = key + "." + warpName;
        
        // Perform async file I/O
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                customConfig.set((path) + ".x", (double) ls.getX());
                customConfig.set((path) + ".v", (int) 2);
                customConfig.set((path) + ".y", (double) ls.getY());
                customConfig.set((path) + ".z", (double) ls.getZ());
                customConfig.set((path) + ".yaw", (String.valueOf(String.valueOf(ls.getYaw())) + "f"));
                customConfig.set((path) + ".pitch", (String.valueOf(String.valueOf(ls.getPitch())) + "f"));
                customConfig.set((path) + ".item", item);
                customConfig.set((path) + ".world", ls.getWorld().getName());
                ConfigGenerator.save(filename);
                
                // Reload inventory on main thread
                Bukkit.getScheduler().runTask(main, () -> {
                    wb.softCreate(key);
                    messageManager.sendMessageWithPrefix(pl, "success.set", warpName);
                });
            } catch (IOException e) {
                main.console.log(Level.SEVERE, "Failed to save warp: " + warpName, e);
                Bukkit.getScheduler().runTask(main, () -> {
                    messageManager.sendMessageWithPrefix(pl, "error.config-error", e.getMessage());
                });
            }
        });
        
        return true;
    }

    public boolean warpReloadCmd(final Player pl, final String[] args, String key) {
        if (!permissionConfig.isEnabled("reload")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String reloadPermission = permissionConfig.getPermission("reload");
        if (!permissionConfig.isDefaultFor("reload") && !pl.hasPermission(reloadPermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.reload");
            return false;
        }
        wb.reload(main);
        messageManager.sendMessageWithPrefix(pl, "success.reload");
        return true;
    }

    public boolean warpEditCmd(final Player pl, final String[] args, String key) {
        if (!permissionConfig.isEnabled("edit")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String editPermission = permissionConfig.getPermission("edit");
        if (!permissionConfig.isDefaultFor("edit") && !pl.hasPermission(editPermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.edit");
            return false;
        }
        
        // Validation: check args length (need warp name and material)
        if (args.length < 2) {
            messageManager.sendMessageWithPrefix(pl, "command.no-args");
            return false;
        }
        
        final String warpName = args[0];
        final String materialName = args[1].toUpperCase();
        final String path = key + "." + warpName;
        
        // Validation: check if warp exists
        if (!customConfig.contains(path)) {
            messageManager.sendMessageWithPrefix(pl, "error.warp-not-found", warpName);
            return false;
        }
        
        // Validation: check if material is valid
        Material material;
        try {
            material = Material.valueOf(materialName);
        } catch (IllegalArgumentException e) {
            messageManager.sendMessageWithPrefix(pl, "error.invalid-material", materialName);
            return false;
        }
        
        // Update material in config
        customConfig.set(path + ".material", material.name());
        
        // Perform async file I/O
        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            try {
                ConfigGenerator.save(filename);
                
                // Reload inventory on main thread
                Bukkit.getScheduler().runTask(main, () -> {
                    wb.softCreate(key);
                    messageManager.sendMessageWithPrefix(pl, "success.edit", warpName, material.name());
                });
            } catch (IOException e) {
                main.console.log(Level.SEVERE, "Failed to save config after editing warp: " + warpName, e);
                Bukkit.getScheduler().runTask(main, () -> {
                    messageManager.sendMessageWithPrefix(pl, "error.config-error", e.getMessage());
                });
            }
        });
        
        return true;
    }
    
    public boolean warpListCmd(final Player pl, final String[] args, String key) {
        if (!permissionConfig.isEnabled("use")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String usePermission = permissionConfig.getPermission("use");
        if (!permissionConfig.isDefaultFor("use") && !pl.hasPermission(usePermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.use");
            return false;
        }
        
        ConfigurationSection section = customConfig.getConfigurationSection(key);
        if (section == null || section.getKeys(false).isEmpty()) {
            messageManager.sendMessageWithPrefix(pl, "info.no-warps");
            return true;
        }
        
        List<String> warpNames = new ArrayList<>(section.getKeys(false));
        warpNames.sort(String.CASE_INSENSITIVE_ORDER);
        
        // Pagination
        int page = 1;
        int warpsPerPage = 10;
        
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                messageManager.sendMessageWithPrefix(pl, "error.invalid-number", args[0]);
                return false;
            }
        }
        
        int totalPages = (int) Math.ceil((double) warpNames.size() / warpsPerPage);
        if (page > totalPages) page = totalPages;
        
        int startIndex = (page - 1) * warpsPerPage;
        int endIndex = Math.min(startIndex + warpsPerPage, warpNames.size());
        
        // Send header
        pl.sendMessage("§6§m----§r §eWarps List §7(Page " + page + "/" + totalPages + ") §6§m----");
        
        // Send warp list
        for (int i = startIndex; i < endIndex; i++) {
            String warpName = warpNames.get(i);
            String material = section.getString(warpName + ".item", "?");
            String world = section.getString(warpName + ".world", "?");
            pl.sendMessage("§a• §7" + warpName + " §8[§e" + material + "§8] §8(§b" + world + "§8)");
        }
        
        // Send footer with navigation
        if (totalPages > 1) {
            String prevButton = page > 1 ? "§a[<< Prev]" : "§8[<< Prev]";
            String nextButton = page < totalPages ? "§a[Next >>]" : "§8[Next >>]";
            pl.sendMessage("§7" + prevButton + " §eTotal: §7" + warpNames.size() + " warps §7" + nextButton);
        }
        
        return true;
    }
    
    public boolean warpInfoCmd(final Player pl, final String[] args, String key) {
        if (!permissionConfig.isEnabled("use")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String usePermission = permissionConfig.getPermission("use");
        if (!permissionConfig.isDefaultFor("use") && !pl.hasPermission(usePermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.use");
            return false;
        }
        
        if (args.length == 0) {
            messageManager.sendMessageWithPrefix(pl, "command.no-args");
            return false;
        }
        
        String warpName = args[0];
        String path = key + "." + warpName;
        
        if (!customConfig.contains(path)) {
            messageManager.sendMessageWithPrefix(pl, "error.warp-not-found", warpName);
            return false;
        }
        
        // Get warp details
        double x = customConfig.getDouble(path + ".x", 0);
        double y = customConfig.getDouble(path + ".y", 0);
        double z = customConfig.getDouble(path + ".z", 0);
        String world = customConfig.getString(path + ".world", "?");
        String material = customConfig.getString(path + ".item", "?");
        
        pl.sendMessage("§6§m--------§r §eWarp Info §6§m--------");
        pl.sendMessage("§7Name: §a" + warpName);
        pl.sendMessage("§7World: §b" + world);
        pl.sendMessage("§7Location: §ex=" + String.format("%.1f", x) + " §ey=" + String.format("%.1f", y) + " §ez=" + String.format("%.1f", z));
        pl.sendMessage("§7Icon: §6" + material);
        
        return true;
    }
    
    public boolean warpSearchCmd(final Player pl, final String[] args, String key) {
        if (!permissionConfig.isEnabled("use")) {
            messageManager.sendMessageWithPrefix(pl, "command.disabled");
            return false;
        }
        
        String usePermission = permissionConfig.getPermission("use");
        if (!permissionConfig.isDefaultFor("use") && !pl.hasPermission(usePermission)) {
            messageManager.sendMessageWithPrefix(pl, "command.use");
            return false;
        }
        
        if (args.length == 0) {
            messageManager.sendMessageWithPrefix(pl, "command.no-args");
            return false;
        }
        
        String searchTerm = args[0].toLowerCase();
        
        ConfigurationSection section = customConfig.getConfigurationSection(key);
        if (section == null || section.getKeys(false).isEmpty()) {
            messageManager.sendMessageWithPrefix(pl, "info.no-warps");
            return true;
        }
        
        List<String> matchingWarps = new ArrayList<>();
        for (String warpName : section.getKeys(false)) {
            if (warpName.toLowerCase().contains(searchTerm)) {
                matchingWarps.add(warpName);
            }
        }
        
        if (matchingWarps.isEmpty()) {
            messageManager.sendMessageWithPrefix(pl, "info.no-results", searchTerm);
            return true;
        }
        
        matchingWarps.sort(String.CASE_INSENSITIVE_ORDER);
        
        pl.sendMessage("§6§m----§r §eSearch Results for '§7" + searchTerm + "§e' §6§m----");
        pl.sendMessage("§7Found §a" + matchingWarps.size() + "§7 warp(s):");
        
        for (String warpName : matchingWarps) {
            String material = section.getString(warpName + ".item", "?");
            String world = section.getString(warpName + ".world", "?");
            pl.sendMessage("§a• §7" + warpName + " §8[§e" + material + "§8] §8(§b" + world + "§8)");
        }
        
        return true;
    }

    public WarpBase getWb() {
        return wb;
    }

    public void setWb(WarpBase wb) {
        this.wb = wb;
    }

}
