   
package net.sf.yogl.types;

import net.sf.yogl.adjacent.keyMap.GraphAdapter;
import net.sf.yogl.exceptions.DuplicateLinkException;
import net.sf.yogl.exceptions.GraphCorruptedException;
import net.sf.yogl.exceptions.GraphException;
import net.sf.yogl.exceptions.NodeNotFoundException;
import net.sf.yogl.impl.DuplicateVertexException;

/** 
 * Cannot be used in conjunction with DupNodesGraph
 *
 * method boolean isEmpty is inherited
 * method int getCountNodes is inherited
 * method int getCountLinks is inherited
 * method LinkedList getNeighbors is inherited
 * method LinkedList getNeighbors is inherited
 */

public final class DirectedAcyclicGraph<VK extends Comparable<VK>, VV, EK extends Comparable<EK>, EV> extends GraphAdapter<VK,VV,EK,EV> {

	/** @see graph.ComparableKeysGraph.getNeighbors
	 */
	public VK[] getSuccessorNodesKeys(VK nodeKeyFrom, int steps)
		throws GraphException {

		return graph.getSuccessorNodesKeys(nodeKeyFrom, steps);
	}

	/** @see graph.ComparableKeysGraph.getNeighbors
	 */
	public VK[] getSuccessorNodesKeys(VK nodeKeyFrom, EK linkKey)
		throws GraphException {

		return graph.getSuccessorNodesKeys(nodeKeyFrom, linkKey);
	}

	/** @see graph.ComparableKeysGraph.getLinks
	 */
	public EK[] getLinksKeysBetween(VK nodeKeyFrom, VK nodeKeyTo) throws NodeNotFoundException
		{

		return graph.getLinksKeysBetween(nodeKeyFrom, nodeKeyFrom);
	}

	public void addNode(VK nodeKey, VV nodeValue)
		throws GraphException {

		try {
			super.addNode(nodeKey, nodeValue);
		} catch (DuplicateVertexException e) {
			//do nothing
		}
	}

	/** @see graph.ComparableKeysGraph.insertLink
	 */
	public void addLinkLast(
		VK nodeKeyFrom,
		VK nodeKeyTo,
		EK linkKey,
		EV linkValue)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {

		graph.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.ComparableKeysGraph.removeNode
	 */
	public void removeNode(VK nodeKey) throws NodeNotFoundException {

		if (nodeKey != null) {
			super.removeNode(nodeKey);
		}
	}

	/** @see graph.ComparableKeysGraph.removeLink
	 */
	public void removeLink(VK nodeFrom, VK nodeTo, EK linkKey)
		throws GraphException {

		graph.removeLink(nodeFrom, nodeTo, linkKey);
	}
}
