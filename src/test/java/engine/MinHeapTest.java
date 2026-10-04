package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {
    private MinHeap heap;

    @BeforeEach
    void setUp() {
        heap = new MinHeap();
    }

    @Test
    void testEmptyHeapThrows() {
        assertThrows(IllegalStateException.class, () -> heap.peekMin());
        assertThrows(IllegalStateException.class, () -> heap.extractMin());
    }

    @Test
    void testHeapPropertyHolds() {
        Random rnd = new Random(42);
        for (int i = 0; i < 50; i++) {
            heap.insert(rnd.nextInt(500));
            assertHeapProperty(heap);
        }

        while (heap.size() > 0) {
            heap.extractMin();
            assertHeapProperty(heap);
        }
    }

    @Test
    void testSortedExtraction() {
        int[] vals = {50, 10, 80, 2, 100, 30, 2};
        for (int v : vals) heap.insert(v);

        int prev = Integer.MIN_VALUE;
        while (heap.size() > 0) {
            int current = heap.extractMin();
            assertTrue(current >= prev);
            prev = current;
        }
    }

    private void assertHeapProperty(MinHeap h) {
        int[] arr = h.getInternalArray();
        for (int i = 0; i < arr.length; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < arr.length) {
                assertTrue(arr[i] <= arr[left], "Heap property violated at left child");
            }
            if (right < arr.length) {
                assertTrue(arr[i] <= arr[right], "Heap property violated at right child");
            }
        }
    }
}