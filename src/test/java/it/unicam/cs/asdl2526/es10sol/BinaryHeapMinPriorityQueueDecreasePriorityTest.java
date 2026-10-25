package it.unicam.cs.asdl2526.es10sol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Test aggiuntivi avanzati per il metodo decreasePriority.
 */
class BinaryHeapMinPriorityQueueDecreasePriorityTest {

    @Test
    void testMultipleDecreasesOnSameElement() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 100.0);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 50.0);
        PriorityQueueElement e2 = new MySimpleElement("e2", -1, 20.0);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);

        // Minimo iniziale: e2 (20.0)
        assertSame(e2, heap.minimum());

        // Abbasso e0 sotto e2
        heap.decreasePriority(e0, 10.0);
        assertEquals(10.0, e0.getPriority());
        assertSame(e0, heap.minimum());

        // Ancora più in basso
        heap.decreasePriority(e0, -5.0);
        assertEquals(-5.0, e0.getPriority());
        assertSame(e0, heap.minimum());

        // La dimensione non deve cambiare
        assertEquals(3, heap.size());
    }

    @Test
    void testDecreasePriorityAfterExtractions() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 30.0);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 10.0);
        PriorityQueueElement e2 = new MySimpleElement("e2", -1, 40.0);
        PriorityQueueElement e3 = new MySimpleElement("e3", -1, 25.0);

        heap.insert(e0);
        heap.insert(e1);
        heap.insert(e2);
        heap.insert(e3);

        // Estrai il minimo (e1)
        assertSame(e1, heap.extractMinimum());
        // Ora il minimo dovrebbe essere e3 (25.0) o e0 (30.0) a seconda della struttura,
        // ma per il test non ci interessa quale.

        // Decrease su un elemento interno (per esempio e2)
        heap.decreasePriority(e2, 5.0);
        assertEquals(5.0, e2.getPriority());
        assertSame(e2, heap.minimum());
    }

    @Test
    void testDecreasePriorityOnElementAlreadyExtracted() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 10.0);
        heap.insert(e0);

        // Dopo l'estrazione l'elemento non è più nella coda
        assertSame(e0, heap.extractMinimum());
        assertTrue(heap.isEmpty());

        // decreasePriority su elemento non più presente deve lanciare NoSuchElementException
        assertThrows(NoSuchElementException.class,
                () -> heap.decreasePriority(e0, 1.0));
    }

    @Test
    void testDecreasePriorityWithNegativeValues() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        PriorityQueueElement e0 = new MySimpleElement("e0", -1, 0.0);
        PriorityQueueElement e1 = new MySimpleElement("e1", -1, 10.0);

        heap.insert(e0);
        heap.insert(e1);

        // e0 è minimo
        assertSame(e0, heap.minimum());

        // Decrease e1 a -100
        heap.decreasePriority(e1, -100.0);
        assertEquals(-100.0, e1.getPriority());
        assertSame(e1, heap.minimum());
    }
}