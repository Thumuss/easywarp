package eu.thumus.easywarp.warp;

import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import eu.thumus.easywarp.Main;

public final class WarpPerso extends WarpBase implements Listener
{
    public WarpPerso(Main plugin) {
        super(plugin);
    }

    @Override
    public void create(final Main plugin) {
        super.create(plugin);
        for (final Map.Entry<String, Object> entry : Fc.getConfigurationSection("players").getValues(false).entrySet()) {
            this.softCreate(entry.getKey());
            plugin.console.log(Level.INFO, "Les Warps perso de {0} sont load", String.join(" ", entry.getKey().split("_")));
        }
        plugin.console.log(Level.INFO, "Menu warp perso load");
    }
    
    public void softCreate(final String strKey) {
        final ConfigurationSection players = Fc.getConfigurationSection("players");
        if (players == null) return;
        final ConfigurationSection playerStr = players.getConfigurationSection(strKey);
        if (playerStr == null) return;
        Map<String, Object> values = playerStr.getValues(false);
        double size = values.size();
        double result = Math.ceil(size / 9.0) * 9.0;
        if (result < 9) { result = (double) 9; }
        createInv((int) result, values, "Warp perso", strKey);
    }
}
