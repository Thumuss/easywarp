package eu.thumus.easywarp;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.warp.WarpPerso;
import eu.thumus.easywarp.warp.WarpW;

public class Commands {

    final private String prefix = "Server -> §4";
    final private WarpCommandsBase WCB;
    final private WarpCommandsBase WCPerso;

    public Commands(Main main) {
        WCB = new WarpCommandsBase(main, new WarpW(main), "classic");
        WCPerso = new WarpCommandsBase(main, new WarpPerso(main), "us");
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player pl = (Player) sender;
        String uid = pl.getUniqueId().toString();
        return switch (command.getName().toLowerCase()) {
            case "warp" ->
                WCB.warpCmd(pl, command, label, args, "warp");
            case "pwarp" ->
                WCPerso.warpCmd(pl, command, label, args, "players." + uid);
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
