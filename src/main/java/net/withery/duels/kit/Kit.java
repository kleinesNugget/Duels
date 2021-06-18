package net.withery.duels.kit;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Kit {

    private final String fileName;
    private final String name;
    private final String description;
    private final ItemStack icon;
    private final long duelTime;
    private final Equipment equipment;
    private boolean disabled;

    public Kit(@NotNull String fileName, @NotNull String name, @NotNull String description, @NotNull ItemStack icon, long duelTime, @NotNull Equipment equipment, boolean disabled) {
        this.fileName = fileName;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.duelTime = duelTime;
        this.equipment = equipment;
        this.disabled = disabled;
    }

    public @NotNull String getFileName() {
        return fileName;
    }

    public @NotNull String getName() {
        return name;
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull ItemStack getIcon() {
        return icon;
    }

    public long getDuelTime() {
        return duelTime;
    }

    public @NotNull Equipment getEquipment() {
        return equipment;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }
}