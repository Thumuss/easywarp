package eu.thumus.easywarp.warp;

import eu.thumus.easywarp.Main;

public final class WarpWorldWide extends WarpBase {
    public WarpWorldWide(Main plugin, String strKey, String display, String filename) {
        super(plugin, strKey, display, false, filename);
        softCreate(strKey);
    }
}
