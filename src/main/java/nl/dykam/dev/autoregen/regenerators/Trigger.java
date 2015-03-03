package nl.dykam.dev.autoregen.regenerators;

import nl.dykam.dev.autoregen.RegenContext;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class Trigger {
    private final Set<ItemStack> tools;
    private final Set<Material> materials;
    private final Set<MaterialData> exactMaterials;
    private final Collection<ItemStack> inventoryRequirements;

    public Trigger(Set<ItemStack> tools, Set<Material> materials, Set<MaterialData> exactMaterials,
                   Collection<ItemStack> inventoryRequirements) {
        this.tools = tools != null ? tools : Collections.<ItemStack>emptySet();
        this.materials = materials != null ? materials : Collections.<Material>emptySet();
        this.exactMaterials = exactMaterials != null ? exactMaterials : Collections.<MaterialData>emptySet();
        this.inventoryRequirements = inventoryRequirements != null
                ? Collections.unmodifiableCollection(inventoryRequirements)
                : Collections.<ItemStack>emptyList();
    }

    public Set<ItemStack> getTools() {
        return tools;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public Set<MaterialData> getExactMaterials() {
        return exactMaterials;
    }

    public Collection<ItemStack> getInventoryRequirements() {
        return inventoryRequirements;
    }

    public boolean test(RegenContext context) {
        if (!tools.isEmpty() && !tools.contains(context.getTool()))
            return false;
        if (!materials.isEmpty() && !materials.contains(context.getBlock().getType()))
            return false;
        if (!exactMaterials.isEmpty() && !exactMaterials.contains(context.getBlock().getData()))
            return false;
        for (ItemStack inventoryRequirement : inventoryRequirements) {
            if (!context.getInventory().contains(inventoryRequirement))
                return false;
        }

        return true;
    }
}
