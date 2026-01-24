package eu.thumus.easywarp;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.data.config.Config;
import eu.thumus.easywarp.data.config.PermissionConfig;
import eu.thumus.easywarp.data.config.WarpConfig;
import eu.thumus.easywarp.warp.WarpUser;
import eu.thumus.easywarp.warp.WarpWorldWide;

public class Commands {

    public HashMap<String, WarpCommandsBase> WCMap = new HashMap<>();

    public HashMap<String, WarpCommandsBase> getWCMap() {
        return WCMap;
    }

    public void setWCMap(HashMap<String, WarpCommandsBase> WCMap) {
        this.WCMap = WCMap;
    }

    public Commands(Main main) {
        for (Map.Entry<String, WarpConfig> warpConfig : Config.getWarps().entrySet()) {
            WarpConfig w = warpConfig.getValue();
            String key = warpConfig.getKey();
            if (w.isUsingPlayerUID()) {
                WCMap.put(key, new WarpCommandsBase(main, new WarpUser(main, key, w.getDisplay(), w.getDisplay()), w, w.getFilename()));
            } else {
                WCMap.put(key, new WarpCommandsBase(main, new WarpWorldWide(main, key, w.getDisplay(), w.getDisplay()), w, w.getFilename()));
            }
        }
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player pl = (Player) sender;
        String uid = pl.getUniqueId().toString();
        String commandName = command.getName().toLowerCase();
        
        // Find the warp configuration for this command
        WarpConfig wp = Config.getWarpByCommand(commandName);
        if (wp == null) {
            return false;
        }
        
        WarpCommandsBase wpcb = WCMap.get(wp.getName());
        if (wpcb == null) {
            return false;
        }
        
        // Determine the key based on whether it uses player UID
        String key = wp.isUsingPlayerUID() ? wp.getName() + "." + uid : wp.getName();
        
        return wpcb.warpCmd(pl, command, label, args, key);
    }

    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        
        final Player pl = (Player) sender;
        String commandName = command.getName().toLowerCase();
        
        // Find the warp configuration for this command
        WarpConfig wp = Config.getWarpByCommand(commandName);
        if (wp == null) {
            return null;
        }
        
        WarpCommandsBase wpcb = WCMap.get(wp.getName());
        if (wpcb == null) {
            return null;
        }
        
        List<String> suggestions = new ArrayList<>();
        
        // First argument: suggest actions
        if (args.length == 1) {
            List<String> actions = new ArrayList<>();
            PermissionConfig permConfig = wp.getPermissionConfig();
            
            if (permConfig.isEnabled("use")) actions.add("use");
            if (permConfig.isEnabled("set")) actions.add("set");
            if (permConfig.isEnabled("delete")) actions.add("delete");
            if (permConfig.isEnabled("edit")) actions.add("edit");
            if (permConfig.isEnabled("reload")) actions.add("reload");
            
            String input = args[0].toLowerCase();
            suggestions = actions.stream()
                .filter(action -> action.startsWith(input))
                .collect(Collectors.toList());
        }
        // Second argument: suggest warp names or materials depending on action
        else if (args.length == 2) {
            String action = args[0].toLowerCase();
            String input = args[1].toLowerCase();
            
            // For set command, suggest material names
            if (action.equals("set")) {
                suggestions = Arrays.stream(Material.values())
                    .filter(Material::isItem)
                    .map(mat -> mat.name().toLowerCase())
                    .filter(name -> name.contains(input))
                    .limit(20) // Limit to first 20 matches
                    .collect(Collectors.toList());
            }
            // For delete and edit, suggest existing warp names
            else if (action.equals("delete") || action.equals("edit")) {
                String uid = pl.getUniqueId().toString();
                String key = wp.isUsingPlayerUID() ? wp.getName() + "." + uid : wp.getName();
                
                ConfigurationSection section = wpcb.getWb().getFileConfiguration().getConfigurationSection(key);
                if (section != null) {
                    suggestions = section.getKeys(false).stream()
                        .filter(name -> name.toLowerCase().startsWith(input))
                        .collect(Collectors.toList());
                }
            }
        }
        // Third argument for set and edit: suggest materials
        else if (args.length == 3) {
            String action = args[0].toLowerCase();
            String input = args[2].toLowerCase();
            
            if (action.equals("set") || action.equals("edit")) {
                suggestions = Arrays.stream(Material.values())
                    .filter(Material::isItem)
                    .map(mat -> mat.name().toLowerCase())
                    .filter(name -> name.contains(input))
                    .limit(20)
                    .collect(Collectors.toList());
            }
        }
        
        return suggestions;
    }
}
