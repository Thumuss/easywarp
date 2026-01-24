package eu.thumus.easywarp;

import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Dynamic command executor that delegates to the Commands class
 */
public class DynamicCommandExecutor implements CommandExecutor {
    
    private final Commands commands;
    
    public DynamicCommandExecutor(Commands commands) {
        this.commands = commands;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            return commands.onCommand(sender, command, label, args);
        } catch (IOException ex) {
            sender.sendMessage("§cAn error occurred while executing the command.");
            ex.printStackTrace();
            return false;
        }
    }
}
