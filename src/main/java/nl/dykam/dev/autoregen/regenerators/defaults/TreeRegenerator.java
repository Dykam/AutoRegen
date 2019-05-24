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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.*;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TreeRegenerator implements Regenerator<Sapling> {
    private static Set<Trigger> triggers;

    static {
        triggers = new HashSet<>();
        triggers.add(new Trigger(null, ImmutableSet.of(Material.LOG, Material.LOG_2, Material.SAPLING, Material.LEAVES, Material.LEAVES_2), null, null));
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
    public Sapling breakdown(RegenContext context) {
        MaterialData data = context.getBlock().getData();
        if (data instanceof Sapling) {
            context.getActions().clear();
            Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This sapling hasn't grown yet";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            return null;
        }
        if (data instanceof Leaves) {
            return null;
        }
        if (!(data instanceof Tree)) {
            return null;
        }
        Wood wood = (Tree) data;

        context.getActions().clear();
        context.getActions().add(new DropAction(Collections.singleton(new ItemStack(context.getBlock().getType()))));
        context.getActions().add(new RemoveAction(context.getBlock().getBlock()));
        context.getActions().add(new RegenerateAction());

        return new Sapling(wood.getSpecies(), false);
    }

    @Override
    public void regenerate(RegenContext context, Sapling sapling) {
        BlockState block = context.getBlock();
        Block blockDown = block.getBlock().getRelative(BlockFace.DOWN);
        if (sapling == null || block.getBlock().getType() != Material.AIR || blockDown == null || !blockDown.getType().isSolid()) {
            return;
        }

        block.getBlock().setType(sapling.getItemType());
        block.getBlock().setData(sapling.getData());
    }

    public static class Creator implements RegeneratorCreator {
        private Plugin plugin;

        public Creator(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "tree";
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
            return new TreeRegenerator();
        }
    }
}
