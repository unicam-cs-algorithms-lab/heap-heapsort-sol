package it.unicam.cs.asdl2526.es10sol;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;
/** Verifiche aggiuntive dei contratti pubblici e degli invarianti. */
class ContrattiHeapAggiuntiviTest {
 @Test void heapConDuplicatiEOrdinamento() {
   // Ogni estrazione deve restituire il massimo residuo, anche con duplicati.
   MaxHeap<Integer> h = new MaxHeap<Integer>();
   for (int n : new int[]{3,1,3,-2,9,9,0}) h.insert(n);
   for (int expected : new int[]{9,9,3,3,1,0,-2}) assertEquals(Integer.valueOf(expected),h.extractMax());
   assertTrue(h.isEmpty()); assertNull(h.extractMax()); assertNull(h.getMax());
 }
 @Test void costruttoreDaListaNonModificaInput() {
   // La costruzione non deve riordinare la lista originale del chiamante.
   List<Integer> original = new ArrayList<Integer>(Arrays.asList(2,8,1,5));
   MaxHeap<Integer> h = new MaxHeap<Integer>(original);
   assertEquals(Arrays.asList(2,8,1,5),original);
   assertEquals(Integer.valueOf(8),h.getMax()); assertEquals(4,h.size());
 }
 @Test void heapsortConDuplicatiENegativi() {
   // Verifica la permutazione ordinata e il conteggio non negativo dei confronti.
   List<Integer> input = new ArrayList<Integer>(Arrays.asList(4,-1,4,0,9,-1));
   SortingAlgorithmResult<Integer> result = new HeapSort<Integer>().sort(input);
   assertEquals(Arrays.asList(-1,-1,0,4,4,9),input);
   assertTrue(result.checkOrder()); assertTrue(result.getCountCompare() >= 0);
 }
 @Test void inputNulli() {
   assertThrows(NullPointerException.class,()->new MaxHeap<Integer>((List<Integer>)null));
   assertThrows(NullPointerException.class,()->new MaxHeap<Integer>().insert(null));
   assertThrows(NullPointerException.class,()->new HeapSort<Integer>().sort(null));
 }
}
