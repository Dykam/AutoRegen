package nl.dykam.dev.autoregen.util;

import org.bukkit.material.MaterialData;

import java.util.*;

public class ChanceList<T> {
    List<Entry> entries;

    public ChanceList() {
        entries = new ArrayList<>();
    }
    public ChanceList(Map<T, Float> entries) {
        this.entries = new ArrayList<>();
        addAll(entries);
    }

    public void add(float weight, T value) {
        entries.add(new Entry(value, weight, weight));
        recalculateWeightFrom(entries.size() - 1);
    }

    public void remove(T value) {
        int index = entries.indexOf(value);
        if(index == -1)
            return;
        entries.remove(index);
        recalculateWeightFrom(index);
    }

    public T pick(Random random) {
        float value = random.nextFloat() * sumOfWeights();
        int index = Collections.binarySearch(entries, value);
        if(index == -1)
            index = entries.size() - 1;
        return entries.get(index).value;
    }

    public float sumOfWeights() {
        return entries.isEmpty() ? 0 : entries.get(entries.size() - 1).cumulativeWeight;
    }

    private void recalculateWeightFrom(int i) {
        float sum = 0;
        if(i > 0)
            sum = entries.get(i - 1).cumulativeWeight + entries.get(i - 1).weight;
        for(; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            entry.cumulativeWeight = sum;
            sum += entry.weight;
        }
    }

    public void addAll(Map<T, Float> entries) {
        for (Map.Entry<T, Float> entry : entries.entrySet()) {
            add(entry.getValue(), entry.getKey());
        }
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    class Entry implements Comparable<Float> {
        public T value;
        public float weight;
        /**
         * Total weight of all entries before this, excluding this
         */
        public float cumulativeWeight;

        public Entry(T value, float weight, float cumulativeWeight) {
            this.value = value;
            this.weight = weight;
            this.cumulativeWeight = cumulativeWeight;
        }

        @Override
        public int compareTo(Float value) {
            if(cumulativeWeight > value)
                return 1;
            if(cumulativeWeight + weight < value)
                return -1;
            return 0;
        }
    }
}
