package it.unicam.cs.asdl2526.es10sol;

/**
 * Interfaccia che consente di inserire oggetti generici in una coda con priorità
 * "dinamica", cioè una coda con priorità in cui la priorità di un elemento può
 * essere aggiornata mentre l'elemento è già presente nella coda.
 * <p>
 * Tipicamente, per supportare l'aggiornamento efficiente della priorità, ad ogni
 * elemento viene associata una <i>handle</i> (maniglia/riferimento) che lo collega
 * direttamente alla sua rappresentazione nella struttura dati che implementa la coda.
 * <p>
 * In questa traccia si assume che la handle sia un <code>int</code> che rappresenta
 * l'indice dell'array (o della {@link java.util.ArrayList}) usato per rappresentare
 * uno heap binario (o, più in generale, uno heap d-ario).
 * <p>
 * Nota importante: la handle deve essere mantenuta coerente con la posizione reale
 * dell'elemento nello heap, cioè deve essere aggiornata ogni volta che l'elemento
 * viene spostato (ad esempio durante risalite/discese o scambi).
 *
 * @author Luca Tesei
 */
public interface PriorityQueueElement {

    /**
     * Restituisce la priorità corrente associata all'elemento.
     *
     * @return la priorità corrente
     */
    double getPriority();

    /**
     * Imposta la priorità dell'elemento ad un nuovo valore.
     *
     * @param newPriority il nuovo valore della priorità
     */
    void setPriority(double newPriority);

    /**
     * Restituisce la handle corrente dell'elemento, cioè l'indice che identifica
     * la posizione dell'elemento nella struttura dati che rappresenta lo heap.
     *
     * @return la handle corrente (indice nello heap)
     */
    int getHandle();

    /**
     * Imposta la handle dell'elemento ad un nuovo valore.
     *
     * @param newHandle il nuovo valore della handle (nuovo indice nello heap)
     */
    void setHandle(int newHandle);
}