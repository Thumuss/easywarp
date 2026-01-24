package eu.thumus.easywarp.data.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Configuration class for permissions of a warp system
 */
public class PermissionConfig {
    private String base;
    private Map<String, Boolean> permissions;
    private Map<String, Boolean> defaultFor;
    
    public PermissionConfig(ConfigurationSection cs, String warpKey) {
        permissions = new HashMap<>();
        defaultFor = new HashMap<>();
        
        if (cs != null) {
            this.base = cs.getString("base", "easywarp." + warpKey);
            
            // Load which permissions are enabled
            permissions.put("use", cs.getBoolean("use", true));
            permissions.put("set", cs.getBoolean("set", true));
            permissions.put("delete", cs.getBoolean("delete", true));
            permissions.put("edit", cs.getBoolean("edit", true));
            permissions.put("reload", cs.getBoolean("reload", true));
            
            // Load default permissions
            ConfigurationSection defaultSection = cs.getConfigurationSection("defaultFor");
            if (defaultSection != null) {
                defaultFor.put("use", defaultSection.getBoolean("use", true));
                defaultFor.put("set", defaultSection.getBoolean("set", false));
                defaultFor.put("delete", defaultSection.getBoolean("delete", false));
                defaultFor.put("edit", defaultSection.getBoolean("edit", false));
                defaultFor.put("reload", defaultSection.getBoolean("reload", false));
            } else {
                // Default values if not specified
                defaultFor.put("use", true);
                defaultFor.put("set", false);
                defaultFor.put("delete", false);
                defaultFor.put("edit", false);
                defaultFor.put("reload", false);
            }
        } else {
            // Default configuration
            this.base = "easywarp." + warpKey;
            permissions.put("use", true);
            permissions.put("set", true);
            permissions.put("delete", true);
            permissions.put("edit", true);
            permissions.put("reload", true);
            
            defaultFor.put("use", true);
            defaultFor.put("set", false);
            defaultFor.put("delete", false);
            defaultFor.put("edit", false);
            defaultFor.put("reload", false);
        }
    }
    
    /**
     * Get the full permission node for an action
     * @param action The action (use, set, delete, edit, reload)
     * @return The full permission node (e.g., "easywarp.players.use")
     */
    public String getPermission(String action) {
        return base + "." + action;
    }
    
    /**
     * Check if a permission is enabled
     */
    public boolean isEnabled(String action) {
        return permissions.getOrDefault(action, true);
    }
    
    /**
     * Check if a permission is granted by default
     */
    public boolean isDefaultFor(String action) {
        return defaultFor.getOrDefault(action, false);
    }
    
    public String getBase() {
        return base;
    }
    
    public void setBase(String base) {
        this.base = base;
    }
    
    public Map<String, Boolean> getPermissions() {
        return permissions;
    }
    
    public Map<String, Boolean> getDefaultFor() {
        return defaultFor;
    }
}
