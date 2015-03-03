package nl.dykam.dev.autoregen.regenerators;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public interface RegeneratorCreator {
    String getName();

    Plugin getPlugin();

    ConfigurationSection getDefaultConfiguration();

    Regenerator generate(ConfigurationSection config);
}
