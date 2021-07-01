package net.withery.duels.config.locale;

import net.withery.duels.Duels;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.logging.Level;

public class LocaleHandler {

    private final static String LOCALE_FILE = "messages.yml";

    private final Duels plugin;
    private final File file;

    private FileConfiguration locale;

    public LocaleHandler(Duels plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), LOCALE_FILE);
    }

    public void sendMessage(@NotNull CommandSender sender, @NotNull LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = string.split("\\{split}");

        for (String message : messages)
            sender.sendMessage(message);
    }

    public void sendMessage(@NotNull CommandSender sender, @NotNull LocaleReference reference, String... variables) {
        sendMessage(sender, reference, true, variables);
    }

    public void sendMessage(CommandSender[] senders, LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = string.split("\\{split}");

        for (CommandSender sender : senders)
            for (String message : messages)
                sender.sendMessage(message);
    }

    public void sendMessage(CommandSender[] senders, LocaleReference reference, String... variables) {
        sendMessage(senders, reference, true, variables);
    }

    public void sendMessage(Collection<CommandSender> senders, LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = string.split("\\{split}");

        for (CommandSender sender : senders)
            for (String message : messages)
                sender.sendMessage(message);
    }

    public void sendMessage(Collection<CommandSender> senders, LocaleReference reference, String... variables) {
        sendMessage(senders, reference, true, variables);
    }

    public void sendMessageToAll(LocaleReference reference, boolean prefix, String... variables) {
        String string = (prefix ? getString(LocaleReference.PREFIX) : "") + getString(reference, variables);
        String[] messages = string.split("\\{split}");

        for (CommandSender player : Bukkit.getOnlinePlayers())
            for (String message : messages)
                player.sendMessage(message);
    }

    public void sendMessageToAll(LocaleReference reference, String... variables) {
        sendMessageToAll(reference, true, variables);
    }

    public String getString(@NotNull LocaleReference reference, String... variables) {
        String string = locale.getString(reference.getReference());

        if (string == null)
            string = reference.getDefaultValue();

        string = ChatColor.translateAlternateColorCodes('&', string);

        if (variables != null) {
            int i = 0;
            for (String variable : reference.getVariables()) {
                if (i >= variables.length) {
                    string = string.replace("%" + variable + "%", "");
                    continue;
                }

                string = string.replace("%" + variable + "%", variables[i++]);
            }
        }

        return string;
    }

    public void load() {
        if (!file.exists()) {
            if (!file.getParentFile().exists())
                if (!file.getParentFile().mkdirs())
                    plugin.getDebugger().log(Level.SEVERE, "An error occurred while creating a directory!", true);

            plugin.saveResource(LOCALE_FILE, false);
        }

        locale = YamlConfiguration.loadConfiguration(file);
    }

}