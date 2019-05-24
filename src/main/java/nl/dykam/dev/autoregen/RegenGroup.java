package nl.dykam.dev.autoregen;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import nl.dykam.dev.autoregen.regenerators.Regenerator;
import nl.dykam.dev.autoregen.regenerators.Trigger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.List;

public class RegenGroup {
    private String name;
    private boolean protect;
    private List<RegeneratorSet> regeneratorSets;
    private ListMultimap<Material, TriggerRegeneratorPair> regeneratorsById;
    private ListMultimap<MaterialData, TriggerRegeneratorPair> regeneratorsByIdAndData;

    @SuppressWarnings("unchecked")
    public RegenGroup(String name, List<RegeneratorSet> regeneratorSets, boolean protect) {
        this.name = name;
        this.protect = protect;
        this.regeneratorSets = ImmutableList.copyOf(regeneratorSets);

        regeneratorsById = MultimapBuilder.hashKeys().linkedListValues().build();
        regeneratorsByIdAndData = MultimapBuilder.hashKeys().linkedListValues().build();

        for (RegeneratorSet regeneratorSet : this.regeneratorSets) {
            Regenerator<Object> regenerator = regeneratorSet.getRegenerator();
            for (Trigger trigger : regenerator.getTriggers()) {
                TriggerRegeneratorPair regeneratorPair = new TriggerRegeneratorPair(trigger, regeneratorSet);
                for (Material material : trigger.getMaterials()) {
                    regeneratorsById.put(material, regeneratorPair);
                }
                for (MaterialData materialData : trigger.getExactMaterials()) {
                    regeneratorsByIdAndData.put(materialData, regeneratorPair);
                }
            }
        }

    }

    public String getName() {
        return name;
    }

    public Iterable<RegeneratorSet> getRegeneratorSets() {
        return regeneratorSets;
    }

    public RegeneratorSet getRegenerator(RegenContext context) {
        Material type = context.getBlock().getType();
        MaterialData data = context.getBlock().getData();
        Iterable<TriggerRegeneratorPair> candidateRegenerators = Iterables.concat(regeneratorsByIdAndData.get(data), regeneratorsById.get(type));
        for (TriggerRegeneratorPair entry : candidateRegenerators) {
            if (entry.trigger.test(context) && entry.regeneratorSet.getRegenerator().validate(context))
                return entry.regeneratorSet;
        }
        return null;
    }

    public boolean isProtected() {
        return protect;
    }

    static class TriggerRegeneratorPair {
        private final Trigger trigger;
        private final RegeneratorSet regeneratorSet;

        public TriggerRegeneratorPair(Trigger trigger, RegeneratorSet regeneratorSet) {
            this.trigger = trigger;
            this.regeneratorSet = regeneratorSet;
        }
    }
}
