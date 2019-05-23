package nl.dykam.dev.autoregen.actions;

import org.bukkit.block.Block;

public class RemoveAction implements Action {
    private Block block;

    public RemoveAction(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }
}
