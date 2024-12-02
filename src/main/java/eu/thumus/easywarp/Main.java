package eu.thumus.easywarp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import eu.thumus.easywarp.commands.WarpCommandsBase;
import eu.thumus.easywarp.data.config.Config;
import eu.thumus.easywarp.warp.WarpBase;

public final class Main extends JavaPlugin {
    private Commands cmds;
    @SuppressWarnings("NonConstantLogger")
    public Logger console;



    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        console = getLogger();
        WarpBase.MATERIAL_NAME = getConfig().getString("defaultMaterialName", "DARK_OAK_BUTTON");
        Config.createData(this);
        this.cmds = new Commands(this);
        final PluginManager pm = this.getServer().getPluginManager();
        cmds.getWCMap().values().stream().map(WarpCommandsBase::getWb).forEach((warp) -> pm.registerEvents(warp, this));
        console.log(Level.INFO, "Plugin loaded");
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
}
