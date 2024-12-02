package eu.thumus.easywarp;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.data.config.Config;
import eu.thumus.easywarp.data.config.Warp;
import eu.thumus.easywarp.warp.WarpUser;
import eu.thumus.easywarp.warp.WarpWorldWide;

public class Commands {

    public HashMap<String, WarpCommandsBase> WCMap = new HashMap<>();

    public HashMap<String, WarpCommandsBase> getWCMap() {
        return WCMap;
    }

    public void setWCMap(HashMap<String, WarpCommandsBase> WCMap) {
        this.WCMap = WCMap;
    }

    public Commands(Main main) {
        for (Map.Entry<String, Warp> warpConfig : Config.getWarps().entrySet()) {
            Warp w = warpConfig.getValue();
            String key = warpConfig.getKey();
            if (w.isUsingPlayerUID()) {
                WCMap.put(key, new WarpCommandsBase(main, new WarpUser(main, key, w.getDisplay(), w.getDisplay()), key, w.getFilename()));
            } else {
                WCMap.put(key, new WarpCommandsBase(main, new WarpWorldWide(main, key, w.getDisplay(), w.getDisplay()), key, w.getFilename()));
            }
        }
    }

    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) throws IOException {
        if (!(sender instanceof Player)) {
            return false;
        }
        final Player pl = (Player) sender;
        String uid = pl.getUniqueId().toString();
        String commandName = command.getName().toLowerCase();
        Warp wp = Config.getLinkedTo(commandName);
        WarpCommandsBase wpcb = WCMap.get(wp.getName());
        if (wpcb == null) {
            return false;
        }
        return switch (commandName) {
            case "warp" ->
                wpcb.warpCmd(pl, command, label, args, wp.getName());
            case "pwarp" ->
                wpcb.warpCmd(pl, command, label, args, wp.getName() + "." + uid);
            default ->
                throw new Error();
        };
    }
}
