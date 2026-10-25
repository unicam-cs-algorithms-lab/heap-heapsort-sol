# Esercitazione — Max-heap, HeapSort e coda di priorità con min-heap

## Presentazione e obiettivi

Questa è la prima esercitazione del Laboratorio di Algoritmi e Strutture Dati dedicata all'implementazione di strutture dati e algoritmi dopo il richiamo a Java. Accompagna le slide di teoria **Heap e Heapsort** e comprende **tre attività di programmazione distinte ma collegate**:

1. **Max-heap binario**: implementare la struttura dati `MaxHeap<E>` e le operazioni fondamentali su un heap massimo.
2. **HeapSort**: implementare l'algoritmo `HeapSort<E>` che ordina una lista *in-place*, costruendo inizialmente il max-heap con l'algoritmo bottom-up.
3. **Coda di priorità minima**: implementare `BinaryHeapMinPriorityQueue` usando un min-heap e le *handle* per aggiornare le priorità senza effettuare ricerche lineari.

Le tre attività permettono di applicare i concetti delle lezioni su generics, interfacce, `Comparable`, invarianti di rappresentazione, complessità degli algoritmi, Javadoc e test JUnit.

## Attività 1 — Max-heap binario (`MaxHeap.java`)

Un **max-heap** è un albero binario quasi completo nel quale ogni nodo ha una chiave maggiore o uguale a quelle dei figli. È rappresentato mediante una `ArrayList<E>` con indici da zero: per un nodo in posizione `i`, il padre è `(i - 1) / 2` (per `i > 0`), il figlio sinistro `2 * i + 1` e quello destro `2 * i + 2`.

Completare i TODO di `MaxHeap<E>`: costruzione da una lista, inserimento, calcolo degli indici, lettura ed estrazione del massimo e ripristino della proprietà di heap. Verificare i casi di heap vuoto, heap con un elemento, duplicati e sequenze già ordinate. `getMax()` richiede `O(1)`; inserimento ed estrazione del massimo richiedono `O(log n)` nel caso peggiore.

### Due strategie per costruire un heap da una lista

**Inserimenti successivi — `O(n log n)` nel caso peggiore.** Si parte da un heap vuoto e si inseriscono gli `n` elementi uno alla volta. Ogni nuovo elemento viene collocato in fondo e può risalire per un cammino lungo al più `O(log n)`; sommando i costi degli inserimenti si ottiene il limite `O(n log n)`. **È la strategia richiesta dal costruttore `MaxHeap(List<E>)` in questa esercitazione.**

**Costruzione bottom-up — `Θ(n)`.** Si copiano gli elementi nella rappresentazione ad array e si applica `heapify` verso il basso ai nodi interni, dall'ultimo genitore alla radice. Le foglie soddisfano già la proprietà di heap. Una singola chiamata può costare `O(log n)`, ma quasi tutti i nodi si trovano vicino alle foglie e richiedono discese brevi. I nodi di altezza `h` sono al più `⌈n / 2^(h+1)⌉`; il lavoro complessivo è limitato da una costante per `n` moltiplicata per la serie convergente `Σ(h+1)/2^h`. Quindi il costo è `O(n)` e, dovendo considerare tutti gli elementi, `Θ(n)`. **Questa strategia è richiesta nella prima fase di HeapSort.**

Le slide del Cormen utilizzano indici a partire da 1: nel codice Java le formule vanno adattate agli indici da 0.

## Attività 2 — HeapSort (`HeapSort.java`)

Completare `HeapSort<E>` implementando `sort(List<E>)` secondo il contratto di `SortingAlgorithm<E>`. L'algoritmo deve ordinare la lista ricevuta *in-place*: non deve costruire una seconda struttura dati contenente tutti gli elementi.

Prima si costruisce il max-heap **bottom-up in `Θ(n)`**; poi si scambia ripetutamente il massimo alla radice con l'ultimo elemento della parte ancora da ordinare, si riduce la dimensione logica dello heap e si ripristina la proprietà di max-heap. La parte finale della lista contiene progressivamente gli elementi già nella posizione definitiva. Il costo totale è `O(n log n)`.

