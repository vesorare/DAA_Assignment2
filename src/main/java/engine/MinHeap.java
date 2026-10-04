package engine;

public class MinHeap {
    private int[] heap;
    private int size;

    public MinHeap() {
        this.heap = new int[16];
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            for (int i = 0; i < size; i++) {
                Metrics.steps++;
                Metrics.moves++;
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }

    public void insert(int x) {
        ensureCapacity();
        heap[size] = x;
        Metrics.moves++;
        bubbleUp(size);
        size++;
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        Metrics.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }
        Metrics.steps++;
        int min = heap[0];
        heap[0] = heap[size - 1];
        Metrics.moves++;
        size--;
        if (size > 0) {
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            Metrics.steps += 2;
            Metrics.comparisons++;
            if (heap[index] < heap[parent]) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void bubbleDown(int index) {
        while (2 * index + 1 < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = left;

            if (right < size) {
                Metrics.steps += 2;
                Metrics.comparisons++;
                if (heap[right] < heap[left]) {
                    smallest = right;
                }
            }

            Metrics.steps += 2;
            Metrics.comparisons++;
            if (heap[smallest] < heap[index]) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Metrics.steps += 2;
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        Metrics.moves += 2;
    }

    public int size() {
        return size;
    }

    public int[] getInternalArray() {
        int[] copy = new int[size];
        System.arraycopy(heap, 0, copy, 0, size);
        return copy;
    }

    public static MinHeap buildHeap(int[] array) {
        MinHeap h = new MinHeap();
        h.heap = new int[Math.max(array.length, 16)];
        System.arraycopy(array, 0, h.heap, 0, array.length);
        h.size = array.length;
        Metrics.moves += array.length;

        for (int i = (h.size / 2) - 1; i >= 0; i--) {
            h.bubbleDown(i);
        }
        return h;
    }
}