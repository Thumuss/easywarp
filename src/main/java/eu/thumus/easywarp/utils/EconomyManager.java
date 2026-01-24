package eu.thumus.easywarp.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Manages economy integration via Vault
 */
public class EconomyManager {
    private final JavaPlugin plugin;
    private Economy economy = null;
    private boolean enabled = false;
    private double costPerWarp;
    private double costPerTeleport;
    private double refundOnDelete;

    public EconomyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("economy.enabled", false);
        this.costPerWarp = plugin.getConfig().getDouble("economy.cost-per-warp-creation", 100);
        this.costPerTeleport = plugin.getConfig().getDouble("economy.cost-per-teleport", 10);
        this.refundOnDelete = plugin.getConfig().getDouble("economy.refund-on-delete", 50);
        
        if (enabled) {
            setupEconomy();
        }
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().warning("Vault not found! Economy features disabled.");
            enabled = false;
            return false;
        }
        
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            plugin.getLogger().warning("No economy plugin found! Economy features disabled.");
            enabled = false;
            return false;
        }
        
        economy = rsp.getProvider();
        plugin.getLogger().info("Economy integration enabled with " + economy.getName());
        return economy != null;
    }

    public boolean isEnabled() {
        return enabled && economy != null;
    }

    /**
     * Charge player for creating a warp
     */
    public boolean chargeWarpCreation(Player player) {
        if (!isEnabled() || costPerWarp <= 0) {
            return true;
        }
        
        if (!economy.has(player, costPerWarp)) {
            return false;
        }
        
        economy.withdrawPlayer(player, costPerWarp);
        return true;
    }

    /**
     * Charge player for teleportation
     */
    public boolean chargeTeleport(Player player) {
        if (!isEnabled() || costPerTeleport <= 0) {
            return true;
        }
        
        if (!economy.has(player, costPerTeleport)) {
            return false;
        }
        
        economy.withdrawPlayer(player, costPerTeleport);
        return true;
    }

    /**
     * Refund player for deleting a warp
     */
    public void refundWarpDeletion(Player player) {
        if (!isEnabled() || refundOnDelete <= 0) {
            return;
        }
        
        economy.depositPlayer(player, refundOnDelete);
    }

    public double getCostPerWarp() {
        return costPerWarp;
    }

    public double getCostPerTeleport() {
        return costPerTeleport;
    }

    public double getRefundOnDelete() {
        return refundOnDelete;
    }

    public String formatMoney(double amount) {
        if (economy != null) {
            return economy.format(amount);
        }
        return String.format("%.2f", amount);
    }
}