`SortingAlgorithm<E>` e `SortingAlgorithmResult<E>` sono già forniti: introducono il contratto che sarà riutilizzato nella successiva esercitazione sugli algoritmi di ordinamento e sul loro confronto sperimentale. Il conteggio dei confronti riguarda i confronti tra elementi (chiamate a `compareTo`), non i controlli sugli indici.

## Attività 3 — Coda di priorità minima (`BinaryHeapMinPriorityQueue.java`)

Una **coda di priorità minima** permette di inserire elementi, consultare o estrarre quello con priorità minore e diminuire la priorità di un elemento già presente. La rappresentazione richiesta è un **min-heap binario**: ogni genitore ha priorità minore o uguale a quella dei figli.

Completare i TODO di `BinaryHeapMinPriorityQueue`, rispettando i contratti Javadoc e le condizioni d'errore. Le interfacce e classi di supporto `PriorityQueueElement` e `MySimpleElement` sono già fornite e **non richiedono implementazione**.

### Perché l'handle è fondamentale

L'interfaccia `PriorityQueueElement` mette a disposizione una *handle*, cioè l'indice corrente dell'elemento nell'array che rappresenta il min-heap. Senza handle, `decreasePriority(element, newPriority)` dovrebbe prima cercare l'elemento, impiegando `O(n)` nel caso peggiore. Con la handle, l'elemento viene localizzato in `O(1)` e la risalita necessaria per ristabilire l'ordine richiede `O(log n)`.

Per ogni elemento `e` presente in posizione `i` deve quindi valere l'**invariante** `e.getHandle() == i`. Ogni scambio di due elementi deve aggiornare anche **entrambe le handle**; quando un elemento viene rimosso, la sua handle deve essere invalidata secondo il contratto della classe. La verifica della presenza utilizza anche l'identità del riferimento (`==`), non soltanto `equals`. Lo stesso oggetto non può occupare contemporaneamente due posizioni della coda.

| Operazione | Complessità |
|---|---|
| `minimum()` | `O(1)` |
| `insert(element)` | `O(log n)` |
| `extractMinimum()` | `O(log n)` |
| `decreasePriority(element, newPriority)` con handle | `O(log n)` |
| Ricerca lineare prima della diminuzione di priorità | `O(n)` |

## Classi fornite e classi da completare

| Classe | Ruolo | Da completare? |
|---|---|---|
| `MaxHeap.java` | Max-heap generico | **Sì, attività 1** |
| `HeapSort.java` | Algoritmo HeapSort | **Sì, attività 2** |
| `BinaryHeapMinPriorityQueue.java` | Min-heap con handle | **Sì, attività 3** |
| `PriorityQueueElement.java` | Contratto degli elementi della coda | No |
| `MySimpleElement.java` | Elemento di esempio | No |
| `SortingAlgorithm.java` | Interfaccia comune agli ordinamenti | No |
| `SortingAlgorithmResult.java` | Risultato di un ordinamento | No |

## Consegna su Moodle

**Consegnare esclusivamente questi tre file Java**, dopo aver completato tutti i TODO richiesti:

1. `MaxHeap.java`
2. `HeapSort.java`
3. `BinaryHeapMinPriorityQueue.java`

I file si trovano nella cartella `src/main/java/it/unicam/cs/asdl2526/es10/` del progetto studenti. **Non consegnare** il progetto ZIP, i test JUnit, il README, il `pom.xml` né le quattro classi di supporto già implementate. Mantenere invariati package, nomi delle classi, firme dei metodi e contratti delle API, così da consentire la compilazione e l'esecuzione dei test.

## Esecuzione, test e metodo di studio

Aprire il progetto Maven in IntelliJ IDEA ed eseguire dalla radice:

```text
mvn test
```

Tutti i test JUnit sono presenti nel progetto, senza distinzione tra test pubblici e segreti. I test verificano casi ordinari, casi limite e, per la coda di priorità, la coerenza delle handle dopo gli spostamenti. Leggere i test e le Javadoc **prima** di implementare i metodi: fanno parte del materiale didattico, ma non sostituiscono la comprensione e la dimostrazione degli invarianti.

Percorso consigliato: partire da `MaxHeap`, passare a `HeapSort` confrontando i due metodi di costruzione, e affrontare infine la coda di priorità, concentrandosi sulla sincronizzazione tra array e handle. La versione con soluzione proposta è destinata alla verifica successiva del proprio lavoro.
