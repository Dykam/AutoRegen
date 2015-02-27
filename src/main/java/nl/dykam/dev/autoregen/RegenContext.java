package nl.dykam.dev.autoregen;

import com.google.common.collect.Lists;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;
import java.util.List;

public class RegenContext {
    private final BlockState block;
    private final ItemStack tool;
    private final Inventory inventory;
    private Collection<ItemStack> drops;

    public RegenContext(BlockState block, ItemStack tool, Inventory inventory, Collection<ItemStack> drops) {
        this.block = block;
        this.tool = tool;
        this.inventory = inventory;
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
}
