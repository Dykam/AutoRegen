package nl.dykam.dev.autoregen;

import nl.dykam.dev.autoregen.regenerators.Regenerator;

public class RegeneratorSet {
    private final TimeRules timeRules;
    private final Regenerator regenerator;

    public RegeneratorSet(TimeRules timeRules, Regenerator regenerator) {
        this.timeRules = timeRules;
        this.regenerator = regenerator;
    }

    public TimeRules getTimeRules() {
        return timeRules;
    }

    public Regenerator getRegenerator() {
        return regenerator;
    }
}
