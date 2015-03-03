package nl.dykam.dev.autoregen.regenerators.defaults;

import com.google.common.collect.ImmutableSet;
import nl.dykam.dev.autoregen.AutoRegenPlugin;
import nl.dykam.dev.autoregen.RegenContext;
import nl.dykam.dev.autoregen.regenerators.Regenerator;
import nl.dykam.dev.autoregen.regenerators.RegeneratorCreator;
import nl.dykam.dev.autoregen.regenerators.Trigger;
import org.bukkit.ChatColor;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CropRegenerator implements Regenerator<Material> {
    private static Set<Trigger> triggers;

    static {
        triggers = new HashSet<>();
        triggers.add(new Trigger(null, ImmutableSet.of(Material.CROPS, Material.CARROT, Material.POTATO), null, null));
    }

    @Override
    public Set<Trigger> getTriggers() {
        return triggers;
    }

    @Override
    public boolean validate(RegenContext context) {
        return true;
    }

    @Override
    public Material breakdown(RegenContext context) {
        MaterialData crops = context.getBlock().getData();
        if (crops.getData() != CropState.RIPE.getData()) {
            Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This isn't fully grown";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            context.getDrops().clear();
            return null;
        }
        for (Iterator<ItemStack> iterator = context.getDrops().iterator(); iterator.hasNext(); ) {
            ItemStack itemStack = iterator.next();
            if (itemStack.getType() == Material.SEEDS)
                iterator.remove();
        }
        context.getBlock().getBlock().setType(Material.AIR);
        return context.getBlock().getType();
    }

    @Override
    public void regenerate(RegenContext context, Material brokenType) {
        BlockState block = context.getBlock();
        if (block.getBlock().getType() == Material.AIR)
            block.getBlock().setType(brokenType);
    }

    public static class Creator implements RegeneratorCreator {
        private Plugin plugin;

        public Creator(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "crops";
        }

        @Override
        public Plugin getPlugin() {
            return plugin;
        }

        @Override
        public ConfigurationSection getDefaultConfiguration() {
            return null;
        }

        @Override
        public Regenerator generate(ConfigurationSection config) {
            return new CropRegenerator();
        }
    }
}
