package nl.dykam.dev.autoregen.regenerators.defaults;

import com.google.common.collect.ImmutableSet;
import nl.dykam.dev.autoregen.AutoRegenPlugin;
import nl.dykam.dev.autoregen.RegenContext;
import nl.dykam.dev.autoregen.actions.DropAction;
import nl.dykam.dev.autoregen.actions.RegenerateAction;
import nl.dykam.dev.autoregen.actions.RemoveAction;
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
import org.bukkit.material.Crops;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CropRegenerator implements Regenerator<Material> {
    private static Set<Trigger> triggers;

    static {
        triggers = new HashSet<>();
        triggers.add(new Trigger(null, ImmutableSet.of(Material.CROPS, Material.CARROT, Material.POTATO, Material.BEETROOT_BLOCK), null, null));
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
        MaterialData data = context.getBlock().getData();
        if (!(data instanceof Crops)) {
            return null;
        }
        context.getActions().clear();
        Crops crops = (Crops)data;
        if (crops.getState() != CropState.RIPE) {
            Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This isn't fully grown";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            return null;
        }
        Collection<ItemStack> drops = context.getBlock().getBlock().getDrops(context.getTool());
        drops.removeIf(itemStack -> itemStack.getType() == Material.SEEDS);
        context.getActions().add(new DropAction(drops));
        context.getActions().add(new RemoveAction(context.getBlock().getBlock()));
        context.getActions().add(new RegenerateAction());
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
