package it.unicam.cs.asdl.esheapsol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Test aggiuntivi per verificare la consistenza delle handle
 * dopo molte operazioni di insert ed extract.
 */
class BinaryHeapMinPriorityQueueHandleTest {

    @Test
    void testHandlesConsistencyAfterManyOperations() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        int n = 50;
        MySimpleElement[] elements = new MySimpleElement[n];

        for (int i = 0; i < n; i++) {
            // handle iniziale -1, sarà impostata dall'heap
            elements[i] = new MySimpleElement("e" + i, -1, i);
            heap.insert(elements[i]);
        }

        assertEquals(n, heap.size());

        // Estraggo un certo numero di elementi,
        // tracciando quali sono stati estratti.
        boolean[] extracted = new boolean[n];
        int extractions = 20;

        for (int k = 0; k < extractions; k++) {
            PriorityQueueElement min = heap.extractMinimum();
            assertNotNull(min);

            int foundIndex = -1;
            for (int i = 0; i < n; i++) {
                if (elements[i] == min) {
                    foundIndex = i;
                    break;
                }
            }
            assertTrue(foundIndex >= 0, "Elemento estratto non trovato nel vettore di riferimento");
            extracted[foundIndex] = true;
        }

        int remaining = heap.size();
        assertEquals(n - extractions, remaining);

        // Controllo che le handle degli elementi rimasti:
        // - siano tutte in [0, remaining - 1]
        // - non abbiano duplicati
        Set<Integer> handles = new HashSet<Integer>();
        for (int i = 0; i < n; i++) {
            if (!extracted[i]) {
                int h = elements[i].getHandle();
                assertTrue(h >= 0 && h < remaining,
                        "Handle fuori dall'intervallo valido: " + h + " (remaining = " + remaining + ")");
                handles.add(h);
            }
        }

        assertEquals(remaining, handles.size(),
                "Le handle degli elementi rimasti non coprono esattamente tutte le posizioni [0.." + (remaining - 1) + "]");

        for (int h = 0; h < remaining; h++) {
            assertTrue(handles.contains(h),
                    "Manca la handle " + h + " tra gli elementi rimasti nello heap");
        }
    }
}