package eu.thumus.easywarp.warp;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import eu.thumus.easywarp.Main;

public final class WarpPerso extends WarpBase {
    public WarpPerso(Main plugin) {
        super(plugin, "players", "Warp Perso");
        usingPlayerUID = true;
        createUs(plugin);
    }

    public void createUs(Main plugin) {
        Object obj = Fc.getConfigurationSection("players");
        if (obj == null) {
            Fc.createSection("players");
            try {
                Fc.save(file);
            } catch (IOException e) {
            }
        }
        for (final Map.Entry<String, Object> entry : Fc.getConfigurationSection("players").getValues(false).entrySet()) {
            this.softCreate(entry.getKey());
            plugin.console.log(Level.INFO, "Les Warps perso de {0} sont load", String.join(" ", entry.getKey().split("_")));
        }
        plugin.console.log(Level.INFO, "Menu warp perso load");
    }
}
