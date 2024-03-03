public class Graph implements Comparable<Graph> {
    private final int from;
    private final int to;
    private final int value;

    public Graph(int from, int to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    @Override
    public int compareTo(Graph o) {
        return this.value - o.value;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getValue() {
        return value;
    }
}
