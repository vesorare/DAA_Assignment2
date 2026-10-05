package engine;

import org.openjdk.jol.info.GraphLayout;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BonusRunner {
    private static final int[] SIZES = {100, 1000, 10000, 100000};

    public static void main(String[] args) throws Exception {
        runTaskA_Memory();
        runTaskB_BuildHeap();
    }

    private static void runTaskA_Memory() throws Exception {
        System.out.println("Running Task A: Memory Benchmark...");
        try (PrintWriter out = new PrintWriter(new FileWriter("results/bonus_memory.csv"))) {
            out.println("structure,n,memory_bytes,memory_mb");

            for (int n : SIZES) {
                DynamicArray arr = new DynamicArray();
                MyLinkedList list = new MyLinkedList();
                MinHeap heap = new MinHeap();

                for (int i = 0; i < n; i++) {
                    arr.add(i);
                    list.add(i);
                    heap.insert(i);
                }

                long arrBytes = GraphLayout.parseInstance(arr).totalSize();
                long listBytes = GraphLayout.parseInstance(list).totalSize();
                long heapBytes = GraphLayout.parseInstance(heap).totalSize();

                out.printf(java.util.Locale.US, "DynamicArray,%d,%d,%.6f%n", n, arrBytes, arrBytes / (1024.0 * 1024.0));
                out.printf(java.util.Locale.US, "MyLinkedList,%d,%d,%.6f%n", n, listBytes, listBytes / (1024.0 * 1024.0));
                out.printf(java.util.Locale.US, "MinHeap,%d,%d,%.6f%n", n, heapBytes, heapBytes / (1024.0 * 1024.0));            }
        }
        System.out.println("Memory results saved to results/bonus_memory.csv");
    }

    private static void runTaskB_BuildHeap() throws Exception {
        System.out.println("Running Task B: buildHeap Benchmark...");
        try (PrintWriter out = new PrintWriter(new FileWriter("results/bonus_buildheap.csv"))) {
            out.println("method,n,time_ms,comparisons,moves,steps");

            for (int n : SIZES) {
                int[] data = new int[n];
                Random r = new Random(42);
                for (int i = 0; i < n; i++) data[i] = r.nextInt();

                long[] timesN = new long[5];
                long compsN = 0, movesN = 0, stepsN = 0;
                for (int run = 0; run < 7; run++) {
                    Metrics.reset();
                    long start = System.nanoTime();
                    MinHeap h = new MinHeap();
                    for (int x : data) h.insert(x);
                    long elapsed = (System.nanoTime() - start) / 1_000_000;
                    if (run >= 2) {
                        timesN[run - 2] = elapsed;
                        compsN = Metrics.comparisons;
                        movesN = Metrics.moves;
                        stepsN = Metrics.steps;
                    }
                }
                Arrays.sort(timesN);
                out.printf("n_inserts,%d,%d,%d,%d,%d%n", n, timesN[2], compsN, movesN, stepsN);

                long[] timesFloyd = new long[5];
                long compsF = 0, movesF = 0, stepsF = 0;
                for (int run = 0; run < 7; run++) {
                    Metrics.reset();
                    long start = System.nanoTime();
                    MinHeap h = MinHeap.buildHeap(data);
                    long elapsed = (System.nanoTime() - start) / 1_000_000;
                    if (run >= 2) {
                        timesFloyd[run - 2] = elapsed;
                        compsF = Metrics.comparisons;
                        movesF = Metrics.moves;
                        stepsF = Metrics.steps;
                    }
                }
                Arrays.sort(timesFloyd);
                out.printf("floyd_buildHeap,%d,%d,%d,%d,%d%n", n, timesFloyd[2], compsF, movesF, stepsF);
            }
        }
        System.out.println("BuildHeap results saved to results/bonus_buildheap.csv");
    }
}