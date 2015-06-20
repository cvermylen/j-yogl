/* Copyright (C) 2003 Symphonix
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.
   
   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.
   
   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
   MA 02111-1307, USA */
   
package net.sf.yogl.types;

import net.sf.yogl.AbstractGraph;
import net.sf.yogl.GraphAdapter;
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

public final class DirectedAcyclicGraph extends GraphAdapter {

	public DirectedAcyclicGraph(AbstractGraph graph) {
		super(graph);
	}

	/** @see graph.AbstractGraph.getNeighbors
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKeyFrom, int steps)
		throws GraphException {

		return graph.getSuccessorNodesKeys(nodeKeyFrom, steps);
	}

	/** @see graph.AbstractGraph.getNeighbors
	 */
	public Object[] getSuccessorNodesKeys(Object nodeKeyFrom, Object linkKey)
		throws GraphException {

		return graph.getSuccessorNodesKeys(nodeKeyFrom, linkKey);
	}

	/** @see graph.AbstractGraph.getLinks
	 */
	public Object[] getLinksKeysBetween(Object nodeKeyFrom, Object nodeKeyTo) throws NodeNotFoundException
		{

		return graph.getLinksKeysBetween(nodeKeyFrom, nodeKeyFrom);
	}

	public void addNode(Object nodeKey, Object nodeValue)
		throws GraphException {

		try {
			super.addNode(nodeKey, nodeValue);
		} catch (DuplicateVertexException e) {
			//do nothing
		}
	}

	/** @see graph.AbstractGraph.insertLink
	 */
	public void addLinkLast(
		Object nodeKeyFrom,
		Object nodeKeyTo,
		Object linkKey,
		Object linkValue)
		throws
			NodeNotFoundException,
			DuplicateLinkException,
			GraphCorruptedException {

		graph.addLinkLast(nodeKeyFrom, nodeKeyTo, linkKey, linkValue);
	}

	/** @see graph.AbstractGraph.removeNode
	 */
	public void removeNode(Object nodeKey) throws NodeNotFoundException {

		if (nodeKey != null) {
			super.removeNode(nodeKey);
		}
	}

	/** @see graph.AbstractGraph.removeLink
	 */
	public void removeLink(Object nodeFrom, Object nodeTo, Object linkKey)
		throws GraphException {

		graph.removeLink(nodeFrom, nodeTo, linkKey);
	}
}
