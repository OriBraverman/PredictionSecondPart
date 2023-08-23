package world.factors.property.definition.api;

public class Range<T> {
    private final T to;
    private final T from;

    public Range(T to, T from) {
        this.to = to;
        this.from = from;
    }

    public T getTo() {
        return to;
    }

    public T getFrom() {
        return from;
    }
}
