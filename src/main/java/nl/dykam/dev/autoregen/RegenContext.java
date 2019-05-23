package nl.dykam.dev.autoregen;

import nl.dykam.dev.autoregen.actions.Action;
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
    private Collection<Action> actions;

    public RegenContext(Player player, BlockState block, ItemStack tool, Inventory inventory, Collection<Action> actions) {
        this.player = player;
        this.block = block;
        this.tool = tool;
        this.inventory = inventory;
        this.actions = actions;
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

    public Player getPlayer() {
        return player;
    }

    public Collection<Action> getActions() {
        return actions;
    }
}
