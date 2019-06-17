   
package net.sf.yogl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.yogl.exceptions.GraphException;

public class BreadthFirstIterator<V extends Vertex<E>, E extends Edge<V>> implements Iterator<V> {

	/** breadth first working structure
	 */
	private Deque<V> queue = new ArrayDeque<>();

	/** Maximum number of times a node can be visited.
	 */
	private int maxCycling;

	/** Creates new BreadthFirstIterator. The instance is created by an
	 *  implementation of AbstractGraph
	 *  Precondition: graph & node must contain valid values. All node
	 *  counters (used to count the number of visits) must be reset.
	 */
	public BreadthFirstIterator(Graph<V, E> graph, int maxCycling)	throws GraphException {
		queue.addAll(graph.getRoots());
		this.maxCycling = maxCycling;
		graph.clearAllVisitCounts();
	}

	/** Returns the next node key in breadth first order
	 */
	public V next() throws NoSuchElementException {
		try {
			if (queue.isEmpty())
				throw new NoSuchElementException("Empty iterator");
			V current = queue.poll();
			if(current != null){
				current.getOutgoingEdges().stream()
				.map(edge -> edge.getOutgoingVertex())
				.filter(vertex -> vertex.getVisitsCount() < maxCycling)
				.peek(vertex ->vertex.incVisitCounts())
				.forEach (vertex ->queue.addLast((V) vertex)); 
			}
			return current;
		} catch (GraphException e1) {
			throw new NoSuchElementException(e1.getMessage());
		}
	}

	/** Return false when the iterator reaches the end of the graph
	 */
	public boolean hasNext() {
		return !queue.isEmpty();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
