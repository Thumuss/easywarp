package eu.thumus.easywarp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.thumus.easywarp.api.EasyWarpAPI;
import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.data.config.Config;
import eu.thumus.easywarp.data.config.PermissionConfig;
import eu.thumus.easywarp.data.config.WarpConfig;
import eu.thumus.easywarp.placeholders.WarpPlaceholders;
import eu.thumus.easywarp.utils.ConfirmationManager;
import eu.thumus.easywarp.utils.CooldownManager;
import eu.thumus.easywarp.utils.EconomyManager;
import eu.thumus.easywarp.utils.MessageManager;
import eu.thumus.easywarp.utils.NotificationManager;
import eu.thumus.easywarp.utils.TemporaryWarpManager;
import eu.thumus.easywarp.utils.WarpConditionsManager;
import eu.thumus.easywarp.utils.WarpLimitManager;
import eu.thumus.easywarp.utils.WorldSettingsManager;
import eu.thumus.easywarp.warp.WarpBase;

public final class Main extends JavaPlugin {
    private Commands cmds;
    @SuppressWarnings("NonConstantLogger")
    public Logger console;
    private MessageManager messageManager;
    private CooldownManager cooldownManager;
    private EconomyManager economyManager;
    private WarpLimitManager warpLimitManager;
    private TemporaryWarpManager temporaryWarpManager;
    private WorldSettingsManager worldSettingsManager;
    private ConfirmationManager confirmationManager;
    private WarpConditionsManager warpConditionsManager;
    private NotificationManager notificationManager;
    private WarpPlaceholders placeholderExpansion;



    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        console = getLogger();
        
        // Initialize message manager
        String locale = getConfig().getString("locale", "en");
        this.messageManager = new MessageManager(this, locale);
        
        // Initialize cooldown manager
        int defaultCooldown = getConfig().getInt("cooldown.default", 3);
        this.cooldownManager = new CooldownManager(defaultCooldown);
        
        // Initialize all feature managers
        this.economyManager = new EconomyManager(this);
        this.warpLimitManager = new WarpLimitManager(this);
        this.temporaryWarpManager = new TemporaryWarpManager(this);
        this.worldSettingsManager = new WorldSettingsManager(this);
        this.confirmationManager = new ConfirmationManager(this);
        this.warpConditionsManager = new WarpConditionsManager(this);
        this.notificationManager = new NotificationManager(this);
        
        // Initialize PlaceholderAPI if present
        if (getConfig().getBoolean("placeholderapi.enabled", true) && 
            getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderExpansion = new WarpPlaceholders(this);
            if (this.placeholderExpansion.register()) {
                console.log(Level.INFO, "PlaceholderAPI integration enabled");
            }
        }
        
        WarpBase.MATERIAL_NAME = getConfig().getString("defaultMaterialName", "DARK_OAK_BUTTON");
        Config.createData(this);
        this.cmds = new Commands(this);
        
        // Initialize API
        EasyWarpAPI.initialize(this);
        
        // Register event listeners for warps
        final PluginManager pm = this.getServer().getPluginManager();
        cmds.getWCMap().values().stream().map(WarpCommandsBase::getWb).forEach((warp) -> pm.registerEvents(warp, this));
        
        // Dynamically register permissions
        registerDynamicPermissions();
        
        // Dynamically register commands from config
        registerDynamicCommands();
        
        console.log(Level.INFO, "Plugin loaded with " + Config.getWarps().size() + " warp systems");
        
