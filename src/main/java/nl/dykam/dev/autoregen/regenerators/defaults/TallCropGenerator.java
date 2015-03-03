package nl.dykam.dev.autoregen.regenerators.defaults;

import com.google.common.collect.ImmutableSet;
import nl.dykam.dev.autoregen.AutoRegenPlugin;
import nl.dykam.dev.autoregen.RegenContext;
import nl.dykam.dev.autoregen.regenerators.Regenerator;
import nl.dykam.dev.autoregen.regenerators.RegeneratorCreator;
import nl.dykam.dev.autoregen.regenerators.Trigger;
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
        Block block = context.getBlock().getBlock();
        Block below = block.getRelative(BlockFace.DOWN);
        Block above = block.getRelative(BlockFace.UP);
        context.getDrops().clear();
        if ((below == null || below.getType() != block.getType())
                && ((above == null || above.getType() != block.getType()))
                && block.getData() != 15) {
            final Player player = context.getPlayer();
            String message = ChatColor.GOLD + "This isn't fully grown";
            AutoRegenPlugin.instance().getToaster().sendMessage(player, message);
            return null;
        }
        Material type = block.getType();
        if (breakFully) {
            // Find bottom most cane
            while (below != null && below.getType() == type) {
                block = below;
                below = block.getRelative(BlockFace.DOWN);
            }
        }
        BlockState blockState = block.getState();
        if (block.getData() != 15) {
            block.setType(Material.AIR);
        } else {
            block.breakNaturally(context.getTool());
        }
        context.getDrops().clear();
        return blockState;
    }

    @Override
    public void regenerate(RegenContext context, BlockState blockState) {
        if (blockState == null)
            return;

        Block block = blockState.getBlock();
        Material type = blockState.getType();
        Block below = block.getRelative(BlockFace.DOWN);
        if (regenFully == 0) {
            if (below == null || below.getType() == Material.SUGAR_CANE_BLOCK || below.getType().isSolid()) {
                block.setType(type);
            }
        } else {
            // Find bottom most cane
            while (below != null && below.getType() == type) {
                block = below;
                below = block.getRelative(BlockFace.DOWN);
            }
            for (int i = 0; i < regenFully; i++) {
                if (block == null || block.getType() != Material.AIR) break;

                block.setType(type);
                block.setData((byte) 15);
                block = block.getRelative(BlockFace.UP);
            }
        }
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
