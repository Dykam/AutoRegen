package nl.dykam.dev.autoregen.util;

public class TitleSettings {
    private final int duration;
    private final int ticksFadeIn;
    private final int ticksFadeOut;

    public TitleSettings(int duration) {
        this(duration, 0);
    }

    public TitleSettings(int duration, int ticksFadeIn) {
        this(duration, ticksFadeIn, ticksFadeIn);
    }

    public TitleSettings(int duration, int ticksFadeIn, int ticksFadeOut) {
        this.duration = duration;
        this.ticksFadeIn = ticksFadeIn;
        this.ticksFadeOut = ticksFadeOut;
    }

    public int getDuration() {
        return duration;
    }

    public int getTicksFadeIn() {
        return ticksFadeIn;
    }

    public int getTicksFadeOut() {
        return ticksFadeOut;
    }
}
