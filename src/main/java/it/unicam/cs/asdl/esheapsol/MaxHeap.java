package it.unicam.cs.asdl.esheapsol;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione di un max-heap binario mediante ArrayList.
 * La radice contiene un elemento massimo; elementi uguali sono ammessi.
 * Le operazioni di inserimento ed estrazione ripristinano la proprieta'
 * di heap risalendo o scendendo lungo un cammino dell'albero.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * ArrayList usata come rappresentazione indicizzata dell'albero quasi completo. La radice ha indice 0, diversamente dalle slide teoriche con indice 1.
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce un max-heap a partire dagli elementi di una lista.
     * <p>
     * La costruzione può avvenire in due modi: inserendo gli elementi uno alla
     * volta (costo O(n log n) nel caso peggiore), oppure copiandoli nella
     * rappresentazione e applicando heapify dal basso verso l'alto (costo O(n)).
     * Nel secondo caso quasi tutti i nodi sono vicini alle foglie, perciò
     * richiedono discese molto brevi: la somma dei costi è lineare.
     * <p>
     * In questa esercitazione si adotta la strategia degli inserimenti
     * successivi; la costruzione bottom-up compare nella prima fase di HeapSort.
     * Si veda il README per la spiegazione della complessità.
     *
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        if (list == null)
            throw new NullPointerException(
                    "Creazione di uno heap da una lista nulla");
        this.heap = new ArrayList<E>();
        // Costruzione per inserimenti successivi: ogni insert può risalire
        // lungo un cammino di altezza O(log n). Su n elementi il limite
        // complessivo è O(n log n), diversamente da Build-Max-Heap bottom-up.
        for (E el : list)
            this.insert(el);
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        if (el == null)
            throw new NullPointerException(
                    "Tentativo di inserire un elemento null");
        if (this.heap.isEmpty())
            // inserisco nella posizione 0
            this.heap.add(el);
        else {
            // inserisco nella prima posizione libera e riadatto
            this.heap.add(el);
            moveUp(this.heap.size() - 1);
        }
    }

    private void moveUp(int indice) {
        // ciclo di scambio col padre fino a quando la proprietà dello heap
        // non è soddisfatta o fino a quando non arrivo alla radice
        while (indice > 0 && this.heap.get(parentIndex(indice))
                .compareTo(this.heap.get(indice)) < 0) {
            // scambio
            E appoggio = this.heap.get(indice);
            this.heap.set(indice, this.heap.get(parentIndex(indice)));
            this.heap.set(parentIndex(indice), appoggio);
            // vado avanti
            indice = parentIndex(indice);
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return 2 * i + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return 2 * i + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        // sfruttiamo il fatto che la divisione intera ha sempre come risultato
        // la parte intera della divisione floating point
        return (i - 1) / 2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if (this.isEmpty())
            return null;
        return this.heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        if (this.isEmpty())
            return null;
        E ret = this.heap.get(0);
        if (this.heap.size() == 1)
            this.heap.clear();
        else {
            // sposto l'ultima foglia nella radice, riduco di 1 la lunghezza e
            // riadatto
            this.heap.set(0, this.heap.get(this.heap.size() - 1));
            this.heap.remove(this.heap.size() - 1);
            this.heapify(0);
        }
        return ret;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        if (!hasLeft(i))
            return;
        int max = i;
        if (this.heap.get(max).compareTo(this.heap.get(leftIndex(i))) < 0)
            max = leftIndex(i);
        if (hasRight(i) && this.heap.get(max)
                .compareTo(this.heap.get(rightIndex(i))) < 0)
            max = rightIndex(i);
        if (max == i)
            return; // ho finito
        // scambio i con max e richiamo la funzione ricorsivamente sull'indice
        // del nodo figlio uguale a max
        E app = this.heap.get(i);
        this.heap.set(i, this.heap.get(max));
        this.heap.set(max, app);
        heapify(max);
    }

    private boolean hasRight(int i) {
        return rightIndex(i) < this.heap.size();
    }

    private boolean hasLeft(int i) {
        return leftIndex(i) < this.heap.size();
    }

    /**
     * Accesso protetto alla rappresentazione interna per i test didattici. Non e' una copia: non modificarla dall'esterno, perche' si puo' violare l'invariante di heap.
     * 
     * @return la lista interna (riferimento condiviso)
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
