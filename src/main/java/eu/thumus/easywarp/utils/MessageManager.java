package eu.thumus.easywarp.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages internationalized messages for the plugin
 */
public class MessageManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration messages;
    private String locale;
    private final Map<String, FileConfiguration> cachedLocales = new HashMap<>();
    
    public MessageManager(JavaPlugin plugin, String locale) {
        this.plugin = plugin;
        this.locale = locale;
        loadMessages();
    }
    
    /**
     * Load messages for the current locale
     */
    private void loadMessages() {
        String filename = "messages_" + locale + ".yml";
        File messageFile = new File(plugin.getDataFolder(), filename);
        
        // Create file from resources if it doesn't exist
        if (!messageFile.exists()) {
            try {
                messageFile.getParentFile().mkdirs();
                InputStream in = plugin.getResource(filename);
                if (in != null) {
                    Files.copy(in, messageFile.toPath());
                    in.close();
                } else {
                    // Fallback to English if locale not found
                    plugin.getLogger().warning("Locale '" + locale + "' not found, using English");
                    this.locale = "en";
                    filename = "messages_en.yml";
                    messageFile = new File(plugin.getDataFolder(), filename);
                    if (!messageFile.exists()) {
                        in = plugin.getResource(filename);
                        if (in != null) {
                            Files.copy(in, messageFile.toPath());
                            in.close();
                        }
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Failed to create message file: " + filename, e);
            }
        }
        
        messages = YamlConfiguration.loadConfiguration(messageFile);
        cachedLocales.put(locale, messages);
        plugin.getLogger().info("Loaded messages for locale: " + locale);
    }
    
    /**
     * Get a message by key with optional placeholders
     * @param key The message key (e.g., "command.use")
     * @param placeholders Optional placeholders to replace {0}, {1}, etc.
     * @return The formatted message
     */
    public String getMessage(String key, Object... placeholders) {
        String message = messages.getString(key);
        
        if (message == null) {
            plugin.getLogger().warning("Missing message key: " + key);
            return "§cMissing message: " + key;
        }
        
        // Replace placeholders
        for (int i = 0; i < placeholders.length; i++) {
            message = message.replace("{" + i + "}", String.valueOf(placeholders[i]));
        }
        
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    /**
     * Get a message with prefix
     */
    public String getMessageWithPrefix(String key, Object... placeholders) {
        String prefix = getMessage("info.prefix");
        String message = getMessage(key, placeholders);
        return prefix + message;
    }
    
    /**
     * Send a message to a command sender
     */
    public void sendMessage(CommandSender sender, String key, Object... placeholders) {
        String message = getMessage(key, placeholders);
        if (message != null && !message.isEmpty()) {
            sender.sendMessage(message);
        }
    }
    
    /**
     * Send a message with prefix to a command sender
     */
    public void sendMessageWithPrefix(CommandSender sender, String key, Object... placeholders) {
        String message = getMessageWithPrefix(key, placeholders);
        if (message != null && !message.isEmpty()) {
            sender.sendMessage(message);
        }
    }
    
    /**
     * Get the current locale
     */
    public String getLocale() {
        return locale;
    }
    
    /**
     * Set and reload a new locale
     */
    public void setLocale(String newLocale) {
        if (cachedLocales.containsKey(newLocale)) {
            this.locale = newLocale;
            this.messages = cachedLocales.get(newLocale);
        } else {
            this.locale = newLocale;
            loadMessages();
        }
    }
    
    /**
     * Reload messages from disk
     */
    public void reload() {
        cachedLocales.clear();
        loadMessages();
    }
    
    /**
     * Check if a message exists and is not null
     */
    public boolean hasMessage(String key) {
        return messages.getString(key) != null;
    }
    
    /**
     * Get prefix for manual message construction
     */
    public String getPrefix() {
        return getMessage("info.prefix");
    }
}
