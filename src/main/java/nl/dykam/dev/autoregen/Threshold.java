package nl.dykam.dev.autoregen;

public class Threshold {
    private float amount;
    private Type type;

    public Threshold(float amount, Type type) {
        this.amount = amount;
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        ABSOLUTE,
        PERCENTAGE
    }
}
