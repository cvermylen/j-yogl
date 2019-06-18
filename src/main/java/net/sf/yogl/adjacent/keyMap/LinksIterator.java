   
package net.sf.yogl.adjacent.keyMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.yogl.Edge;
import net.sf.yogl.Graph;
import net.sf.yogl.Vertex;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.types.VertexType;

/** A LinksIterator allows to retrieve all links from a given graph.
 *  When pointing to a specific link, the iterator gives also
 *  which nodes the link connect.
 *  
 *  REFACTOR: From a given Vertex, returns an iterator on all outgoing edges (key & value)
 */
public class LinksIterator<V extends Vertex<E>, E extends Edge<V>> implements Iterator<E> {

	private ArrayList<V> predNodes = null;
	private ArrayList<E> links = null;
	private ArrayList<V> succNodes = null;
	private int counter = 0;

	public LinksIterator(Graph<V, E> graph) throws NodeNotFoundException {

		int count = graph.getLinkCount();
		int index = 0;
		predNodes = new ArrayList<V>(count);
		links = new ArrayList<E>(count);
		succNodes = new ArrayList<V>(count);

		Iterator<V> nodesIter = graph.getVertices(VertexType.ANY).iterator();
		while (nodesIter.hasNext()) {
			V vertex = nodesIter.next();
			Collection<E> neighbors = vertex.getOutgoingEdges();
			Iterator<E> edgesIter = neighbors.iterator();
			while (edgesIter.hasNext()) {
				E edge = edgesIter.next();
				predNodes.add(index, vertex);
				links.add(index, edge);
				V destKey = edge.getOutgoingVertex();
				succNodes.add(index++, destKey);
			}
		}
	}

	/** Returns true if there are still un-read elements in the iterator
	 */
	public boolean hasNext() {

		return counter < links.size();
	}

	/** Read the next link from the iterator
	 */
	public E next() {

		E result = null;
		if (hasNext()) {
			result = links.get(counter);
			counter++;
		}
		return result;
	}

	/** Returns the source node of the last 'read' link
	 */
	public V getOriginator() {

		V result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = predNodes.get(counter - 1);
		}
		return result;
	}

	/** Returns the sink node of the last 'read' node
	 */
	public V getDestination() {

		V result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = succNodes.get(counter - 1);
		}
		return result;
	}

	public void remove()
		throws UnsupportedOperationException, IllegalStateException {

		throw new UnsupportedOperationException("remove not defined in LinksIterator");
	}
}
