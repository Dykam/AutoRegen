package nl.dykam.dev.autoregen;

public class TimeRules {
    public static final TimeRules INSTANT = new TimeRules(0, TimeRuleType.DELAY);
    //    private Threshold threshold;
    private long time;
    private TimeRuleType type;

    public TimeRules(/*Threshold threshold, */long time, TimeRuleType type) {
//        this.threshold = threshold;
        this.time = time;
        this.type = type;
    }

//    public Threshold getThreshold() {
//        return threshold;
//    }
//
//    public void setThreshold(Threshold thresHold) {
//        this.threshold = thresHold;
//    }

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public TimeRuleType getType() {
        return type;
    }

    public void setType(TimeRuleType type) {
        this.type = type;
    }
}
