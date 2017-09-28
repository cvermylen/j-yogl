/* Default implementation. No assumption about the implementation of the graph */
   
package net.sf.yogl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.yogl.exceptions.GraphException;

public class BreadthFirstIterator implements Iterator<Vertex> {

	/** breadth first working structure
	 */
	private Deque<Vertex> queue = new ArrayDeque<>();

	/** Maximum number of times a node can be visited.
	 */
	private int maxCycling;

	/** Creates new BreadthFirstIterator. The instance is created by an
	 *  implementation of AbstractGraph
	 *  Precondition: graph & node must contain valid values. All node
	 *  counters (used to count the number of visits) must be reset.
	 */
	public BreadthFirstIterator(
		Graph graph,
		int maxCycling)
		throws GraphException {
		queue.addAll(graph.getRoots());
		this.maxCycling = maxCycling;
		graph.clearAllVisitCounts();
	}

	/** Returns the next node key in breadth first order
	 */
	public Vertex next() throws NoSuchElementException {
		try {
			if (queue.isEmpty())
				throw new NoSuchElementException("Empty iterator");
			Vertex current = queue.poll();
			if(current != null){
				List<? extends Edge>nextEdges = current.getOutgoingEdges();
				
				for (Edge edge: nextEdges) {
					Vertex v = edge.getOutgoingVertex();
					if(v.getVisitsCount() < maxCycling){
						queue.addLast(v);
						v.incVisitCounts();
					}
				}
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
