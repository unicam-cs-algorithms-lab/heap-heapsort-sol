package it.unicam.cs.asdl2526.es10sol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Test minimali per {@link BinaryHeapMinPriorityQueue}.
 *
 * Obiettivo: verificare le funzionalità di base (insert, minimum, extractMinimum,
 * decreasePriority, size/isEmpty/clear) e le eccezioni richieste dalle API.
 *
 * Nota: non si assume alcuna stabilità a parità di priorità (uno heap non è stabile).
 */
class BinaryHeapMinPriorityQueueTest {

    @Test
    final void testConstructorAndClear() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());

        heap.insert(new MySimpleElement("e0", -1, 13));
        heap.insert(new MySimpleElement("e1", -1, 7));
        heap.insert(new MySimpleElement("e2", -1, 8));

        assertFalse(heap.isEmpty());
        assertEquals(3, heap.size());

        heap.clear();
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    final void testInsertNull() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();
        assertThrows(NullPointerException.class, () -> heap.insert(null));
    }

    @Test
    final void testMinimumOnEmpty() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();
        assertThrows(NoSuchElementException.class, () -> heap.minimum());
    }

    @Test
    final void testExtractMinimumOnEmpty() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();
        assertThrows(NoSuchElementException.class, () -> heap.extractMinimum());
    }

    @Test
    final void testMinimumDoesNotRemove() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 13);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 7);
        PriorityQueueElement e2 = new MySimpleElement("e2", -1, 8);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);

        assertEquals(3, heap.size());
        assertSame(e1, heap.minimum());
        assertEquals(3, heap.size());
        assertSame(e1, heap.minimum());
        assertEquals(3, heap.size());
    }

    @Test
    final void testExtractMinimumOrderOnDistinctPriorities() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 13);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 7);
        PriorityQueueElement e2 = new MySimpleElement("e2", -1, 8);
        PriorityQueueElement e3 = new MySimpleElement("e3", -1, 9);
        PriorityQueueElement e4 = new MySimpleElement("e4", -1, 20);
        PriorityQueueElement e5 = new MySimpleElement("e5", -1, 21);
        PriorityQueueElement e6 = new MySimpleElement("e6", -1, 44);
        PriorityQueueElement e7 = new MySimpleElement("e7", -1, 1);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);
        heap.insert(e3);
        heap.insert(e4);
        heap.insert(e5);
        heap.insert(e6);
        heap.insert(e7);

        assertEquals(8, heap.size());

        assertSame(e7, heap.extractMinimum());
        assertSame(e1, heap.extractMinimum());
        assertSame(e2, heap.extractMinimum());
        assertSame(e3, heap.extractMinimum());
        assertSame(e0, heap.extractMinimum());
        assertSame(e4, heap.extractMinimum());
        assertSame(e5, heap.extractMinimum());
        assertSame(e6, heap.extractMinimum());

        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    final void testDecreasePriorityExceptionsAndEffect() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 13);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 7);
        PriorityQueueElement e2 = new MySimpleElement("e2", -1, 8);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);

        // newPriority non strettamente minore -> IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> heap.decreasePriority(e0, 13));
        assertThrows(IllegalArgumentException.class, () -> heap.decreasePriority(e0, 14));

        // elemento non presente -> NoSuchElementException
        PriorityQueueElement notInHeap = new MySimpleElement("X", -1, 100);
        assertThrows(NoSuchElementException.class, () -> heap.decreasePriority(notInHeap, 1));

        // caso valido: abbasso e0 sotto tutti -> e0 diventa minimo
        heap.decreasePriority(e0, 1.5);
        assertEquals(1.5, e0.getPriority());
        assertSame(e0, heap.minimum());

        // size invariata
        assertEquals(3, heap.size());
    }

    @Test
    final void testHandleIsSetToValidIndexAfterInsert() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        MySimpleElement e0 = new MySimpleElement("e0", -1, 13);
        MySimpleElement e1 = new MySimpleElement("e1", -1, 7);
        MySimpleElement e2 = new MySimpleElement("e2", -1, 8);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);

        int n = heap.size();
        assertTrue(e0.getHandle() >= 0 && e0.getHandle() < n);
        assertTrue(e1.getHandle() >= 0 && e1.getHandle() < n);
        assertTrue(e2.getHandle() >= 0 && e2.getHandle() < n);
    }

    @Test
    final void testSingleElementBehavior() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e = new MySimpleElement("single", -1, 50);
        heap.insert(e);

        assertFalse(heap.isEmpty());
        assertEquals(1, heap.size());

        assertSame(e, heap.minimum());
        assertSame(e, heap.extractMinimum());

        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    final void testReinsertAfterExtraction() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement a = new MySimpleElement("A", -1, 20);
        PriorityQueueElement b = new MySimpleElement("B", -1, 10);
        PriorityQueueElement c = new MySimpleElement("C", -1, 30);

        heap.insert(a);
        heap.insert(b);
        heap.insert(c);

        assertSame(b, heap.extractMinimum());

        PriorityQueueElement d = new MySimpleElement("D", -1, 5);
        heap.insert(d);

        assertSame(d, heap.extractMinimum());
        assertSame(a, heap.extractMinimum());
        assertSame(c, heap.extractMinimum());
        assertTrue(heap.isEmpty());
    }
}