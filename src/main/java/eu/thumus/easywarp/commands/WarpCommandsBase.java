package eu.thumus.easywarp.commands;

import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import eu.thumus.easywarp.Main;
import eu.thumus.easywarp.utils.ConfigGenerator;
import eu.thumus.easywarp.warp.WarpBase;

public class WarpCommandsBase {

    final private String prefix;
    final private Main main;
    final private FileConfiguration customConfig;
    private WarpBase wb;
    final private String permission;
    final private String filename;

    public WarpCommandsBase(Main mn, WarpBase wpb, String perm, String fn) {
        main = mn;
        wb = wpb;
        permission = "easywarp." + perm;
        prefix = mn.getConfig().getString("prefix", "Server -> §4");
        filename = fn;
        customConfig = ConfigGenerator.getOrCreateFC(filename);
    }

    public boolean warpCmd(final Player pl, final Command command, final String label, String[] args, String key) throws IOException {
        if (args.length == 0) {
            return warpUseCmd(pl, new String[0], key);
        }
        final String arg0 = args[0];
        final String[] newArgs = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);

        return switch (arg0) {
            case "delete" ->
                warpDeleteCmd(pl, newArgs, key);
            case "edit" ->
                warpEditCmd(pl, newArgs, key);
            case "set" ->
                warpSetCmd(pl, newArgs, key);
            case "reload" ->
                warpReloadCmd(pl, newArgs, key);
            case "use" ->
                warpUseCmd(pl, newArgs, key);
            default ->
                warpUseCmd(pl, newArgs, key);
        };
    }

    public boolean warpUseCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!pl.hasPermission(permission + ".use")) {
            sendMessage(pl, "Tu ne peux pas utiliser cette commande !");
            return false;
        }
        Inventory invs = wb.inv.get(key);
        if (invs == null) {
            customConfig.createSection(key);
            ConfigGenerator.save(filename);
            wb.softCreate(key);
            invs = wb.inv.get(key);
        }
        pl.openInventory(invs);
        return true;
    }

    public boolean warpDeleteCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!pl.hasPermission(permission + ".delete")) {
            sendMessage(pl, prefix + "Tu ne peux pas utiliser cette commande !");
            return false;
        }
        final String path = key + "." + args[0];
        customConfig.set(String.valueOf(path), null);
        ConfigGenerator.save(filename);
        wb.softCreate(key);
        sendMessage(pl, "Warp supprimé"); //TODO
        return true;
    }

    public void sendMessage(Player pl, String msg) {
        pl.sendMessage(prefix + msg);
    }

    public boolean warpSetCmd(final Player pl, final String[] args, String key) throws IOException {
        if (!pl.hasPermission(permission + ".set")) {
            sendMessage(pl, prefix + "Tu ne peux pas utiliser cette commande !");
            return false;
        }
        String item;
        if (args.length > 1) {
            item = args[1].toUpperCase();
            if (Material.getMaterial(item) == null) {
                item = WarpBase.MATERIAL_NAME;
            }
        } else {
            item = WarpBase.MATERIAL_NAME;
        }
        final Location ls = pl.getLocation();
        final String path = key + "." + args[0];
        customConfig.set((path) + ".x", (double) ls.getX());
        customConfig.set((path) + ".v", (int) 2);
        customConfig.set((path) + ".y", (double) ls.getY());
        customConfig.set((path) + ".z", (double) ls.getZ());
        customConfig.set((path) + ".yaw", (String.valueOf(String.valueOf(ls.getYaw())) + "f"));
        customConfig.set((path) + ".pitch", (String.valueOf(String.valueOf(ls.getPitch())) + "f"));
        customConfig.set((path) + ".item", item);
        customConfig.set((path) + ".world", pl.getWorld().getName());
        ConfigGenerator.save("warp");
        wb.softCreate(key);
        pl.sendMessage(prefix + "Warp ajoutée !");
        return true;
    }

    public boolean warpReloadCmd(final Player pl, final String[] args, String key) {
        if (!pl.hasPermission(permission + ".reload")) {
            sendMessage(pl, "Tu ne peux pas utiliser cette commande !");
            return false;
        }
        wb.reload(main);
        pl.sendMessage(prefix + "§4Warp reload!");
        return true;
    }

    public boolean warpEditCmd(final Player pl, final String[] args, String key) {
        // TODO
        return false;
    }

    public WarpBase getWb() {
        return wb;
    }

    public void setWb(WarpBase wb) {
        this.wb = wb;
    }

}
