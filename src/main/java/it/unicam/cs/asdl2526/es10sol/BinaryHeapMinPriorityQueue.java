package it.unicam.cs.asdl2526.es10sol;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Implementazione di una coda con priorità minima (min-priority queue)
 * tramite heap binario.
 * <p>
 * Gli elementi inseriti devono implementare {@link PriorityQueueElement},
 * che fornisce l'accesso alla priorità e alla handle (indice nello heap).
 * <p>La handle consente di raggiungere un elemento in O(1) per eseguire
 * {@link #decreasePriority(PriorityQueueElement, double)} senza scansioni lineari.
 * Ogni scambio aggiorna entrambe le handle: la successiva risalita costa O(log n).
 * <p>La stessa istanza non può essere inserita due volte e le priorità devono essere finite.
 * Lo heap è rappresentato tramite una {@link ArrayList} usando tutte le
 * posizioni (la radice è in posizione 0).
 */
public class BinaryHeapMinPriorityQueue {

    /*
     * ArrayList che rappresenta lo heap binario.
     * Per un elemento in posizione i:
     * - padre: (i - 1) / 2
     * - figlio sinistro: 2*i + 1
     * - figlio destro: 2*i + 2
     */
    private ArrayList<PriorityQueueElement> heap;

    /**
     * Crea una coda con priorità minima vuota.
     */
    public BinaryHeapMinPriorityQueue() {
        this.heap = new ArrayList<PriorityQueueElement>();
    }

    /**
     * Inserisce un elemento in questa coda con priorità minima.
     * <p>
     * L'elemento viene inizialmente inserito in fondo allo heap e poi
     * fatto risalire fino a ripristinare la proprietà di min-heap.
     * La handle dell'elemento viene aggiornata coerentemente.
     *
     * @param element l'elemento da inserire
     * @throws NullPointerException se l'elemento è null
     */
    public void insert(PriorityQueueElement element) {
        if (element == null) {
            throw new NullPointerException("Cannot insert null element");
        }
        // Una stessa istanza non può occupare due posizioni: avrebbe una sola handle.
        int oldHandle = element.getHandle();
        if (oldHandle >= 0 && oldHandle < heap.size() && heap.get(oldHandle) == element)
            throw new IllegalArgumentException("Elemento già presente nella coda");
        if (!Double.isFinite(element.getPriority()))
            throw new IllegalArgumentException("La priorità deve essere finita");
        heap.add(element);
        int idx = heap.size() - 1;
        element.setHandle(idx);
        heapifyUp(idx);
    }

    /**
     * Ripristina la proprietà di heap facendo risalire l'elemento
     * in posizione {@code i} verso la radice.
     *
     * @param i indice dell'elemento da far risalire
     */
    private void heapifyUp(int i) {
        while (i > 0) {
            int p = parent(i);
            if (heap.get(i).getPriority() < heap.get(p).getPriority()) {
                swap(i, p);
                i = p;
            } else {
                break;
            }
        }
    }

    /**
     * Restituisce l'indice del padre del nodo in posizione {@code i}.
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Restituisce l'indice del figlio sinistro del nodo in posizione {@code i}.
     */
    private int left(int i) {
        return (i * 2) + 1;
    }

    /**
     * Restituisce l'indice del figlio destro del nodo in posizione {@code i}.
     */
    private int right(int i) {
        return (i * 2) + 2;
    }

    /**
     * Scambia due elementi dello heap aggiornando correttamente
     * anche le rispettive handle.
     *
     * @param i indice del primo elemento
     * @param j indice del secondo elemento
     */
    private void swap(int i, int j) {
        if (i == j)
            return;
        PriorityQueueElement ei = heap.get(i);
        PriorityQueueElement ej = heap.get(j);
        ei.setHandle(j);
        ej.setHandle(i);
        heap.set(i, ej);
        heap.set(j, ei);
    }

    /**
     * Restituisce (senza estrarlo) l'elemento minimo presente nella coda.
     *
     * @return l'elemento con priorità minima
     * @throws NoSuchElementException se la coda è vuota
     */
    public PriorityQueueElement minimum() {
        if (this.heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        return heap.get(0);
    }

    /**
     * Estrae e restituisce l'elemento minimo dalla coda.
     * <p>
     * Dopo l'estrazione, la proprietà di heap viene ripristinata
     * facendo scendere l'elemento in radice.
     *
     * @return l'elemento minimo
     * @throws NoSuchElementException se la coda è vuota
     */
    public PriorityQueueElement extractMinimum() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        PriorityQueueElement min = heap.get(0);
        int last = heap.size() - 1;
        swap(0, last);
        heap.remove(last);
        min.setHandle(-1); // L’elemento estratto non appartiene più allo heap.
        if (!heap.isEmpty()) {
            heapifyDown(0);
        }
        return min;
    }

    /**
     * Ripristina la proprietà di heap facendo scendere l'elemento
     * in posizione {@code i} verso le foglie.
     *
     * @param i indice dell'elemento da far scendere
     */
    private void heapifyDown(int i) {
        while (true) {
            int l = left(i);
            int r = right(i);
            int smallest = i;

            if (l < heap.size() &&
                    heap.get(l).getPriority() < heap.get(smallest).getPriority()) {
                smallest = l;
            }
            if (r < heap.size() &&
                    heap.get(r).getPriority() < heap.get(smallest).getPriority()) {
                smallest = r;
            }
            if (smallest != i) {
                swap(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    /**
     * Diminuisce la priorità di un elemento già presente nella coda.
     * <p>
     * L'elemento viene individuato tramite la sua handle (accesso O(1))
     * e poi fatto risalire per ripristinare la proprietà di heap.
     *
     * @param element elemento di cui diminuire la priorità
     * @param newPriority nuova priorità (strettamente minore di quella corrente)
     * @throws NullPointerException se l'elemento è null
     * @throws NoSuchElementException se l'elemento non è presente nella coda
     * @throws IllegalArgumentException se la nuova priorità non è valida
     */
    public void decreasePriority(PriorityQueueElement element, double newPriority) {
        if (element == null) {
            throw new NullPointerException("Cannot decrease null element");
        }
        int handle = element.getHandle();
        if (handle < 0 || handle >= heap.size()) {
            throw new NoSuchElementException(
                    "Element does not appear to be in the heap (invalid handle)");
        }
        if (heap.get(handle) != element) {
            throw new NoSuchElementException(
                    "Element's handle does not match its position in the heap");
        }
        double current = element.getPriority();
        if (!Double.isFinite(newPriority) || !(newPriority < current)) {
            throw new IllegalArgumentException(
                    "newPriority must be strictly less than current priority");
        }
        element.setPriority(newPriority);
        heapifyUp(handle);
    }

    /**
     * Verifica se la coda con priorità è vuota.
     * @return {@code true} se la coda è vuota, {@code false} altrimenti
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Restituisce il numero di elementi presenti nella coda.
     * @return il numero di elementi presenti
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Rimuove tutti gli elementi dalla coda.
     */
    public void clear() {
        // Le handle non devono più indicare posizioni nella vecchia coda.
        for (PriorityQueueElement element : heap) element.setHandle(-1);
        this.heap.clear();
    }
}