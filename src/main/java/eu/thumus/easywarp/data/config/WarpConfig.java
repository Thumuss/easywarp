package eu.thumus.easywarp.data.config;

import org.bukkit.configuration.ConfigurationSection;

final public class WarpConfig {
    String filename;
    String display;
    boolean usingPlayerUID;
    ConfigurationSection CS;
    String command;
    String name;
    String[] aliases;
    String description;
    String usage;
    PermissionConfig permissionConfig;

    public WarpConfig(ConfigurationSection CS) {
        this.CS = CS;
        this.createData();
    }

    public void createData() {
        setName(CS.getName());
        setFilename(CS.getString("filename", "default.yml"));
        setDisplay(CS.getString("display", "Warp"));
        setUsingPlayerUID(CS.getBoolean("usingPlayerUID", false));
        setCommand(CS.getString("command", CS.getName()));
        setAliases(CS.getStringList("aliases").toArray(new String[0]));
        setDescription(CS.getString("description", "Warp command"));
        setUsage(CS.getString("usage", "/" + getCommand()));
        setPermissionConfig(new PermissionConfig(CS.getConfigurationSection("permissions"), getName()));
    }

    public boolean isUsingPlayerUID() {
        return usingPlayerUID;
    }

    public void setUsingPlayerUID(boolean usingPlayerUID) {
        this.usingPlayerUID = usingPlayerUID;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
    
    public PermissionConfig getPermissionConfig() {
        return permissionConfig;
    }
    
    public void setPermissionConfig(PermissionConfig permissionConfig) {
        this.permissionConfig = permissionConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