        // Log feature status
        logFeatureStatus();
    }
    
    /**
     * Register all permissions dynamically from the config
     */
    private void registerDynamicPermissions() {
        PluginManager pm = getServer().getPluginManager();
        
        for (Map.Entry<String, WarpConfig> entry : Config.getWarps().entrySet()) {
            WarpConfig warpConfig = entry.getValue();
            PermissionConfig permConfig = warpConfig.getPermissionConfig();
            
            // Parent permission (e.g., easywarp.players.*)
            String parentPermissionNode = permConfig.getBase() + ".*";
            Permission parentPermission = new Permission(
                parentPermissionNode,
                "All permissions for " + warpConfig.getDisplay(),
                PermissionDefault.OP
            );
            
            try {
                pm.addPermission(parentPermission);
                console.log(Level.INFO, "Registered parent permission: " + parentPermissionNode);
            } catch (IllegalArgumentException e) {
                // Permission already exists
            }
            
            // Register individual permissions for each action
            String[] actions = {"use", "set", "delete", "edit", "reload"};
            for (String action : actions) {
                if (!permConfig.isEnabled(action)) {
                    continue; // Skip disabled permissions
                }
                
                String permissionNode = permConfig.getPermission(action);
                PermissionDefault permDefault = permConfig.isDefaultFor(action) 
                    ? PermissionDefault.TRUE 
                    : PermissionDefault.OP;
                
                Permission permission = new Permission(
                    permissionNode,
                    "Allow " + action + " action for " + warpConfig.getDisplay(),
                    permDefault
                );
                
                // Add as child of parent permission
                permission.addParent(parentPermission, true);
                
                try {
                    pm.addPermission(permission);
                    console.log(Level.INFO, "Registered permission: " + permissionNode + " (default: " + permDefault + ")");
                } catch (IllegalArgumentException e) {
                    // Permission already exists
                }
            }
        }
    }
    
    /**
     * Register all 
    
    /**
     * Register all warp commands dynamically from the config
     */
    private void registerDynamicCommands() {
        try {
            CommandMap commandMap = getCommandMap();
            DynamicCommandExecutor executor = new DynamicCommandExecutor(cmds);
            
            for (Map.Entry<String, WarpConfig> entry : Config.getWarps().entrySet()) {
                WarpConfig warpConfig = entry.getValue();
                String commandName = warpConfig.getCommand();
                
                // Create a dynamic command
                DynamicWarpCommand command = new DynamicWarpCommand(
                    commandName,
                    warpConfig.getDescription(),
                    warpConfig.getUsage(),
                    Arrays.asList(warpConfig.getAliases()),
                    "easywarp." + entry.getKey()
                );
                
                command.setExecutor(executor);
                
                // Register the command
                commandMap.register("easywarp", command);
                
                console.log(Level.INFO, "Registered command: /" + commandName + 
                    " with aliases: " + Arrays.toString(warpConfig.getAliases()));
            }
        } catch (Exception e) {
            console.log(Level.SEVERE, "Failed to register dynamic commands", e);
        }
    }
    
    /**
     * Get the CommandMap using reflection
     */
    private CommandMap getCommandMap() {
        try {
            if (getServer().getPluginManager() instanceof SimplePluginManager) {
                java.lang.reflect.Field field = SimplePluginManager.class.getDeclaredField("commandMap");
                field.setAccessible(true);
                return (CommandMap) field.get(getServer().getPluginManager());
            }
        } catch (Exception e) {
            console.log(Level.SEVERE, "Failed to get CommandMap", e);
        }
        return null;
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
    
    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        return cmds.onTabComplete(sender, command, alias, args);
    }
    
    public MessageManager getMessageManager() {
        return messageManager;
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
    
    public Commands getCommands() {
        return cmds;
    }
    
    public EconomyManager getEconomyManager() {
        return economyManager;
    }
    
    public WarpLimitManager getWarpLimitManager() {
        return warpLimitManager;
    }
    
    public TemporaryWarpManager getTemporaryWarpManager() {
        return temporaryWarpManager;
    }
    
    public WorldSettingsManager getWorldSettingsManager() {
        return worldSettingsManager;
    }
    
    public ConfirmationManager getConfirmationManager() {
        return confirmationManager;
    }
    
    public WarpConditionsManager getWarpConditionsManager() {
        return warpConditionsManager;
    }
    
    public NotificationManager getNotificationManager() {
        return notificationManager;
    }
    
    /**
     * Log the status of all features
     */
    private void logFeatureStatus() {
        console.log(Level.INFO, "=== Feature Status ===");
        console.log(Level.INFO, "Economy Integration: " + (economyManager.isEnabled() ? "ENABLED" : "DISABLED"));
        console.log(Level.INFO, "Temporary Warps: " + (temporaryWarpManager.isEnabled() ? "ENABLED" : "DISABLED"));
        console.log(Level.INFO, "Warp Conditions: " + (warpConditionsManager.isEnabled() ? "ENABLED" : "DISABLED"));
        console.log(Level.INFO, "PlaceholderAPI: " + (placeholderExpansion != null ? "ENABLED" : "DISABLED"));
        console.log(Level.INFO, "=====================");
    }
    
    /**
     * Custom command class for dynamically registered warp commands
     */
    private class DynamicWarpCommand extends BukkitCommand {
        private DynamicCommandExecutor executor;
        private final String permission;
        
        protected DynamicWarpCommand(String name, String description, String usageMessage, 
                                     List<String> aliases, String permission) {
            super(name);
            this.setDescription(description);
            this.setUsage(usageMessage);
            this.setAliases(aliases);
            this.permission = permission;
        }
        
        public void setExecutor(DynamicCommandExecutor executor) {
            this.executor = executor;
        }
        
        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (executor != null) {
                return executor.onCommand(sender, this, commandLabel, args);
            }
            return false;
        }
        
        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            return cmds.onTabComplete(sender, this, alias, args);
        }
        
        @Override
        public String getPermission() {
            return this.permission;
        }
    }
}
