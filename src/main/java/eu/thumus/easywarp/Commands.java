package eu.thumus.easywarp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Commands {

    final private String prefix = "Server -> §4";
    final private Main mn;
    final private FileConfiguration customConfig;
    final private File customConfigFile;

    public FileConfiguration custom() {
        return customConfig;
    }

    public Commands(Main main) {
        mn = main;
        customConfigFile = new File(mn.getDataFolder(), "warp.yml");

        customConfig = new YamlConfiguration();

        if (!customConfigFile.exists()) {
            try {
                customConfigFile.getParentFile().mkdirs();
                mn.getLogger().log(Level.INFO, customConfigFile.getAbsolutePath());
                customConfig.save(customConfigFile);
            } catch (IOException ex) {
            }
        }

        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) throws IOException {
        return switch (command.getName().toLowerCase()) {
            case "warp" ->
                warpCmd(sender, command, label, args);
            case "pwarp" ->
                pwarpCmd(sender, command, label, args);
            case "ec" ->
                ecCmd(sender, command, label, args);
            case "rename" ->
                renameCmd(sender, command, label, args);
            case "setlore" ->
                setloreCmd(sender, command, label, args);
            case "wb" ->
                wbCmd(sender, command, label, args);
            case "spec" ->
                specCmd(sender, command, label, args);
            default ->
                throw new AssertionError();
        };
    }

    public void sendMessage(Player pl, String msg) {
        pl.sendMessage(prefix + msg);
    }

    public boolean warpCmd(final CommandSender sender, final Command command, final String label, String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player pl = (Player) sender;
        String arg0 = args[0];
        String[] newArgs = Arrays.copyOfRange(args, 1, args.length - 1);

        return switch (arg0) {
            case "delete" ->
                warpDeleteCmd(pl, newArgs);
            case "edit" ->
                warpEditCmd(pl, newArgs);
            case "set" ->
                warpSetCmd(pl, newArgs);
            case "reload" ->
                warpReloadCmd(pl, newArgs);
            case "use" ->
                warpUseCmd(pl, newArgs);
            default ->
                warpUseCmd(pl, newArgs);
        };
    }

    public boolean warpUseCmd(final Player pl, final String[] args) {
        if (!pl.hasPermission("easywarp.warp.use")) {
            sendMessage(pl, "Tu ne peux pas utiliser cette commande !");
            return false;
        }

        pl.openInventory(mn.wr2.inv);
        return true;
    }

    public boolean warpDeleteCmd(final Player pl, final String[] args) throws IOException {
        final String path = "warp." + args[0];
        customConfig.set(String.valueOf(path), null);
        customConfig.save(customConfigFile);
        mn.wr2.create(mn);
        sendMessage(pl, "Warp supprimé");
        return true;
    }

    public boolean warpSetCmd(final Player pl, final String[] args) {
        return false;
    }

    public boolean warpReloadCmd(final Player pl, final String[] args) {
        return false;
    }

    public boolean warpEditCmd(final Player pl, final String[] args) {
        return false;
    }

    public boolean pwarpCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }

    public boolean ecCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }

    public boolean wbCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }

    public boolean specCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }

    public boolean renameCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }

    public boolean setloreCmd(final CommandSender sender, final Command command, final String label, final String[] args) {
        return false;
    }
}
