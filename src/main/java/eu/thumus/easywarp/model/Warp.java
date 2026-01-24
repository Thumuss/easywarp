package eu.thumus.easywarp.model;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * Represents a single warp point with all its properties
 */
public class Warp {
    private final String name;
    private final UUID ownerUUID;
    private final Location location;
    private Material material;
    private final WarpMetadata metadata;

    /**
     * Create a new warp
     */
    public Warp(String name, UUID ownerUUID, Location location, Material material) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.material = material;
        this.metadata = new WarpMetadata();
    }

    /**
     * Create warp from existing metadata
     */
    public Warp(String name, UUID ownerUUID, Location location, Material material, WarpMetadata metadata) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.location = location;
        this.material = material;
        this.metadata = metadata;
    }

    // Getters
    public String getName() {
        return name;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public WarpMetadata getMetadata() {
        return metadata;
    }

    // Convenience accessors for common metadata
    public WarpVisibility getVisibility() {
        return metadata.getVisibility();
    }

    public void setVisibility(WarpVisibility visibility) {
        metadata.setVisibility(visibility);
    }

    public boolean isPublic() {
        return metadata.getVisibility() == WarpVisibility.PUBLIC;
    }

    public boolean isShared() {
        return metadata.getVisibility() == WarpVisibility.SHARED;
    }

    public boolean isFavorite() {
        return metadata.isFavorite();
    }

    public void setFavorite(boolean favorite) {
        metadata.setFavorite(favorite);
    }

    public boolean isTemporary() {
        return metadata.isTemporary();
    }

    public boolean isExpired() {
        return metadata.isExpired();
    }

    public List<String> getLore() {
        return metadata.getLore();
    }

    public void setLore(List<String> lore) {
        metadata.setLore(lore);
    }

    /**
     * Check if a player can access this warp
     */
    public boolean canAccess(UUID playerUUID) {
        return metadata.canAccess(playerUUID, ownerUUID);
    }

    /**
     * Serialize warp to configuration section
     */
    public void saveTo(ConfigurationSection section) {
        // Basic location data
        section.set("world", location.getWorld().getName());
        section.set("x", location.getX());
        section.set("y", location.getY());
        section.set("z", location.getZ());
        section.set("yaw", String.valueOf(location.getYaw()));
        section.set("pitch", String.valueOf(location.getPitch()));
        section.set("item", material.name());

        // Metadata
        section.set("visibility", metadata.getVisibility().name());
        
        if (!metadata.getSharedWith().isEmpty()) {
            List<String> sharedList = new ArrayList<>();
            for (UUID uuid : metadata.getSharedWith()) {
                sharedList.add(uuid.toString());
            }
            section.set("shared-with", sharedList);
        }

        if (!metadata.getLore().isEmpty()) {
            section.set("lore", metadata.getLore());
        }

        section.set("favorite", metadata.isFavorite());
        section.set("created-at", metadata.getCreatedAt());
        section.set("last-used", metadata.getLastUsed());
        section.set("use-count", metadata.getUseCount());

        if (metadata.getExpiresAt() != null) {
            section.set("expires-at", metadata.getExpiresAt());
        }

        if (!metadata.getConditions().isEmpty()) {
            section.set("conditions", metadata.getConditions());
        }

        section.set("protected", metadata.isProtected());

        if (metadata.getCustomModelData() != null) {
            section.set("custom-model-data", metadata.getCustomModelData());
        }
    }

    /**
     * Load warp from configuration section
     */
    public static Warp loadFrom(String name, UUID ownerUUID, ConfigurationSection section) {
        // Load location
        String worldName = section.getString("world", "world");
        org.bukkit.World world = org.bukkit.Bukkit.getWorld(worldName);
        if (world == null) {
            return null; // World not found
        }

        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = Float.parseFloat(section.getString("yaw", "0.0"));
        float pitch = Float.parseFloat(section.getString("pitch", "0.0"));
        Location location = new Location(world, x, y, z, yaw, pitch);

        // Load material
        String materialName = section.getString("item", "COMPASS");
        Material material = Material.getMaterial(materialName);
        if (material == null) {
            material = Material.COMPASS;
        }

        // Load metadata
        WarpMetadata metadata = new WarpMetadata();

        String visibilityStr = section.getString("visibility", "PRIVATE");
        try {
            metadata.setVisibility(WarpVisibility.valueOf(visibilityStr));
        } catch (IllegalArgumentException e) {
            metadata.setVisibility(WarpVisibility.PRIVATE);
        }

        if (section.contains("shared-with")) {
            List<String> sharedList = section.getStringList("shared-with");
            for (String uuidStr : sharedList) {
                try {
                    metadata.addSharedPlayer(UUID.fromString(uuidStr));
                } catch (IllegalArgumentException e) {
                    // Skip invalid UUID
                }
            }
        }

        if (section.contains("lore")) {
            metadata.setLore(section.getStringList("lore"));
        }

        metadata.setFavorite(section.getBoolean("favorite", false));
        metadata.setCreatedAt(section.getLong("created-at", System.currentTimeMillis()));
        metadata.setLastUsed(section.getLong("last-used", 0));
        // Use reflection or direct field access for use-count since there's no setter
        int useCount = section.getInt("use-count", 0);
        for (int i = 0; i < useCount; i++) {
            metadata.incrementUseCount(); // This will set use count indirectly
        }

        if (section.contains("expires-at")) {
            metadata.setExpiresAt(section.getLong("expires-at"));
        }

        if (section.contains("conditions")) {
            ConfigurationSection conditionsSection = section.getConfigurationSection("conditions");
            if (conditionsSection != null) {
                Map<String, String> conditions = new HashMap<>();
                for (String key : conditionsSection.getKeys(false)) {
                    conditions.put(key, conditionsSection.getString(key));
                }
                metadata.setConditions(conditions);
            }
        }

        metadata.setProtected(section.getBoolean("protected", false));

        if (section.contains("custom-model-data")) {
            metadata.setCustomModelData(section.getInt("custom-model-data"));
        }

        return new Warp(name, ownerUUID, location, material, metadata);
    }

    @Override
    public String toString() {
        return "Warp{" +
                "name='" + name + '\'' +
                ", owner=" + ownerUUID +
                ", location=" + location +
                ", visibility=" + metadata.getVisibility() +
                ", favorite=" + metadata.isFavorite() +
                ", temporary=" + metadata.isTemporary() +
                '}';
    }

    /**
     * Create a simple key for storing warps
     */
    public String getKey() {
        return name.toLowerCase().replace(" ", "_");
    }

    /**
     * Get world name
     */
    public String getWorldName() {
        return location.getWorld().getName();
    }

    /**
     * Calculate distance to a location
     */
    public double distanceTo(Location other) {
        if (!location.getWorld().equals(other.getWorld())) {
            return Double.MAX_VALUE; // Different worlds
        }
        return location.distance(other);
    }
}
