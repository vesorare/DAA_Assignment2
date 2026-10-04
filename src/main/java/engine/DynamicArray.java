package engine;

public class DynamicArray {
    private int[] data;
    private int size;

    public DynamicArray() {
        this.data = new int[8];
        this.size = 0;
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                Metrics.steps++;
                Metrics.moves++;
                newData[i] = data[i];
            }
            data = newData;
        }
    }

    public void add(int x) {
        ensureCapacity();
        Metrics.moves++;
        data[size++] = x;
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            Metrics.steps++;
            Metrics.moves++;
            data[i] = data[i - 1];
        }
        Metrics.moves++;
        data[index] = x;
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Metrics.steps++;
        int removedValue = data[index];
        for (int i = index; i < size - 1; i++) {
            Metrics.steps++;
            Metrics.moves++;
            data[i] = data[i + 1];
        }
        size--;
        return removedValue;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Metrics.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            Metrics.steps++;
            Metrics.comparisons++;
            if (data[i] == x) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }
}