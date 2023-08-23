package world.factors.rule;

public class Activation {
    private int ticks = 1;
    private double probabilty = 1;

    public Activation() {}
    public Activation(int ticks) {
        this.ticks = ticks;
    }

    public Activation(double probabilty) {
        this.probabilty = probabilty;
    }

    public Activation(int ticks, double probabilty) {
        this.ticks = ticks;
        this.probabilty = probabilty;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbabilty() {
        return probabilty;
    }
}
