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

    public static FileConfiguration createConfig(Main main, String fileName, String name) {
        File customConfigFile = new File(main.getDataFolder(), fileName);

        FileConfiguration customConfig = new YamlConfiguration();

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

    public static void save(String key) throws IOException {
        FileConfiguration FC = SFC.get(key);
        FC.save(SF.get(key));
    }
}
