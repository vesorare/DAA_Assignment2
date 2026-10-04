package engine;

public class MyLinkedList {
    private static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    public MyLinkedList() {
        this.head = null;
        this.size = 0;
    }

    public void add(int x) {
        add(size, x);
    }

    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node newNode = new Node(x);
        if (index == 0) {
            newNode.next = head;
            Metrics.moves++;
            head = newNode;
        } else {
            Node curr = head;
            for (int i = 0; i < index - 1; i++) {
                Metrics.steps++;
                curr = curr.next;
            }
            Metrics.steps++;
            newNode.next = curr.next;
            curr.next = newNode;
            Metrics.moves += 2;
        }
        size++;
    }

    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        int removedVal;
        if (index == 0) {
            Metrics.steps++;
            removedVal = head.val;
            head = head.next;
            Metrics.moves++;
        } else {
            Node curr = head;
            for (int i = 0; i < index - 1; i++) {
                Metrics.steps++;
                curr = curr.next;
            }
            Metrics.steps += 2;
            removedVal = curr.next.val;
            curr.next = curr.next.next;
            Metrics.moves++;
        }
        size--;
        return removedVal;
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node curr = head;
        for (int i = 0; i < index; i++) {
            Metrics.steps++;
            curr = curr.next;
        }
        Metrics.steps++;
        return curr.val;
    }

    public boolean contains(int x) {
        Node curr = head;
        while (curr != null) {
            Metrics.steps++;
            Metrics.comparisons++;
            if (curr.val == x) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    public int size() {
        return size;
    }
}