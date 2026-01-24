package eu.thumus.easywarp.model;

import java.util.*;

/**
 * Extended metadata for warps
 */
public class WarpMetadata {
    private WarpVisibility visibility = WarpVisibility.PRIVATE;
    private Set<UUID> sharedWith = new HashSet<>();
    private List<String> lore = new ArrayList<>();
    private boolean favorite = false;
    private long createdAt = System.currentTimeMillis();
    private long lastUsed = 0;
    private int useCount = 0;
    private Long expiresAt = null; // Null = permanent, timestamp = temporary
    private Map<String, String> conditions = new HashMap<>(); // Custom conditions
    private boolean isProtected = false;
    private Integer customModelData = null;

    public WarpVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(WarpVisibility visibility) {
        this.visibility = visibility;
    }

    public Set<UUID> getSharedWith() {
        return sharedWith;
    }

    public void addSharedPlayer(UUID playerUUID) {
        this.sharedWith.add(playerUUID);
    }

    public void removeSharedPlayer(UUID playerUUID) {
        this.sharedWith.remove(playerUUID);
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(long lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getUseCount() {
        return useCount;
    }

    public void incrementUseCount() {
        this.useCount++;
        this.lastUsed = System.currentTimeMillis();
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != null && System.currentTimeMillis() > expiresAt;
    }

    public boolean isTemporary() {
        return expiresAt != null;
    }

    public Map<String, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, String> conditions) {
        this.conditions = conditions;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(Integer customModelData) {
        this.customModelData = customModelData;
    }

    /**
     * Check if a player can access this warp
     */
    public boolean canAccess(UUID playerUUID, UUID ownerUUID) {
        if (playerUUID.equals(ownerUUID)) {
            return true; // Owner always has access
        }
        
        switch (visibility) {
            case PRIVATE:
                return false;
            case SHARED:
                return sharedWith.contains(playerUUID);
            case PUBLIC:
                return true;
            default:
                return false;
        }
    }
}
