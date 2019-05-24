package nl.dykam.dev.autoregen.regenerators.defaults;

import com.google.common.collect.ImmutableSet;
import nl.dykam.dev.autoregen.AutoRegenPlugin;
import nl.dykam.dev.autoregen.RegenContext;
import nl.dykam.dev.autoregen.actions.DropAction;
import nl.dykam.dev.autoregen.actions.RemoveAction;
import nl.dykam.dev.autoregen.actions.RegenerateAction;
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
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;

public class TallCropGenerator implements Regenerator<BlockState> {
    private static Set<Trigger> triggers;

    static {
        triggers = new HashSet<>();
        triggers.add(new Trigger(null, ImmutableSet.of(Material.SUGAR_CANE_BLOCK, Material.CACTUS), null, null));
    }

    private final int regenFully;
    private final boolean breakFully;

    public TallCropGenerator(int regenFully, boolean breakFully) {
        this.regenFully = regenFully;
        this.breakFully = breakFully;
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
    public BlockState breakdown(RegenContext context) {
        context.getActions().clear();
        Material type = context.getBlock().getType();
        Block block = context.getBlock().getBlock();

        if (isRoot(context.getBlock()) && !Utilities.isOfType(type, block.getRelative(BlockFace.UP))) {
            Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This hasn't grown enough";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            return null;
        }

        if (breakFully) {
            while (Utilities.isOfType(type, block.getRelative(BlockFace.UP))) {
                block = block.getRelative(BlockFace.UP);
            }
            context.getActions().add(new DropAction(block.getDrops(context.getTool())));
            context.getActions().add(new RemoveAction(block));
            block = block.getRelative(BlockFace.DOWN);
            while (Utilities.isOfType(type, block.getRelative(BlockFace.DOWN))) {
                block = block.getRelative(BlockFace.DOWN);
                context.getActions().add(new DropAction(block.getDrops(context.getTool())));
                context.getActions().add(new RemoveAction(block));
            }
        } else {
            context.getActions().add(new DropAction(block.getDrops(context.getTool())));
            context.getActions().add(new RemoveAction(block));
        }
        context.getActions().add(new RegenerateAction());
        return block.getState();
    }

    @Override
    public void regenerate(RegenContext context, BlockState blockState) {
        if (blockState == null)
            return;

        Material type = blockState.getType();
        if (!isRoot(blockState)) {
            return;
        }
        if (regenFully == 0) {
            blockState.getBlock().setType(type);
        } else {
            Block block = blockState.getBlock();
            // Find bottom most cane
            while (Utilities.isOfType(type, block.getRelative(BlockFace.DOWN))) {
                block = block.getRelative(BlockFace.DOWN);
            }
            for (int i = 0; i < regenFully; i++) {
                if (Utilities.isOfType(Material.AIR, block)) break;

                block.setType(type);
                block.setData((byte) 15);
                block = block.getRelative(BlockFace.UP);
            }
        }
    }

    public static boolean isRoot(BlockState block) {
        Block below = block.getBlock().getRelative(BlockFace.DOWN);
        return !Utilities.isOfType(Material.AIR, below) && !Utilities.isOfType(block.getType(), below);
    }

    public static class Creator implements RegeneratorCreator {
        private Plugin plugin;

        public Creator(Plugin plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getName() {
            return "tallcrops";
        }

        @Override
        public Plugin getPlugin() {
            return plugin;
        }

        @Override
        public ConfigurationSection getDefaultConfiguration() {
            MemoryConfiguration memoryConfiguration = new MemoryConfiguration();
            memoryConfiguration.set("regen-fully", false);
            memoryConfiguration.set("break-fully", false);
            return memoryConfiguration;
        }

        @Override
        public Regenerator generate(ConfigurationSection config) {
            int regenFully = 0;
            boolean breakFully = false;
            if (config != null) {
                regenFully = config.isInt("regen-fully")
                        ? config.getInt("regen-fully")
                        : config.getBoolean("regen-fully", false) ? 3 : 0;
                breakFully = config.getBoolean("break-fully", false);
            }
            return new TallCropGenerator(regenFully, breakFully);
        }
    }
}
