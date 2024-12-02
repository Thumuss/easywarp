package eu.thumus.easywarp.warp;
import eu.thumus.easywarp.Main;

public final class WarpUser extends WarpBase {
    public WarpUser(Main plugin, String strKey, String display, String filename) {
        super(plugin, strKey, display, true, filename);
        softCreate(strKey);
    }
}
