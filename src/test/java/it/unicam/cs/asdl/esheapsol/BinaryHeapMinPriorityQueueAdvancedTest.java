package it.unicam.cs.asdl.esheapsol;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Test aggiuntivi avanzati che confrontano il comportamento della
 * BinaryHeapMinPriorityQueue con una PriorityQueue di Java.
 */
class BinaryHeapMinPriorityQueueAdvancedTest {

    @Test
    void testRandomScenarioAgainstJavaPriorityQueue() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        // PriorityQueue Java che usa la stessa nozione di priorità (minimo = più piccolo double)
        PriorityQueue<MySimpleElement> oracle =
                new PriorityQueue<MySimpleElement>(11, new Comparator<MySimpleElement>() {
                    @Override
                    public int compare(MySimpleElement o1, MySimpleElement o2) {
                        return Double.compare(o1.getPriority(), o2.getPriority());
                    }
                });

        int n = 100;
        MySimpleElement[] elements = new MySimpleElement[n];
        boolean[] inHeap = new boolean[n];

        // Costruiamo qualche priorità "variegata" ma deterministica
        for (int i = 0; i < n; i++) {
            double priority = (i * 37) %
                    53; // funzione pseudo-"random" ma deterministica
            elements[i] = new MySimpleElement("e" + i, -1, priority);
            heap.insert(elements[i]);
            oracle.add(elements[i]);
            inHeap[i] = true;
        }

        assertEquals(n, heap.size());
        assertEquals(n, oracle.size());

        Random rnd = new Random(123456); // seed fisso per riproducibilità

        int operations = 200;
        for (int op = 0; op < operations && !heap.isEmpty(); op++) {
            // circa 1/3 delle operazioni: decreasePriority
            boolean doDecrease = (op % 3 == 0);

            if (doDecrease) {
                // sceglie un elemento ancora presente
                int idx = findAnyInHeap(inHeap, rnd);
                if (idx == -1) {
                    // se nessun elemento è rimasto (dovrebbe coincidere con heap vuota)
                    assertTrue(heap.isEmpty());
                    assertTrue(oracle.isEmpty());
                    break;
                }
                MySimpleElement e = elements[idx];

                // Nuova priorità più piccola (per rispettare il contratto)
                double oldPriority = e.getPriority();
                double newPriority = oldPriority - 1.0;

                // Per l'oracolo: remove + setPriority + add
                boolean removed = oracle.remove(e);
                assertTrue(removed);

                heap.decreasePriority(e, newPriority);
                // decreasePriority modifica già la priority dell'elemento
                oracle.add(e);

                // Confronto il minimo tra le due strutture:
                // confrontiamo la priorità minima (non la reference), e verifichiamo
                // che l'elemento restituito dall'heap abbia la priorità attesa.
                if (!heap.isEmpty()) {
                    PriorityQueueElement heapMin = heap.minimum();
                    MySimpleElement oracleMin = oracle.peek();

                    // 1) priorità uguale (tolleranza numerica minima)
                    assertEquals(oracleMin.getPriority(), heapMin.getPriority(), 1e-12);

                    // 2) l'elemento restituito dall'heap deve essere uno degli elementi
                    //    ancora presenti (cioè una delle entries in 'elements' con inHeap==true)
                    boolean found = false;
                    for (int i = 0; i < n; i++) {
                        if (inHeap[i] && elements[i] == heapMin) {
                            found = true;
                            break;
                        }
                    }
                    assertTrue(found, "heapMin non appartiene all'insieme degli elementi ancora presenti");
                }
            } else {
                // Operazione di estrazione del minimo
                if (heap.isEmpty()) {
                    assertTrue(oracle.isEmpty());
                    break;
                }

                PriorityQueueElement heapMin = heap.extractMinimum();
                assertNotNull(heapMin);

                // Rimuovo la stessa reference dall'oracolo: questo mantiene
                // sincronizzati oracle, elements[] e inHeap[]
                boolean removedFromOracle = oracle.remove(heapMin);
                assertTrue(removedFromOracle, "L'elemento estratto dall'heap non era presente nell'oracolo");

                // Controllo che la priorità sia coerente con l'oracolo (opzionale/utile)
                double removedPriority = heapMin.getPriority();
                // Non possiamo usare oracle.peek() qui perché abbiamo già rimosso l'elemento corrispondente.

                // Aggiorna inHeap (trova l'indice dell'oggetto estratto)
                int idx = findIndexOf(elements, (MySimpleElement) heapMin);
                assertTrue(idx >= 0);
                inHeap[idx] = false;
            }
        }

        // Alla fine le due strutture devono essere entrambe vuote
        while (!heap.isEmpty()) {
            PriorityQueueElement heapMin = heap.extractMinimum();
            assertTrue(oracle.remove(heapMin), "L'elemento estratto dall'heap non era presente nell'oracolo");
            int idx = findIndexOf(elements, (MySimpleElement) heapMin);
            assertTrue(idx >= 0);
            inHeap[idx] = false;
        }
        assertTrue(heap.isEmpty());
        assertTrue(oracle.isEmpty());
    }

    @Test
    void testMinimumAndExtractOnEmptyConsistency() {
        BinaryHeapMinPriorityQueue heap = new BinaryHeapMinPriorityQueue();

        // Nessun inserimento
        assertThrows(NoSuchElementException.class, new Runnable() {
            @Override
            public void run() {
                heap.minimum();
            }
        }::run);

        assertThrows(NoSuchElementException.class, new Runnable() {
            @Override
            public void run() {
                heap.extractMinimum();
            }
        }::run);
    }

    // Trova un indice i tale che inHeap[i] == true, scegliendolo "a caso"
    private int findAnyInHeap(boolean[] inHeap, Random rnd) {
        int count = 0;
        for (int i = 0; i < inHeap.length; i++) {
            if (inHeap[i]) {
                count++;
            }
        }
        if (count == 0) {
            return -1;
        }
        int target = rnd.nextInt(count);
        int seen = 0;
        for (int i = 0; i < inHeap.length; i++) {
            if (inHeap[i]) {
                if (seen == target) {
                    return i;
                }
                seen++;
            }
        }
        return -1;
    }

    // Trova l'indice di e nel vettore elements (per riferimento, non equals)
    private int findIndexOf(MySimpleElement[] elements, MySimpleElement e) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == e) {
                return i;
            }
        }
        return -1;
    }
}