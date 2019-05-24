package nl.dykam.dev.autoregen.regenerators.defaults;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Utilities {

    public static boolean isOfType(Material type, Block block) {
        return block != null && block.getType() == type;
    }
}
