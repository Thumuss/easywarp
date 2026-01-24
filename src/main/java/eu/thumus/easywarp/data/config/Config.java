package eu.thumus.easywarp.data.config;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import eu.thumus.easywarp.Main;

public final class Config {

    static FileConfiguration FC;

    static String prefix = "Server -> §4";
    static Material defaultMaterialName = Material.DARK_OAK_BUTTON;
    static HashMap<String, WarpConfig> warps = new HashMap<>();

    static public void createData(Main main) {
        FC = main.getConfig();
        setPrefix(FC.getString("prefix", "Server -> §4"));
        setDefaultMaterialName(FC.getString("defaultMaterialName", "DARK_OAK_BUTTON"));
        warps.clear();
        ConfigurationSection CS = FC.getConfigurationSection("warps");
        if (CS == null) return;
        for (String key : CS.getKeys(false)) {
            warps.put(key, new WarpConfig(CS.getConfigurationSection(key)));
        }
    }

    static public WarpConfig getWarpByCommand(String command) {
        for (WarpConfig warp : warps.values()) {
            if (warp.getCommand().equalsIgnoreCase(command)) {
                return warp;
            }
            // Check aliases too
            if (warp.getAliases() != null) {
                for (String alias : warp.getAliases()) {
                    if (alias.equalsIgnoreCase(command)) {
                        return warp;
                    }
                }
            }
        }
        return null;
    }

    static public String getPrefix() {
        return prefix;
    }

    static public void setPrefix(String prefix) {
        Config.prefix = prefix;
    } 

    static public Material getDefaultMaterialName() {
        return Config.defaultMaterialName;
    }

    static public void setDefaultMaterialName(Material defaultMaterialName) {
        Config.defaultMaterialName = defaultMaterialName;
    }

    static public void setDefaultMaterialName(String defaultMaterialName) {
        Config.defaultMaterialName = Material.getMaterial(defaultMaterialName);
    }

    static public HashMap<String, WarpConfig> getWarps() {
        return Config.warps;
    }

    static public void setWarps(HashMap<String, WarpConfig> warps) {
        Config.warps = warps;
    }
}
