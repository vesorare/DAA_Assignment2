package engine;

public class Metrics {
    public static long steps = 0;
    public static long moves = 0;
    public static long comparisons = 0;

    public static void reset() {
        steps = 0;
        moves = 0;
        comparisons = 0;
    }
}