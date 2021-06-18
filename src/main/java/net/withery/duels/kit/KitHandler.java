package net.withery.duels.kit;

import net.withery.duels.Duels;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class KitHandler {

    private final Duels plugin;

    private final Set<Kit> kits = new HashSet<>();

    public KitHandler(Duels plugin) {
        this.plugin = plugin;
    }

    public void load() {
        createExampleKit();

        File[] files = new File(plugin.getDataFolder() + "/kits/").listFiles(pathname -> pathname.getName().endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                String fileName = file.getName();
                String name = config.getString("name");
                if (name == null) {
                    plugin.getDebugger().log(Level.WARNING, "Kit \"" + fileName + "\" is missing a name! Skipping kit...");
                    continue;
                }

                String description = config.getString("description", "None");
                String iconMaterial = config.getString("icon");
                if (iconMaterial == null) {
                    plugin.getDebugger().log(Level.WARNING, "Kit \"" + fileName + "\" is missing an icon material! Skipping kit...");
                    continue;
                }

                Material material = Material.matchMaterial(iconMaterial);
                if (material == null) {
                    plugin.getDebugger().log(Level.WARNING, "Kit \"" + fileName + "\" does not have a valid icon material! Skipping kit...");
                    continue;
                }

                long duelTime = config.getLong("duel-time", 300);
                boolean disabled = config.getBoolean("disabled", false);
                Equipment equipment = new Equipment();

                ConfigurationSection armorSection = config.getConfigurationSection("equipment.armor");
                if (armorSection != null) {
                    for (String key : armorSection.getKeys(false)) {
                        ConfigurationSection partSection = armorSection.getConfigurationSection(key);
                        if (partSection == null) continue;

                        ItemStack armor = ItemStack.deserialize(partSection.getValues(false));
                        switch (key) {
                            case "helmet" -> equipment.setHelmet(armor);
                            case "chestplate" -> equipment.setChestplate(armor);
                            case "leggings" -> equipment.setLeggings(armor);
                            case "boots" -> equipment.setBoots(armor);
                        }
                    }
                }

                ConfigurationSection inventorySection = config.getConfigurationSection("equipment.inventory");
                if (inventorySection != null) {
                    for (String key : inventorySection.getKeys(false)) {
                        int slot;
                        try {
                            slot = Integer.parseInt(key);
                        } catch (NumberFormatException e) {
                            plugin.getDebugger().log(Level.WARNING, "Kit \"" + fileName + "\" has an invalid slot in it's inventory equipment! Skipping item...");
                            continue;
                        }
                        ConfigurationSection itemSection = inventorySection.getConfigurationSection(key);
                        if (itemSection == null) continue;

                        ItemStack item = ItemStack.deserialize(itemSection.getValues(false));
                        equipment.setInventoryItem(slot, item);
                    }
                }

                ConfigurationSection offhandSection = config.getConfigurationSection("equipment.offhand");
                if (offhandSection != null)
                    equipment.setOffhand(ItemStack.deserialize(offhandSection.getValues(false)));

                ItemStack icon = new ItemStack(material);
                ItemMeta itemMeta = icon.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName(ChatColor.BLUE + name);
                    itemMeta.setLore(Collections.singletonList(description));
                    icon.setItemMeta(itemMeta);
                }

                kits.add(new Kit(fileName, name, description, icon, duelTime, equipment, disabled));
                plugin.getDebugger().log(Level.INFO, "Loaded kit " + name);
            }

            final int loadedFiles = files.length;
            final int loadedKits = kits.size();
            plugin.getDebugger().log(Level.INFO, "Loaded " + loadedKits + " kits (Skipped " + (loadedFiles - loadedKits) + " out of " + loadedFiles + " files)");
        }

//        startSavingTask();
    }

    private void createExampleKit() {
        File file = new File(plugin.getDataFolder() + "/kits/", "no-debuff.yml");

        if (!file.exists()) {
            if (!file.getParentFile().exists())
                if (!file.getParentFile().mkdirs())
                    plugin.getDebugger().log(Level.SEVERE, "An error occurred while creating a directory!", true);

            plugin.saveResource("kits/no-debuff.yml", false);
        }
    }

}