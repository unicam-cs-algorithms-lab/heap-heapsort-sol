package it.unicam.cs.asdl2526.es10sol;

/**
 * Semplice implementazione dell'interfaccia {@link PriorityQueueElement},
 * pensata esclusivamente per scopi di test.
 * <p>
 * Gli oggetti di questa classe rappresentano elementi con:
 * <ul>
 *   <li>un contenuto informativo (una stringa);</li>
 *   <li>una priorità numerica (double);</li>
 *   <li>una handle (int), che rappresenta l'indice dell'elemento
 *       all'interno dello heap che implementa la coda con priorità.</li>
 * </ul>
 *
 * <p>
 * Questa classe <b>non</b> contiene alcuna logica di gestione dello heap:
 * si limita a fornire un'implementazione minimale delle API richieste
 * dall'interfaccia {@link PriorityQueueElement}, così da poter testare
 * correttamente il funzionamento della coda con priorità.
 */
public class MySimpleElement implements PriorityQueueElement {

    /**
     * Contenuto informativo dell'elemento.
     * Non viene utilizzato per il confronto delle priorità.
     */
    private String content;

    /**
     * Handle dell'elemento, cioè l'indice che identifica la posizione corrente
     * dell'elemento nello heap che rappresenta la coda con priorità.
     */
    private int index;

    /**
     * Priorità associata all'elemento.
     * Valori più piccoli corrispondono a priorità più alta
     * (min-priority queue).
     */
    private double value;

    /**
     * Crea un nuovo elemento semplice con contenuto, handle iniziale
     * e priorità specificati.
     *
     * @param content contenuto informativo dell'elemento
     * @param index   handle iniziale dell'elemento
     * @param value   priorità iniziale dell'elemento
     */
    public MySimpleElement(String content, int index, double value) {
        this.content = content;
        this.index = index;
        this.value = value;
    }

    /**
     * Restituisce la priorità corrente dell'elemento.
     *
     * @return la priorità corrente
     */
    @Override
    public double getPriority() {
        return value;
    }

    /**
     * Imposta una nuova priorità per questo elemento.
     *
     * @param newPriority il nuovo valore della priorità
     */
    @Override
    public void setPriority(double newPriority) {
        this.value = newPriority;
    }

    /**
     * Restituisce la handle corrente dell'elemento, cioè l'indice
     * che ne rappresenta la posizione nello heap.
     *
     * @return la handle corrente
     */
    @Override
    public int getHandle() {
        return index;
    }

    /**
     * Imposta una nuova handle per questo elemento.
     * <p>
     * Questo metodo viene tipicamente chiamato dalla coda con priorità
     * quando l'elemento viene spostato in una nuova posizione dello heap.
     *
     * @param newHandle il nuovo valore della handle
     */
    @Override
    public void setHandle(int newHandle) {
        index = newHandle;
    }
}