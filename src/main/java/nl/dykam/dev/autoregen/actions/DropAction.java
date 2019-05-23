package nl.dykam.dev.autoregen.actions;

import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class DropAction implements Action {
    private Collection<ItemStack> drops;

    public DropAction(Collection<ItemStack> drops) {
        this.drops = drops;
    }

    public Collection<ItemStack> getDrops() {
        return drops;
    }
}
