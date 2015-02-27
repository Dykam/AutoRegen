package nl.dykam.dev.autoregen.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ConfigExtra {
    @SuppressWarnings("unchecked")
    public static List<ConfigurationSection> getConfigList(ConfigurationSection config, String path)
    {
        if (!config.isList(path)) return null;

        List<ConfigurationSection> list = new LinkedList<>();

        for (Object object : config.getList(path)) {
            if (object instanceof Map) {
                MemoryConfiguration mc = new MemoryConfiguration();
                for (Map.Entry<String, ?> entry : ((Map<String,?>) object).entrySet()) {
                    if(entry.getValue() instanceof Map) {
                        mc.createSection(entry.getKey(), (Map<?, ?>) entry.getValue());
                    } else {
                        mc.set(entry.getKey(), entry.getValue());
                    }
                }

                list.add(mc);
            }
        }

        return list;
    }
}
