package it.unicam.cs.asdl.esheapsol;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.NoSuchElementException;

/** Verifica invarianti di rappresentazione e comportamento dopo le rimozioni. */
class PriorityQueueInvariantiTest {
    @Test
    void handlesDopoScambiEdEstrazioni() {
        BinaryHeapMinPriorityQueue q = new BinaryHeapMinPriorityQueue();
        MySimpleElement a = new MySimpleElement("a", -1, 8);
        MySimpleElement b = new MySimpleElement("b", -1, 3);
        MySimpleElement c = new MySimpleElement("c", -1, 5);
        q.insert(a); q.insert(b); q.insert(c);
        // La risalita di b modifica gli indici sia di b sia di a.
        assertEquals(0, b.getHandle());
        assertEquals(1, a.getHandle());
        assertSame(b, q.extractMinimum());
        // Le handle degli elementi rimasti devono ancora indicare posizioni valide.
        assertTrue(a.getHandle() >= 0 && a.getHandle() < q.size());
        assertTrue(c.getHandle() >= 0 && c.getHandle() < q.size());
        q.decreasePriority(a, 1);
        assertSame(a, q.minimum());
    }

    @Test
    void handleNonCoerenteNonPermetteAggiornamento() {
        BinaryHeapMinPriorityQueue q = new BinaryHeapMinPriorityQueue();
        MySimpleElement presente = new MySimpleElement("p", -1, 4);
        MySimpleElement esterno = new MySimpleElement("e", 0, 8);
        q.insert(presente);
        // L'indice 0 è valido, ma in quella posizione c'è un altro oggetto.
        assertThrows(NoSuchElementException.class, () -> q.decreasePriority(esterno, 2));
    }
}
