package nl.dykam.dev.autoregen;

import com.google.common.collect.Lists;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class RegenContext {
    private final Player player;
    private final BlockState block;
    private final ItemStack tool;
    private final Inventory inventory;
    private Collection<ItemStack> drops;

    public RegenContext(Player player, BlockState block, ItemStack tool, Inventory inventory, Collection<ItemStack> drops) {
        this.player = player;
        this.block = block;
        this.tool = tool;
        this.inventory = inventory;
        this.drops = drops;
        this.drops = Lists.newArrayList(drops);
    }

    public BlockState getBlock() {
        return block;
    }

    public ItemStack getTool() {
        return tool;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Collection<ItemStack> getDrops() {
        return drops;
    }

    public Player getPlayer() {
        return player;
    }
}
