   
package net.sf.yogl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.yogl.exceptions.NodeNotFoundException;

public class BreadthFirstIterator<VC extends Vertex<VC, TC>, TC extends Edge<TC, VC>> implements Iterator<VC> {

	/** breadth first working structure
	 */
	private Deque<VC> queue = new ArrayDeque<>();

	/** Maximum number of times a node can be visited.
	 */
	private int maxCycling;

	/** Creates new BreadthFirstIterator. The instance is created by an
	 *  implementation of AbstractGraph
	 *  Precondition: graph & node must contain valid values. All node
	 *  counters (used to count the number of visits) must be reset.
	 */
	public BreadthFirstIterator(Graph<VC, TC> graph, int maxCycling)	throws NodeNotFoundException {
		queue.addAll(graph.getRoots());
		this.maxCycling = maxCycling;
		graph.clearAllVisitCounts();
	}

	/** Returns the next node key in breadth first order
	 */
	public VC next() throws NoSuchElementException {
		if (queue.isEmpty())
			throw new NoSuchElementException("Empty iterator");
		VC current = queue.poll();
		if (current != null) {
			current.getOutgoingEdges().stream().map(edge -> edge.getToVertex())
					.filter(vertex -> vertex.getVisitsCount() < maxCycling).peek(vertex -> vertex.incVisitCounts())
					.forEach(vertex -> queue.addLast((VC) vertex));
		}
		return current;
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
