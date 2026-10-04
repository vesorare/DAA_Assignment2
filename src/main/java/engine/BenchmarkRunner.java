package engine;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};

    public static void main(String[] args) throws Exception {
        File dir = new File("results");
        if (!dir.exists()) dir.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                System.out.println("Processing n = " + n + "...");
                runW1(writer, n);
                runW2(writer, n);
                runW3(writer, n, "head");
                runW3(writer, n, "middle");
                runW4(writer, n);
            }
        }
        System.out.println("Benchmark finished. Results saved to results/results.csv");
    }

    private static void runW1(PrintWriter out, int n) {
        // DynamicArray
        long[] times = new long[5];
        long steps = 0, moves = 0, comps = 0;
        for (int run = 0; run < 7; run++) {
            DynamicArray arr = new DynamicArray();
            Random r = new Random(42);
            for (int i = 0; i < n; i++) arr.add(r.nextInt());

            Metrics.reset();
            Random qRnd = new Random(42);
            long start = System.nanoTime();
            for (int k = 0; k < 10000; k++) {
                arr.get(qRnd.nextInt(n));
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W1,-,DynamicArray,%d,%d,%d,%d,%d%n", n, times[2], steps, moves, comps);

        // MyLinkedList
        for (int run = 0; run < 7; run++) {
            MyLinkedList list = new MyLinkedList();
            Random r = new Random(42);
            for (int i = 0; i < n; i++) list.add(r.nextInt());

            Metrics.reset();
            Random qRnd = new Random(42);
            long start = System.nanoTime();
            for (int k = 0; k < 10000; k++) {
                list.get(qRnd.nextInt(n));
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W1,-,MyLinkedList,%d,%d,%d,%d,%d%n", n, times[2], steps, moves, comps);
    }

    private static void runW2(PrintWriter out, int n) {
        long[] times = new long[5];
        long steps = 0, moves = 0, comps = 0;

        // DynamicArray
        for (int run = 0; run < 7; run++) {
            DynamicArray arr = new DynamicArray();
            Random r = new Random(42);
            int[] added = new int[n];
            for (int i = 0; i < n; i++) {
                added[i] = r.nextInt(1_000_000);
                arr.add(added[i]);
            }

            Metrics.reset();
            Random qRnd = new Random(99);
            long start = System.nanoTime();
            for (int k = 0; k < 500; k++) arr.contains(added[qRnd.nextInt(n)]);
            for (int k = 0; k < 500; k++) arr.contains(2_000_000 + qRnd.nextInt(1_000_000));
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W2,-,DynamicArray,%d,%d,%d,%d,%d%n", n, times[2], steps, moves, comps);

        // MyLinkedList
        for (int run = 0; run < 7; run++) {
            MyLinkedList list = new MyLinkedList();
            Random r = new Random(42);
            int[] added = new int[n];
            for (int i = 0; i < n; i++) {
                added[i] = r.nextInt(1_000_000);
                list.add(added[i]);
            }

            Metrics.reset();
            Random qRnd = new Random(99);
            long start = System.nanoTime();
            for (int k = 0; k < 500; k++) list.contains(added[qRnd.nextInt(n)]);
            for (int k = 0; k < 500; k++) list.contains(2_000_000 + qRnd.nextInt(1_000_000));
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W2,-,MyLinkedList,%d,%d,%d,%d,%d%n", n, times[2], steps, moves, comps);
    }

    private static void runW3(PrintWriter out, int n, String variant) {
        long[] times = new long[5];
        long steps = 0, moves = 0, comps = 0;

        // DynamicArray
        for (int run = 0; run < 7; run++) {
            DynamicArray arr = new DynamicArray();
            Random r = new Random(42);
            for (int i = 0; i < n; i++) arr.add(r.nextInt());

            Metrics.reset();
            long start = System.nanoTime();
            for (int k = 0; k < 1000; k++) {
                int idx = variant.equals("head") ? 0 : arr.size() / 2;
                arr.add(idx, k);
            }
            for (int k = 0; k < 1000; k++) {
                int idx = variant.equals("head") ? 0 : arr.size() / 2;
                arr.remove(idx);
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W3,%s,DynamicArray,%d,%d,%d,%d,%d%n", variant, n, times[2], steps, moves, comps);

        // MyLinkedList
        for (int run = 0; run < 7; run++) {
            MyLinkedList list = new MyLinkedList();
            Random r = new Random(42);
            for (int i = 0; i < n; i++) list.add(r.nextInt());

            Metrics.reset();
            long start = System.nanoTime();
            for (int k = 0; k < 1000; k++) {
                int idx = variant.equals("head") ? 0 : list.size() / 2;
                list.add(idx, k);
            }
            for (int k = 0; k < 1000; k++) {
                int idx = variant.equals("head") ? 0 : list.size() / 2;
                list.remove(idx);
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W3,%s,MyLinkedList,%d,%d,%d,%d,%d%n", variant, n, times[2], steps, moves, comps);
    }

    private static void runW4(PrintWriter out, int n) {
        long[] times = new long[5];
        long steps = 0, moves = 0, comps = 0;

        for (int run = 0; run < 7; run++) {
            MinHeap heap = new MinHeap();
            Random r = new Random(42);

            Metrics.reset();
            long start = System.nanoTime();
            for (int i = 0; i < n; i++) heap.insert(r.nextInt());
            int prev = Integer.MIN_VALUE;
            for (int i = 0; i < n; i++) {
                int val = heap.extractMin();
                if (val < prev) throw new IllegalStateException("Non-decreasing order violated");
                prev = val;
            }
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            if (run >= 2) {
                times[run - 2] = elapsed;
                steps = Metrics.steps; moves = Metrics.moves; comps = Metrics.comparisons;
            }
        }
        Arrays.sort(times);
        out.printf("W4,-,MinHeap,%d,%d,%d,%d,%d%n", n, times[2], steps, moves, comps);
    }
}