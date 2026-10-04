package engine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {
    private MyLinkedList list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList();
    }

    @Test
    void testEmptyList() {
        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void testAddAndGet() {
        list.add(10);
        list.add(20);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    void testInsertions() {
        list.add(100);
        list.add(0, 50); // голова
        list.add(2, 200); // хвост
        list.add(1, 75); // середина
        assertEquals(4, list.size());
        assertEquals(50, list.get(0));
        assertEquals(75, list.get(1));
        assertEquals(100, list.get(2));
        assertEquals(200, list.get(3));
    }

    @Test
    void testRemovals() {
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.remove(0));
        assertEquals(3, list.remove(1));
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void testContains() {
        list.add(5);
        list.add(5); // дубликат
        assertTrue(list.contains(5));
        assertFalse(list.contains(10));
    }
}