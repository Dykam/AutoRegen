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
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.CocoaPlant;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CocoaRegenerator implements Regenerator<CocoaPlant> {
    private static Set<Trigger> triggers;

    static {
        triggers = new HashSet<>();
        triggers.add(new Trigger(null, ImmutableSet.of(Material.COCOA), null, null));
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
    public CocoaPlant breakdown(RegenContext context) {
        context.getActions().clear();
        CocoaPlant plant = (CocoaPlant) context.getBlock().getData();
        if (plant.getSize() != CocoaPlant.CocoaPlantSize.LARGE) {
            Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This isn't fully grown";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            return null;
        }
        context.getActions().add(new DropAction(new ArrayList<>(Collections.singleton(new ItemStack(Material.INK_SACK, 3, (short) 3)))));
        context.getActions().add(new RemoveAction(context.getBlock().getBlock()));
        context.getActions().add(new RegenerateAction());
        return plant;
    }

    @Override
    public void regenerate(RegenContext context, CocoaPlant originalPlant) {
        if (originalPlant == null)
            return;
        CocoaPlant clone = originalPlant.clone();
        clone.setSize(CocoaPlant.CocoaPlantSize.SMALL);
        context.getBlock().setData(clone);
        context.getBlock().update(true);

    }

    public static class Creator implements RegeneratorCreator {
        private Plugin plugin;

        public Creator(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "cocoa";
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
            return new CocoaRegenerator();
        }
    }
}
