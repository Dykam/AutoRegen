package nl.dykam.dev.autoregen.regenerators;

import nl.dykam.dev.autoregen.RegenContext;

import java.util.Set;

public interface Regenerator<T extends Object> {
    public Set<Trigger> getTriggers();

    public boolean validate(RegenContext context);

    /**
     * In case of e.g. automatic tree chopping, break down the entire tree
     *
     * @param context
     */
    public T breakdown(RegenContext context);

    public void regenerate(RegenContext context, T breakdownData);
}
