package eu.thumus.easywarp.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import eu.thumus.easywarp.Main;

public class ConfigGenerator {

    public static HashMap<String, File> SF = new HashMap<>();
    public static HashMap<String, FileConfiguration> SFC = new HashMap<>();
    private static Main mn;

    public static FileConfiguration createConfig(Main main, String filename) {
        mn = main;
        final String name = String.join("-", filename.split("/"));
        File customConfigFile = new File(main.getDataFolder(), filename);

        final FileConfiguration customConfig = new YamlConfiguration();

        if (!customConfigFile.exists()) {
            try {
                customConfigFile.getParentFile().mkdirs();
                main.console.log(Level.INFO, "Creating config file: " + customConfigFile.getAbsolutePath());
                customConfig.save(customConfigFile);
            } catch (IOException ex) {
                main.console.log(Level.SEVERE, "Failed to create config file: " + filename, ex);
            }
        } 

        try {
            customConfig.load(customConfigFile);
        } catch (IOException e) {
            main.console.log(Level.SEVERE, "Failed to load config file: " + filename, e);
        } catch (InvalidConfigurationException e) {
            main.console.log(Level.SEVERE, "Invalid configuration in file: " + filename, e);
        }
        SF.put(name, customConfigFile);
        SFC.put(name, customConfig);
        return customConfig;
    }

    public static FileConfiguration getOrCreateFC(String filename) {
        String name = String.join("-", filename.split("/"));
        if (SFC.containsKey(name)) {
            return SFC.get(name);
        }

        return ConfigGenerator.createConfig(mn, filename);
    }

    public static File getOrCreateFileToSave(String filename) {
        String name = String.join("-", filename.split("/"));
        if (SFC.containsKey(name)) {
            return SF.get(name);
        }
        ConfigGenerator.createConfig(mn, filename);
        return SF.get(name);
    }

    public static void save(String filename) throws IOException {
        String name = String.join("-", filename.split("/"));
        FileConfiguration FC = ConfigGenerator.getOrCreateFC(name);
        File file = SF.get(name);
        
        if (file == null) {
            if (mn != null) {
                mn.console.log(Level.SEVERE, "Cannot save config: File not found for " + filename);
            }
            throw new IOException("File not found for: " + filename);
        }
        
        try {
            FC.save(file);
        } catch (IOException e) {
            if (mn != null) {
                mn.console.log(Level.SEVERE, "Failed to save config file: " + filename, e);
            }
            throw e;
        }
    }
}
