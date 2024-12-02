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
                main.console.log(Level.INFO, customConfigFile.getAbsolutePath());
                customConfig.save(customConfigFile);
            } catch (IOException ex) {
            }
        } 

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
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
        FC.save(SF.get(name));
    }
}
