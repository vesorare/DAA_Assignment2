package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DynamicArrayTest {
    private DynamicArray array;

    @BeforeEach
    void setUp() {
        array = new DynamicArray();
    }

    @Test
    void testEmptyStructure() {
        assertEquals(0, array.size());
        assertThrows(IndexOutOfBoundsException.class, () -> array.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> array.remove(0));
    }

    @Test
    void testAddAndGet() {
        array.add(10);
        array.add(20);
        assertEquals(2, array.size());
        assertEquals(10, array.get(0));
        assertEquals(20, array.get(1));
    }

    @Test
    void testResizing() {
        for (int i = 0; i < 20; i++) {
            array.add(i);
        }
        assertEquals(20, array.size());
        for (int i = 0; i < 20; i++) {
            assertEquals(i, array.get(i));
        }
    }

    @Test
    void testInsertAtIndices() {
        array.add(10);
        array.add(30);
        array.add(1, 20); // вставка в середину
        array.add(0, 5);  // вставка в начало
        array.add(4, 40); // вставка в конец

        assertEquals(5, array.size());
        assertEquals(5, array.get(0));
        assertEquals(10, array.get(1));
        assertEquals(20, array.get(2));
        assertEquals(30, array.get(3));
        assertEquals(40, array.get(4));
    }

    @Test
    void testRemove() {
        array.add(100);
        array.add(200);
        array.add(300);
        assertEquals(200, array.remove(1));
        assertEquals(2, array.size());
        assertEquals(300, array.get(1));
    }

    @Test
    void testContains() {
        array.add(42);
        array.add(42); // дубликат
        assertTrue(array.contains(42));
        assertFalse(array.contains(99));
    }
}