package eu.thumus.easywarp.data.config;

import org.bukkit.configuration.ConfigurationSection;

final public class Warp {
    String filename;
    String display;
    boolean usingPlayerUID;
    ConfigurationSection CS;
    String linkTo;
    String name;

    public Warp(ConfigurationSection CS) {
        this.CS = CS;
        this.createData();
    }

    public void createData() {
        setName(CS.getName());
        setFilename(CS.getString("filename", "default.yml"));
        setDisplay(CS.getString("display", "Warp"));
        setUsingPlayerUID(CS.getBoolean("usingPlayerUID", false));
        setLinkTo(CS.getString("linkTo", null));
    }

    public boolean isUsingPlayerUID() {
        return usingPlayerUID;
    }

    public void setUsingPlayerUID(boolean usingPlayerUID) {
        this.usingPlayerUID = usingPlayerUID;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getLinkTo() {
        return linkTo;
    }

    public void setLinkTo(String linkTo) {
        this.linkTo = linkTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
