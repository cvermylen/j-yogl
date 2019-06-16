   
package net.sf.yogl.adjacent.keyMap;

import java.util.ArrayList;
import java.util.Iterator;

import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.ImplementationGraph;

/** A LinksIterator allows to retrieve all links from a given graph.
 *  When pointing to a specific link, the iterator gives also
 *  which nodes the link connect.
 *  
 *  REFACTOR: From a given Vertex, returns an iterator on all outgoing edges (key & value)
 */
public class LinksIterator<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> implements Iterator<EV> {

	private ArrayList<VK> predNodes = null;
	private ArrayList<EV> links = null;
	private ArrayList<VK> succNodes = null;
	private int counter = 0;

	public LinksIterator(ImplementationGraph<VK, VV, EK, EV> graph) throws NodeNotFoundException {

		int count = graph.getLinkCount();
		int index = 0;
		predNodes = new ArrayList<VK>(count);
		links = new ArrayList<EV>(count);
		succNodes = new ArrayList<VK>(count);

		Iterator<VK> nodesIter = graph.nodesKeySet().iterator();
		while (nodesIter.hasNext()) {
			VK key = nodesIter.next();
			AdjKeyVertex<VK, VV, EK, EV> vertex = graph.findVertexByKey(key);
			AdjKeyEdge<VK, VV, EK, EV>[]neighbors = vertex.getNeighbors();
			for(int i=0; i < neighbors.length; i++) {
				predNodes.add(index, key);
				links.add(index, neighbors[i].getUserValue());
				VK destKey = neighbors[i].getNextVertexKey();
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
	public EV next() {

		EV result = null;
		if (hasNext()) {
			result = links.get(counter);
			counter++;
		}
		return result;
	}

	/** Returns the source node of the last 'read' link
	 */
	public VK getOriginator() {

		VK result = null;
		if ((counter <= links.size()) && (counter > 0)) {
			result = predNodes.get(counter - 1);
		}
		return result;
	}

	/** Returns the sink node of the last 'read' node
	 */
	public VK getDestination() {

		VK result = null;
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
